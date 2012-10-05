package arollavengers.core.views.pandemic;

import arollavengers.core.events.pandemic.PlayerMovedEvent;
import arollavengers.core.events.pandemic.WorldMemberJoinedTeamEvent;
import arollavengers.core.infrastructure.AnnotationBasedEventHandler;
import arollavengers.core.infrastructure.Bus;
import arollavengers.core.infrastructure.DomainEvent;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.Message;
import arollavengers.core.infrastructure.VersionedDomainEvent;
import arollavengers.core.infrastructure.annotation.OnEvent;
import arollavengers.core.util.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Service
public class PlayerViewProjection {

    private Logger log = LoggerFactory.getLogger(PlayerViewProjection.class);

    @Inject
    private Bus bus;

    private Bus.Listener listener;

    @Inject
    private PlayerViewRepository repository;

    @Inject
    private ViewErrorHandler errorHandler;

    @PostConstruct
    public void busSubscription() {
        final AnnotationBasedEventHandler<DomainEvent> eventHandler =
                new AnnotationBasedEventHandler<DomainEvent>(this, false);

        listener = new Bus.Listener() {
            @Override
            public void onMessage(@Nonnull Message message) {
                if (message instanceof VersionedDomainEvent) {
                    VersionedDomainEvent versionedEvent = (VersionedDomainEvent) message;
                    eventHandler.handle(versionedEvent.event(), versionedEvent.aggregateId());
                }
            }
        };
        bus.subscribe(listener);
        log.info("Player projection initialized and listening Bus");
    }

    @PreDestroy
    public void tearDown() {
        bus.unsubscribe(listener);
    }

    @OnEvent
    private void on(final WorldMemberJoinedTeamEvent event) {
        PlayerView view = new PlayerView();
        view.setId(event.worldId(), event.memberId());
        repository.save(view);
    }

    @OnEvent
    private void on(final PlayerMovedEvent event, Id worldId) {
        PlayerView.PK pk = PlayerView.pk(worldId, event.entityId());
        repository.modifyOrNotFound(pk, errorHandler, new Function<PlayerView>() {
            @Override
            public void apply(PlayerView playerView) {
                playerView.setLocation(event.cityId());
            }
        });
    }
}
