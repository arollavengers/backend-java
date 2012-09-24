package arollavengers.core.domain.pandemic;

import arollavengers.core.domain.user.User;
import arollavengers.core.domain.user.UserRepository;
import arollavengers.core.events.pandemic.GameStartedEvent;
import arollavengers.core.events.pandemic.PlayerCardDrawnFromPileEvent;
import arollavengers.core.events.pandemic.ResearchCenterBuiltEvent;
import arollavengers.core.events.pandemic.WorldCityCuredEvent;
import arollavengers.core.events.pandemic.WorldCityTreatedEvent;
import arollavengers.core.events.pandemic.WorldCreatedEvent;
import arollavengers.core.events.pandemic.WorldDiseaseEradicatedEvent;
import arollavengers.core.events.pandemic.WorldEvent;
import arollavengers.core.events.pandemic.WorldMemberActionSpentEvent;
import arollavengers.core.events.pandemic.WorldMemberJoinedTeamEvent;
import arollavengers.core.events.pandemic.WorldPlayerDrawPileCreatedEvent;
import arollavengers.core.exceptions.EntityAlreadyCreatedException;
import arollavengers.core.exceptions.pandemic.GameAlreadyStartedException;
import arollavengers.core.exceptions.pandemic.InvalidUserException;
import arollavengers.core.exceptions.pandemic.MemberNotFoundException;
import arollavengers.core.exceptions.pandemic.NoDiseaseToCureException;
import arollavengers.core.exceptions.pandemic.NotEnoughPlayerException;
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
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class World extends AggregateRoot<WorldEvent> {

    public static final int MAX_TEAM_SIZE = 4;
    public static final int MIN_TEAM_SIZE = 2;
    private final UnitOfWork uow;
    private final EventHandler<WorldEvent> eventHandler;

    // ~~~ World state
    private Id ownerId = Id.undefined();

    private Difficulty difficulty;

    private Team team;

    private CityStates cityStates;

    private Set<Disease> eradicatedDiseases;
    private Set<Disease> curesDiscovered;

    private boolean started;

    private PlayerDrawPile playerDrawPile;

    private int infectionRate = -1;

    private int outbreaks = -1;


    public World(Id worldId, UnitOfWork uow) {
        super(worldId);
        this.uow = uow;
        this.eventHandler = new AnnotationBasedEventHandler<WorldEvent>(this);
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
        this.infectionRate = 2;
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

        final Optional<Member> member = team().findMember(user.entityId());
        if (member.isPresent()) {
            throw new UserAlreadyRegisteredException(entityId(), user.entityId());
        }
        applyNewEvent(new WorldMemberJoinedTeamEvent(entityId(), Id.next(), user.entityId(), role));
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

    /**
     * Start the game. It does the following actions:
     * - start the game (nobody can register for playing anymore)
     * - initialize draw cards
     * - give initial hand to member
     */
    public void startGame() {

        ensureWorldIsCreated();
        ensureGameIsNotAlreadyStarted();

        final int teamSize = team().size();
        if (teamSize < MIN_TEAM_SIZE) {
            throw new NotEnoughPlayerException(teamSize, MIN_TEAM_SIZE);
        }

        applyNewEvent(new WorldPlayerDrawPileCreatedEvent(entityId(), Id.next()));
        playerDrawPile.initialize();

        int nbCardsPerPlayer = nbCardsPerPlayer(teamSize);
        for (Member member : team()) {
            for (int i = 0; i < nbCardsPerPlayer; i++) {
                applyNewEvent(new PlayerCardDrawnFromPileEvent(member.entityId(), playerDrawPile.drawTop()));
            }
        }
        playerDrawPile.completeForDifficulty(difficulty());
        applyNewEvent(new ResearchCenterBuiltEvent(entityId(), CityId.Atlanta));
        applyNewEvent(new GameStartedEvent(entityId()));
    }

    private int nbCardsPerPlayer(int teamSize) {
        return 6 - teamSize;
    }

    @OnEvent
    private void doCreateDrawPile(final WorldPlayerDrawPileCreatedEvent event) {
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
        //         applyNewEvent(new PlayerCardDrawnFromPileEvent(entityId(), member));
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
     * @param memberKey  Action doer
     * @param city    City to treat
     * @param disease Disease to treat
     */
    public void treatCity(MemberKey memberKey, CityId city, Disease disease) {
        Optional<Member> memberOpt = team().findMember(memberKey);
        if(!memberOpt.isPresent())
            throw new MemberNotFoundException(memberKey);

        Member member = memberOpt.get();

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

    /**
     * @param repository Repository used to retrieve the user
     * @return the world's owner. User returns will be automatically attached
     *         to the world's unit of work.
     * @see #ownerId()
     */
    User owner(UserRepository repository) {
        return repository.getUser(uow, ownerId);
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
    protected EventHandler<WorldEvent> internalEventHandler() {
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

    public int memberHandSize(@Nonnull MemberKey memberKey) {
        Optional<Member> memberOpt = team().findMember(memberKey);
        if(!memberOpt.isPresent())
            throw new MemberNotFoundException(memberKey);

        return memberOpt.get().handSize();
    }

    public int infectionRate() {
        return this.infectionRate;
    }

    public int outbreaks() {
        return this.outbreaks;
    }
}


