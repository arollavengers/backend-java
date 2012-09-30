package arollavengers.core.domain.pandemic;

import arollavengers.core.domain.user.User;
import arollavengers.core.events.pandemic.CityInfectedEvent;
import arollavengers.core.events.pandemic.CurrentPlayerDefinedEvent;
import arollavengers.core.events.pandemic.FirstPlayerDesignatedEvent;
import arollavengers.core.events.pandemic.GameStartedEvent;
import arollavengers.core.events.pandemic.InfectionDrawPileCreatedEvent;
import arollavengers.core.events.pandemic.OutbreakChainTriggeredEvent;
import arollavengers.core.events.pandemic.PandemicEvent;
import arollavengers.core.events.pandemic.PlayerDrawPileCreatedEvent;
import arollavengers.core.events.pandemic.ResearchCenterBuiltEvent;
import arollavengers.core.events.pandemic.WorldCityCuredEvent;
import arollavengers.core.events.pandemic.WorldCityTreatedEvent;
import arollavengers.core.events.pandemic.WorldCreatedEvent;
import arollavengers.core.events.pandemic.WorldDiseaseEradicatedEvent;
import arollavengers.core.events.pandemic.WorldMemberActionSpentEvent;
import arollavengers.core.events.pandemic.WorldMemberJoinedTeamEvent;
import arollavengers.core.exceptions.EntityAlreadyCreatedException;
import arollavengers.core.exceptions.pandemic.FirstPlayerNotDefinedException;
import arollavengers.core.exceptions.pandemic.GameAlreadyStartedException;
import arollavengers.core.exceptions.pandemic.InvalidMoveException;
import arollavengers.core.exceptions.pandemic.InvalidPlayerTurnException;
import arollavengers.core.exceptions.pandemic.InvalidUserException;
import arollavengers.core.exceptions.pandemic.MemberNotFoundException;
import arollavengers.core.exceptions.pandemic.NoDiseaseToCureException;
import arollavengers.core.exceptions.pandemic.NotEnoughPlayerException;
import arollavengers.core.exceptions.pandemic.PandemicRuntimeException;
import arollavengers.core.exceptions.pandemic.UserAlreadyRegisteredException;
import arollavengers.core.exceptions.pandemic.WorldNotYetCreatedException;
import arollavengers.core.exceptions.pandemic.WorldNumberOfRoleLimitReachedException;
import arollavengers.core.exceptions.pandemic.WorldRoleAlreadyChosenException;
import arollavengers.core.infrastructure.AggregateRoot;
import arollavengers.core.infrastructure.AnnotationBasedEventHandler;
import arollavengers.core.infrastructure.EventHandler;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;
import arollavengers.core.infrastructure.annotation.OnEvent;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class World extends AggregateRoot<PandemicEvent> {

    public static final int MAX_TEAM_SIZE = 4;
    public static final int MIN_TEAM_SIZE = 2;
    public static final int NB_ACTIONS = 4;
    public static final int OUTBREAK_THRESHOLD = OutbreakChain.OUTBREAK_THRESHOLD;
    public static final int NB_PLAYER_CARDS_PER_TURN = 2;

    private final UnitOfWork uow;
    private final EventHandler<PandemicEvent> eventHandler;

    // ~~~ World state
    public boolean userCannotBeRegisteredTwice = true;
    private Id ownerId = Id.undefined();

    private Difficulty difficulty;

    private Team team;

    private CityStates cityStates;

    private Set<Disease> eradicatedDiseases;
    private Set<Disease> curesDiscovered;

    private boolean started;

    private PlayerDrawPile playerDrawPile;
    private InfectionDrawPile infectionDrawPile;

    private InfectionRate infectionRate;

    private int outbreaks = -1;
    private MemberKey currentMemberKey;


    public World(Id worldId, UnitOfWork uow) {
        super(worldId);
        this.uow = uow;
        this.eventHandler = new AnnotationBasedEventHandler<PandemicEvent>(this);
    }

    /**
     * Create a new world of given id and difficulty owned by a user
     *
     * @param owner      Owner of this world
     * @param difficulty Difficulty of the game
     */
    public void createWorld(User owner, Difficulty difficulty) {
        ensureValidUser(owner);
        ensureEntityWasNotAlreadyCreated();

        applyNewEvent(new WorldCreatedEvent(entityId(), owner.entityId(), difficulty));
    }

    private void ensureEntityWasNotAlreadyCreated() {
        if (!ownerId.isUndefined()) {
            throw new EntityAlreadyCreatedException(entityId());
        }
    }

    @OnEvent
    private void doCreateWorld(WorldCreatedEvent event) {
        this.difficulty = event.difficulty();
        this.ownerId = event.ownerId();
        this.cityStates = new CityStates();
        this.eradicatedDiseases = new HashSet<Disease>();
        this.curesDiscovered = new HashSet<Disease>();
        this.team = new Team();
        this.infectionRate = InfectionRate.first;
        this.outbreaks = 0;
    }

    /**
     * Register a new user with the given role in the game.
     *
     * @param user user that wants to join the game
     * @param role role of the user in the game
     */
    public void registerMember(User user, MemberRole role) {
        ensureWorldIsCreated();
        ensureValidUser(user);
        ensureGameIsNotAlreadyStarted();

        if (team().size() >= MAX_TEAM_SIZE) {
            throw new WorldNumberOfRoleLimitReachedException(role);
        }

        if (team().hasRole(role)) {
            throw new WorldRoleAlreadyChosenException(entityId(), role);
        }

        // re-think of the rule, MemberKey is probably more flexible
        if (userCannotBeRegisteredTwice) {
            Optional<Member> member = team().findMember(user.entityId());
            if (member.isPresent()) {
                throw new UserAlreadyRegisteredException(entityId(), user.entityId());
            }
        }

        applyNewEvent(new WorldMemberJoinedTeamEvent(entityId(), Id.next(Member.class), user.entityId(), role));

        MemberKey memberKey = new MemberKey(user.entityId(), role);
        team().findMember(memberKey).get().setPositionOnTable(team.size() - 1);
    }

    private void ensureGameIsNotAlreadyStarted() {
        if (isStarted()) {
            throw new GameAlreadyStartedException();
        }
    }

    private void ensureValidUser(final User user) {
        if (user.entityId().isUndefined()) {
            throw new InvalidUserException(user);
        }
    }

    @OnEvent
    private void doEnrole(final WorldMemberJoinedTeamEvent event) {
        Member newMember = new Member(aggregate(), event.memberId(), event.newComerId(), event.role());
        team.enrole(newMember);
    }

    public void designateFirstPlayer(User user, MemberRole role) {
        designateFirstPlayer(new MemberKey(user.entityId(), role));
    }

    /**
     * @param memberKey member's key designated as the first player to play.
     */
    public void designateFirstPlayer(MemberKey memberKey) {
        ensureWorldIsCreated();
        ensureGameIsNotAlreadyStarted();

        Member member = team().findMember(memberKey).or(memberNotFoundException(memberKey));
        applyNewEvent(new FirstPlayerDesignatedEvent(entityId(), member.memberKey()));
    }

    @OnEvent
    private void onDesignateFirstPlayer(FirstPlayerDesignatedEvent event) {
        currentMemberKey = event.memberKey();
    }

    private static Supplier<? extends Member> memberNotFoundException(final MemberKey memberKey) {
        return new Supplier<Member>() {
            @Override
            public Member get() {
                throw new MemberNotFoundException(memberKey);
            }
        };
    }

    /**
     * Start the game. It does the following actions:
     * - start the game (nobody can register for playing anymore)
     * - initialize draw cards
     * - give initial hand to member
     */
    public void startGame() {
        ensureWorldIsCreated();
        ensureGameIsNotAlreadyStarted();
        ensureTeamSizeIsEnough();
        ensureCurrentPlayerHasBeenDesignated();

        preparePlayerCards();
        prepareInfectionCards();

        applyNewEvent(new ResearchCenterBuiltEvent(entityId(), startCity()));
        moveAllMembersToStartCity();
        applyNewEvent(new GameStartedEvent(entityId()));
        startCurrentPlayerTurn();
    }

    private void moveAllMembersToStartCity() {
        for (Member member : team()) {
            member.moveTo(startCity(), MoveType.Setup, Optional.<PlayerCard>absent());
        }
    }

    private void prepareInfectionCards() {
        applyNewEvent(new InfectionDrawPileCreatedEvent(entityId(), Id.next(InfectionDrawPile.class)));
        infectionDrawPile.initialize();

        // TODO make city a dedicated entity?
        for (Integer nbCubes : Arrays.asList(3, 3, 3, 2, 2, 2, 1, 1, 1)) {
            InfectionCard card = infectionDrawPile.drawTop();
            handleInfectionCard(card, nbCubes);
        }
    }

    private void handleInfectionCard(InfectionCard card, int nbCubes) {
        switch (card.cardType()) {
            case City:
                infectCity(((InfectionCityCard) card).cityId(), nbCubes);
                break;
            default:
                throw new IllegalStateException("unknown card type");
        }
    }

    public void infectCity(CityId cityId, int nbCubes) {

        Disease disease = cityId.defaultDisease();

        // check for outbreak
        CityState cityState = cityStates.getStateOf(cityId);
        int nbActualCubes = cityState.numberOfCubes(disease);
        if (nbActualCubes + nbCubes >= OUTBREAK_THRESHOLD) {
            // booommm !
            OutbreakGenerationChain outbreakChain = OutbreakGenerationChain.calculate(
                    cityId, disease, CityGraph.getInstance(), cityStates);

            EnumMap<CityId, Integer> resultingInfections = outbreakChain.getResultingInfections();
            OutbreakGenerationChain.OutbreakGenerationMap generations = outbreakChain.toOutbreakGenerationMap();

            // Each time a city outbreaks, move the Outbreaks Marker up one space on the Outbreak Indicator.
            // If the number of outbreaks ever reaches 8 (and the Outbreaks Marker reaches the skull symbol),
            // the game immediately ends in defeat for all players.
            applyNewEvent(new OutbreakChainTriggeredEvent(entityId(), generations, disease, resultingInfections));

            // TODO add in the generation order:
            //   per outbreaked city:
            //     CityOutbreakedEvent(cityId, generation) (which is merely more a notification than an event)
            //     CityInfectedEvent(infestedCity...)
        }
        else {
            applyNewEvent(new CityInfectedEvent(entityId(), cityId, disease, nbCubes));
        }

        // TODO
        // Also, if there are not enough cubes to
        // add to the board when infecting, the game
        // immediately ends in defeat for all players.

        // TODO
        // Outbreak : Each time a city outbreaks, move
        // the Outbreaks Marker up one space on the Outbreak Indicator.
        // If the number of outbreaks ever reaches 8 (and the Outbreaks
        // Marker reaches the skull symbol), the game immediately ends
        // in defeat for all players. Also, if there are not enough
        // cubes to add to the board when infecting, the game immediately
        // ends in defeat for all players.
    }

    @OnEvent
    private void onCityInfected(CityInfectedEvent event) {
        CityState cityState = cityStates.getStateOf(event.cityId());
        cityState.addCubes(event.disease(), event.nbCubes());
    }

    @OnEvent
    private void onOutbreakTriggered(OutbreakChainTriggeredEvent event) {
        EnumMap<CityId, Integer> resultingInfections = event.resultingInfections();
        for (Map.Entry<CityId, Integer> entry : resultingInfections.entrySet()) {
            CityState cityState = cityStates.getStateOf(entry.getKey());
            cityState.setCubes(event.disease(), entry.getValue());
        }


        outbreaks += event.generations().numberOfOutbreaks();
    }

    private void preparePlayerCards() {
        applyNewEvent(new PlayerDrawPileCreatedEvent(entityId(), Id.next(PlayerDrawPile.class)));
        playerDrawPile.initialize();

        int nbCardsPerPlayer = nbCardsPerPlayer(team().size());
        for (Member member : team()) {
            for (int i = 0; i < nbCardsPerPlayer; i++) {
                member.addToHand(playerDrawPile.drawTop());
            }
        }
        playerDrawPile.completeForDifficulty(difficulty());
    }

    private CityId startCity() {
        return CityId.Atlanta;
    }

    private void ensureCurrentPlayerHasBeenDesignated() {
        if (currentMemberKey == null) {
            throw new FirstPlayerNotDefinedException();
        }
    }

    private void ensureTeamSizeIsEnough() {
        final int teamSize = team().size();
        if (teamSize < MIN_TEAM_SIZE) {
            throw new NotEnoughPlayerException(teamSize, MIN_TEAM_SIZE);
        }
    }

    private int nbCardsPerPlayer(int teamSize) {
        return 6 - teamSize;
    }

    @OnEvent
    private void doCreateInfectionDrawPile(final InfectionDrawPileCreatedEvent event) {
        infectionDrawPile = new InfectionDrawPile(aggregate(), event.drawPileId());
    }


    @OnEvent
    private void doCreatePlayerDrawPile(final PlayerDrawPileCreatedEvent event) {
        playerDrawPile = new PlayerDrawPile(aggregate(), event.drawPileId());
    }

    @OnEvent
    private void doStartGame(final GameStartedEvent event) {

        // Caution: 'onEvent' must be deterministic
        // due to the replay behavior used to reload the aggregate
        // random sequence must be calculated once, and stored in an event
        // for replay
        //
        // ~~~ don't do this:
        // playerDrawPile.buildAndShuffle(event.randomSeq());

        // no event triggering in 'OnEvent' : only state assignments
        // otherwise new events are generated at each 'replay/reloadFromHistory'
        //
        // ~~~ don't do this:
        // for (Member member : team()) {
        //     for (int i = team().size(); i < 6; i++) {
        //         applyNewEvent(new PlayerCardAddedToHandEvent(entityId(), member));
        //     }
        // }
        // applyNewEvent(new ResearchCenterBuiltEvent(entityId(), CityId.Atlanta));

        this.started = true;
    }

    @OnEvent
    private void doBuildResearchCenter(final ResearchCenterBuiltEvent event) {
        cityStates.buildResearchCenter(event.city());
    }

    /**
     * Treat a city by a member of team removing one cube of given disease
     *
     * @param memberKey Action doer
     * @param city      City to treat
     * @param disease   Disease to treat
     */
    public void treatCity(MemberKey memberKey, CityId city, Disease disease) {
        Member member = team().findMember(memberKey).or(memberNotFoundException(memberKey));

        // fail if it is not player's turn or no more action point
        member.ensureActionIsAuthorized();

        CityState cityState = cityStates.getStateOf(city);
        int cityDiseaseCubes = cityState.numberOfCubes(disease);
        if (cityDiseaseCubes == 0) {
            throw new NoDiseaseToCureException(entityId(), city, disease);
        }

        applyNewEvent(new WorldMemberActionSpentEvent(entityId(), member.entityId()));

        int nbCubeTreated;
        if (hasCureFor(disease) || member.role() == MemberRole.Medic) {
            nbCubeTreated = cityDiseaseCubes;
            applyNewEvent(new WorldCityCuredEvent(entityId(),
                    member.entityId(),
                    city,
                    disease));
        }
        else {
            nbCubeTreated = 1;
            applyNewEvent(new WorldCityTreatedEvent(entityId(),
                    member.entityId(),
                    city,
                    disease));
        }

        // check for eradication
        int worldDiseaseCubes = cityStates.numberOfCubes(disease);
        if (worldDiseaseCubes == nbCubeTreated) {
            applyNewEvent(new WorldDiseaseEradicatedEvent(entityId(),
                    member.entityId(),
                    disease));
        }
    }

    @OnEvent
    private void doTreatCity(WorldCityTreatedEvent event) {
        CityState cityState = cityStates.getStateOf(event.city());
        cityState.removeOneCube(event.disease());
    }

    @OnEvent
    private void doCureCity(WorldCityCuredEvent event) {
        CityState cityState = cityStates.getStateOf(event.city());
        cityState.removeAllCubes(event.disease());
    }

    @OnEvent
    private void doMarkDiseaseEradicated(WorldDiseaseEradicatedEvent event) {
        eradicatedDiseases.add(event.disease());
    }

    /**
     * Returns the world's owner id.
     */
    public Id ownerId() {
        return ownerId;
    }

    boolean hasCureFor(final Disease disease) {
        return curesDiscovered.contains(disease);
    }

    boolean hasBeenEradicated(final Disease disease) {
        return eradicatedDiseases.contains(disease);
    }

    Difficulty difficulty() {
        return difficulty;
    }

    /**
     * Team is mutable and thus should not be accessible from outside and must be kept private.
     *
     * @return The team playing
     * @see #rolesAssigned()
     * @see #isRoleAssigned(MemberRole)
     */
    private Team team() {
        return team;
    }

    @Override
    protected UnitOfWork unitOfWork() {
        return uow;
    }

    @Override
    protected EventHandler<PandemicEvent> internalEventHandler() {
        return eventHandler;
    }

    private void ensureWorldIsCreated() {
        if (ownerId().isUndefined()) {
            throw new WorldNotYetCreatedException();
        }
    }

    public Set<MemberRole> rolesAssigned() {
        return team().roles();
    }

    public boolean isRoleAssigned(MemberRole role) {
        return team().hasRole(role);
    }

    public boolean isStarted() {
        return started;
    }

    public int playerDrawPileSize() {
        if (playerDrawPile == null) {
            return 0;
        }
        return playerDrawPile.size();
    }

    public Collection<CityId> citiesWithResearchCenters() {
        return cityStates.citiesWithResearchCenters();
    }

    /**
     * Return the member's hand size. That is the number of cards the member currently has in its hand.
     */
    public int memberHandSize(@Nonnull MemberKey memberKey) {
        Optional<Member> memberOpt = team().findMember(memberKey);
        if (!memberOpt.isPresent()) {
            throw new MemberNotFoundException(memberKey);
        }

        return memberOpt.get().handSize();
    }

    /**
     * @return the current's infection rate of the game
     */
    public InfectionRate infectionRate() {
        return this.infectionRate;
    }

    public int outbreaks() {
        return this.outbreaks;
    }

    private Member currentPlayer() {
        return team().findMember(currentMemberKey).or(memberNotFoundException(currentMemberKey));
    }

    public void endCurrentPlayerTurn() {
        Member member = currentPlayer();

        // Play the infestor
        // Draw cards from the Infection Draw Pile equal to
        // the current Infection Rate and add one cube to the
        // pictured cities, using a cube of the same color
        // as each card.
        for (int i = 0; i < infectionRate.amount(); i++) {
            if (infectionDrawPile.isEmpty()) {
                throw new PandemicRuntimeException("What to do when there is no more cards?");
            }
            InfectionCard card = infectionDrawPile.drawTop();
            handleInfectionCard(card, 1);
        }

        member.endTurn();

        // After all of the Infection Cards are resolved,
        // place them into the Infection Discard Pile.
        // Your turn is over.
        // The player to the left now begins his turn.

        // TODO move positionOnTable usage into Team#nextPlayerKeyOf(member)
        int positionOnTable = member.positionOnTable();
        int nextPlayerPosition = (positionOnTable + 1) % team().size();

        MemberKey newCurrentPlayerKey = team().getMemberAtPosition(nextPlayerPosition).memberKey();
        applyNewEvent(new CurrentPlayerDefinedEvent(entityId(), newCurrentPlayerKey));
        //
        startCurrentPlayerTurn();
    }

    @OnEvent
    private void onCurrentPlayerDefined(CurrentPlayerDefinedEvent event) {
        currentMemberKey = event.memberKey();
    }

    private void startCurrentPlayerTurn() {
        Member member = currentPlayer();
        member.startTurn(NB_ACTIONS);

        for (int i = 0; i < NB_PLAYER_CARDS_PER_TURN; i++) {
            PlayerCard card = playerDrawPile.drawTop();
            if (card.cardType() == PlayerCardType.Epidemic) {
                triggerEpidemic();
            }
            else {
                member.addToHand(card);
            }
        }
    }

    private void triggerEpidemic() {
        // 1. Increase the Infection Rate
        infectionRate = infectionRate.next();

        // 2. Infect: Take the bottom card from the Infection Draw Pile
        // and add 3 cubes to the city pictured on the card, then place
        // the card into the Infection Discard Pile. Note: No city can
        // contain more than 3 cubes of any one color. If the Epidemic
        // would cause the city to exceed that limit, any excess cubes
        // are returned to the stock and an outbreak is triggered.
        InfectionCard card = infectionDrawPile.drawTop();
        handleInfectionCard(card, 3);

        // 3. Increase the intensity of infection:
        // Take the Infection Discard Pile, thoroughly shuffle it, then
        // place it on top of the remaining Infection Draw Pile. (Don’t
        // shuffle these cards into the Infection Draw Pile.)
        infectionDrawPile.increaseTheIntensityOfInfection();
    }

    /**
     * Because Pandemic is a test of cooperation and mettle (and not of memory), players may freely
     * examine the contents of the Player Discard Pile and the Infection Discard Pile at any time.
     *
     * @return (a copy of) of the cards that have been currently been discarded
     */
    public List<InfectionCard> getInfectionDiscardPile() {
        return Lists.newArrayList(infectionDrawPile.getDiscardedCards());
    }

    public List<PlayerCard> getPlayerDiscardPile() {
        return Lists.newArrayList(playerDrawPile.getDiscardedCards());
    }


    public boolean isMoveAllowed(CityId from, CityId destination, Optional<PlayerCard> cardOpt) {
        MoveService moveService = getMoveService();
        MoveType moveType = moveService.moveTypeFor(from, destination, cardOpt);
        return (moveType != MoveType.None);
    }

    public void move(MemberKey movedMemberKey, CityId destination, Optional<PlayerCard> cardOpt) {
        Member member = getIfCurrentPlayerAndEnoughActionOrFail(movedMemberKey);
        member.ensurePlayerCardIsPresent(cardOpt);
        MoveType moveType = computeMoveTypeOrFail(member, destination, cardOpt);

        member.moveTo(destination, moveType, cardOpt);
    }

    private MoveType computeMoveTypeOrFail(Member member, CityId destination, Optional<PlayerCard> cardOpt) {
        MoveService moveService = getMoveService();
        CityId cityFrom = member.location();

        MoveType moveType = moveService.moveTypeFor(cityFrom, destination, cardOpt);
        if (moveType == MoveType.None) {
            throw new InvalidMoveException(member.memberKey(), cityFrom, destination);
        }
        return moveType;
    }

    private MoveService getMoveService() {
        return new MoveService(cityStates, CityGraph.getInstance());
    }

    private Member getIfCurrentPlayerAndEnoughActionOrFail(MemberKey memberKey) {
        // fail if it is not player's turn...
        if (!memberKey.equals(this.currentMemberKey)) {
            throw new InvalidPlayerTurnException(currentMemberKey, memberKey);
        }

        Member member = team().findMember(memberKey).or(memberNotFoundException(memberKey));

        // ...or no more action point
        member.ensureActionIsAuthorized();
        return member;
    }

}


