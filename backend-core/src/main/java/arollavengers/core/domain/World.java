package arollavengers.core.domain;

import arollavengers.core.events.*;
import arollavengers.core.exceptions.*;
import arollavengers.core.infrastructure.AggregateRoot;
import arollavengers.core.infrastructure.EventHandler;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.UnitOfWork;

import java.util.HashSet;
import java.util.Set;

public class World extends AggregateRoot<WorldEvent> {
  public static final int MAX_ROLES = 4;
  private final UnitOfWork uow;
  private final WorldEventHandler eventHandler;

  // ~~~ World state
  private UserId ownerId;

  private Difficulty difficulty;

  private MemberStates memberStates;

  private Team team;

  private CityStates cityStates;

  private Set<Disease> eradicatedDiseases;
  private Set<Disease> curedDiseases;

  public World(UnitOfWork uow) {
    this.uow = uow;
    this.eventHandler = new IntrospectionBasedEventHandler(this);
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
    final WorldCreatedEvent worldCreatedEvent = new WorldCreatedEvent(newId, owner.userId(), difficulty);
    applyNewEvent(worldCreatedEvent);
  }

  public void registerRole(Member newComer) {
    if (aggregateId().isUndefined()) {
      throw new WorldNotYetCreatedException();
    }

    if (team().roles().size() >= MAX_ROLES) {
      throw new WorldNumberOfRoleLimitReachedException(newComer.role());
    }

    if (team.hasRole(newComer.role())) {
      throw new WorldRoleAlreadyChosenException(aggregateId(), newComer.role());
    }

    applyNewEvent(new WorldMemberJoinedTeamEvent(aggregateId(), newComer));


  }


  void doCreateWorld(WorldCreatedEvent event) {
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

    applyNewEvent(new WorldMemberActionSpentEvent(aggregateId(), member.id()));

    int nbCubeTreated;
    if (hasCureFor(disease) || member.role() == MemberRole.Medic) {
      nbCubeTreated = cityDiseaseCubes;
      applyNewEvent(new WorldCityCuredEvent(aggregateId(),
          member.id(),
          city,
          disease));
    } else {
      nbCubeTreated = 1;
      applyNewEvent(new WorldCityTreatedEvent(aggregateId(),
          member.id(),
          city,
          disease));
    }

    // check for eradication
    int worldDiseaseCubes = cityStates.numberOfCubes(disease);
    if (worldDiseaseCubes == nbCubeTreated) {
      applyNewEvent(new WorldDiseaseEradicatedEvent(aggregateId(),
          member.id(),
          disease));
    }
  }

  UserId ownedBy() {
    return ownerId;
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

  Team team() {
    return team;
  }


  private void doTreatCity(WorldCityTreatedEvent event) {
    CityState cityState = cityStates.getStateOf(event.city());
    cityState.removeOneCube(event.disease());
  }

  private void doCureCity(WorldCityCuredEvent event) {
    CityState cityState = cityStates.getStateOf(event.city());
    cityState.removeAllCubes(event.disease());
  }

  private void doMarkDiseaseEradicated(WorldDiseaseEradicatedEvent event) {
    eradicatedDiseases.add(event.disease());
  }


  protected UnitOfWork unitOfWork() {
    return uow;
  }

  protected EventHandler<WorldEvent> internalEventHandler() {
    return eventHandler;
  }

  public void doEnrole(final Member member) {
    team.enrole(member);
  }
}


