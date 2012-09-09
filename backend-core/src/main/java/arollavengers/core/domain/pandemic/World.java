package arollavengers.core.domain.pandemic;

import arollavengers.core.domain.user.User;
import arollavengers.core.domain.user.UserRepository;
import arollavengers.core.events.pandemic.*;
import arollavengers.core.exceptions.EntityIdAlreadyAssignedException;
import arollavengers.core.exceptions.pandemic.NoDiseaseToCureException;
import arollavengers.core.exceptions.pandemic.WorldNotYetCreatedException;
import arollavengers.core.exceptions.pandemic.WorldNumberOfRoleLimitReachedException;
import arollavengers.core.exceptions.pandemic.WorldRoleAlreadyChosenException;
import arollavengers.core.infrastructure.*;
import arollavengers.core.infrastructure.annotation.OnEvent;
import com.google.common.base.Optional;

import java.util.HashSet;
import java.util.Set;

public class World extends AggregateRoot<WorldEvent> {
    public static final int MAX_ROLES = 4;
    public static final int MIN_ROLES = 2;
    private final UnitOfWork uow;
    private final EventHandler<WorldEvent> eventHandler;

    // ~~~ World state
    private Id ownerId;

    private Difficulty difficulty;

    private MemberStates memberStates;

    private Team team;

    private CityStates cityStates;

    private Set<Disease> eradicatedDiseases;
    private Set<Disease> curedDiseases;

    private boolean started;

    public World(UnitOfWork uow) {
        this.uow = uow;
        this.eventHandler = new AnnotationBasedEventHandler<WorldEvent>(this);
    }

    /**
     * Create a new world of given id and difficulty owned by a user
     *
     * @param newId      Id of the world
     * @param owner      Owner of this world
     * @param difficulty Difficulty of the game
     */
    public void createWorld(Id newId, User owner, Difficulty difficulty) {
        if (!aggregateId().isUndefined()) {
            throw new EntityIdAlreadyAssignedException(aggregateId(), newId);
        }
        final WorldCreatedEvent worldCreatedEvent = new WorldCreatedEvent(newId, owner.aggregateId(), difficulty);
        applyNewEvent(worldCreatedEvent);
    }

    @OnEvent
    private void doCreateWorld(WorldCreatedEvent event) {
        assignId(event.aggregateId());
        this.difficulty = event.difficulty();
        this.ownerId = event.ownerId();
        this.memberStates = new MemberStates();
        this.cityStates = new CityStates();
        this.eradicatedDiseases = new HashSet<Disease>();
        this.curedDiseases = new HashSet<Disease>();
        this.team = new Team();
    }

    /**
     * Register a new user with the given role in the game.
     *
     * @param user user that wants to join the game
     * @param role role of the user in the game
     */
    public void registerMember(User user, MemberRole role) {
        ensureWorldIsCreated();

        if (team().roles().size() >= MAX_ROLES) {
            throw new WorldNumberOfRoleLimitReachedException(role);
        }

        if (team().hasRole(role)) {
            throw new WorldRoleAlreadyChosenException(aggregateId(), role);
        }

        final Optional<Member> member = team().findMember(user.aggregateId());
        if (member.isPresent()) {
            throw new WorldRoleAlreadyChosenException(aggregateId(), member.get().role());
        }

        applyNewEvent(new WorldMemberJoinedTeamEvent(aggregateId(), user.aggregateId(), role));
    }

    @OnEvent
    private void doEnrole(final WorldMemberJoinedTeamEvent event) {
        team.enrole(new Member(event.newComerId(), event.role()));
    }

    /**
     * Start the game
     */
    public void startGame() {
        throw new WorldNotYetCreatedException();
    }

    /**
     * Treat a city by a member of team removing one cube of given disease
     *
     * @param member  Action doer
     * @param city    City to treat
     * @param disease Disease to treat
     */
    public void treatCity(Member member, CityId city, Disease disease) {
        MemberState memberState = memberStates.getStateOf(member);
        // fail if it is not player's turn or no more action point
        memberState.ensureActionIsAuthorized();

        CityState cityState = cityStates.getStateOf(city);
        int cityDiseaseCubes = cityState.numberOfCubes(disease);
        if (cityDiseaseCubes == 0) {
            throw new NoDiseaseToCureException(aggregateId(), city, disease);
        }

        applyNewEvent(new WorldMemberActionSpentEvent(aggregateId(), member.userId()));

        int nbCubeTreated;
        if (hasCureFor(disease) || member.role() == MemberRole.Medic) {
            nbCubeTreated = cityDiseaseCubes;
            applyNewEvent(new WorldCityCuredEvent(aggregateId(),
                member.userId(),
                city,
                disease));
        } else {
            nbCubeTreated = 1;
            applyNewEvent(new WorldCityTreatedEvent(aggregateId(),
                member.userId(),
                city,
                disease));
        }

        // check for eradication
        int worldDiseaseCubes = cityStates.numberOfCubes(disease);
        if (worldDiseaseCubes == nbCubeTreated) {
            applyNewEvent(new WorldDiseaseEradicatedEvent(aggregateId(),
                member.userId(),
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
     * @return the world's owner id.
     */
    public Id ownerId() {
        return ownerId;
    }

    /**
     * @param repository Repository used to retrieve the user
     * @return the world's owner.
     * @see #ownerId()
     */
    User owner(UserRepository repository) {
        return repository.getUser(uow, ownerId);
    }

    boolean hasCureFor(final Disease disease) {
        return curedDiseases.contains(disease);
    }

    boolean hasBeenEradicated(final Disease disease) {
        return eradicatedDiseases.contains(disease);
    }

    Difficulty difficulty() {
        return difficulty;
    }

    /**
     * Team is mutable and thus should not be accessible from outside and kept private.
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
        if (aggregateId().isUndefined()) {
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
}


