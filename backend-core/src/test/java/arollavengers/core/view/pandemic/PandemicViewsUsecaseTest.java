package arollavengers.core.view.pandemic;

import static org.fest.assertions.api.Assertions.assertThat;

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.domain.pandemic.Disease;
import arollavengers.core.domain.pandemic.Member;
import arollavengers.core.domain.pandemic.MemberRole;
import arollavengers.core.domain.pandemic.MoveType;
import arollavengers.core.domain.pandemic.World;
import arollavengers.core.domain.user.User;
import arollavengers.core.events.pandemic.CityInfectedEvent;
import arollavengers.core.events.pandemic.GameStartedEvent;
import arollavengers.core.events.pandemic.PandemicEvent;
import arollavengers.core.events.pandemic.PlayerMovedEvent;
import arollavengers.core.events.pandemic.ResearchCenterBuiltEvent;
import arollavengers.core.events.pandemic.WorldMemberJoinedTeamEvent;
import arollavengers.core.infrastructure.Bus;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.Message;
import arollavengers.core.infrastructure.SimpleBus;
import arollavengers.core.infrastructure.VersionedDomainEvent;
import arollavengers.core.util.Function;
import arollavengers.core.util.spring.SpringContextBuilder;
import arollavengers.core.views.pandemic.CityView;
import arollavengers.core.views.pandemic.CityViewProjection;
import arollavengers.core.views.pandemic.CityViewRepository;
import arollavengers.core.views.pandemic.PlayerView;
import arollavengers.core.views.pandemic.PlayerViewProjection;
import arollavengers.core.views.pandemic.PlayerViewRepository;
import arollavengers.core.views.pandemic.ViewErrorHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import javax.inject.Inject;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PandemicViewsUsecaseTest {

    // initialized in @Before
    private AnnotationConfigApplicationContext applicationContext;

    @Inject
    private CityViewRepository cityViewRepository;

    @Inject
    private PlayerViewRepository playerViewRepository;

    @Inject
    private Bus bus;

    @Before
    public void setUp() {
        applicationContext = new SpringContextBuilder()
                .usingClasses(
                        CityViewProjection.class,
                        CityViewRepository.class,
                        PlayerViewProjection.class,
                        PlayerViewRepository.class,
                        SimpleBus.class,
                        ViewErrorHandler.class)
                .usingClasspathResources("spring/appContext-persistence-views.xml")
                .get();
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @After
    public void tearDown() {
        applicationContext.destroy();
    }

    @Test
    public void create_and_find_view() {
        Id worldId = Id.next(World.class);

        CityView view = new CityView();
        view.setId(worldId, CityId.Paris);
        view.setResearchCenter(true);

        cityViewRepository.save(view);

        CityView cityView = cityViewRepository.find(CityView.pk(worldId, CityId.Paris));
        assertThat(cityView).isNotNull();
        assertThat(cityView.getCityId()).isEqualTo(CityId.Paris);
        assertThat(cityView.getWorldId()).isEqualTo(worldId);
        assertThat(cityView.hasResearchCenter()).isTrue();
    }

    @Test
    public void city_views_are_initialized_on_game_start() {
        Id worldId = Id.next(World.class);

        CityView cityViewBefore = cityViewRepository.find(CityView.pk(worldId, CityId.Paris));
        assertThat(cityViewBefore).isNull();

        bus.publish(toVersioned(worldId, new GameStartedEvent(worldId)));

        CityView cityViewAfter = cityViewRepository.find(CityView.pk(worldId, CityId.Paris));
        assertThat(cityViewAfter).isNotNull();
        assertThat(cityViewAfter.hasResearchCenter()).isFalse();
        for (Disease disease : Disease.values()) {
            assertThat(cityViewAfter.getNbCubes(disease)).isEqualTo(0);
        }
    }

    @Test
    public void on_event__research_center__does_not_create_view() {
        Id worldId = Id.next(World.class);

        bus.publish(toVersioned(worldId, new ResearchCenterBuiltEvent(worldId, CityId.Paris)));

        CityView cityView = cityViewRepository.find(CityView.pk(worldId, CityId.Paris));
        assertThat(cityView).isNull();
    }

    @Test
    public void on_event__research_center() {
        Id worldId = Id.next(World.class);

        bus.publish(toVersioned(worldId, new GameStartedEvent(worldId)));
        bus.publish(toVersioned(worldId, new ResearchCenterBuiltEvent(worldId, CityId.Paris)));

        CityView cityView = cityViewRepository.find(CityView.pk(worldId, CityId.Paris));
        assertThat(cityView).isNotNull();
        assertThat(cityView.hasResearchCenter()).isTrue();
    }


    @Test
    public void change_on_event__multiple_events() {
        Id worldId = Id.next(World.class);
        Id memberId = Id.next(Member.class);
        Id userId = Id.next(User.class);

        bus.publish(toVersioned(worldId, new GameStartedEvent(worldId)));
        bus.publish(toVersioned(worldId, new ResearchCenterBuiltEvent(worldId, CityId.Paris)));
        bus.publish(toVersioned(worldId, new WorldMemberJoinedTeamEvent(worldId, memberId, userId, MemberRole.Medic)));

        PlayerView playerView = playerViewRepository.find(PlayerView.pk(worldId, memberId));
        assertThat(playerView).isNotNull();
        assertThat(playerView.getLocation()).isEqualTo(CityId.Atlanta);


        bus.publish(toVersioned(worldId, new PlayerMovedEvent(memberId, CityId.Paris, MoveType.Drive, null)));
        bus.publish(toVersioned(worldId, new CityInfectedEvent(worldId, CityId.London, Disease.Orange, 2)));

        playerView = playerViewRepository.find(PlayerView.pk(worldId, memberId));
        assertThat(playerView).isNotNull();
        assertThat(playerView.getLocation()).isEqualTo(CityId.Paris);
    }

    private static Message toVersioned(Id aggregateId, PandemicEvent event) {
        return new VersionedDomainEvent<PandemicEvent>(aggregateId, event);
    }

}
