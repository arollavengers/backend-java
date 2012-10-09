package arollavengers.core.views.pandemic;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */

import arollavengers.core.domain.pandemic.CityId;
import arollavengers.core.events.pandemic.CityInfectedEvent;
import arollavengers.core.events.pandemic.GameStartedEvent;
import arollavengers.core.events.pandemic.PlayerMovedEvent;
import arollavengers.core.events.pandemic.ResearchCenterBuiltEvent;
import arollavengers.core.infrastructure.AnnotationBasedEventHandler;
import arollavengers.core.infrastructure.Bus;
import arollavengers.core.infrastructure.DomainEvent;
import arollavengers.core.infrastructure.Id;
import arollavengers.core.infrastructure.Message;
import arollavengers.core.infrastructure.VersionedDomainEvent;
import arollavengers.core.infrastructure.annotation.OnEvent;
import arollavengers.core.util.Function;

import org.springframework.stereotype.Service;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

@Service
public class CityViewProjection {

    @Inject
    private Bus bus;

    private Bus.Listener listener;

    @Inject
    private CityViewRepository repository;

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
                    eventHandler.handle(versionedEvent.event());
                }
            }
        };
        bus.subscribe(listener);
    }

    @PreDestroy
    public void tearDown() {
        bus.unsubscribe(listener);
    }

    @OnEvent
    private void on(GameStartedEvent event) {
        Id worldId = event.entityId();
        for(CityId cityId : CityId.values()) {
            CityView view = new CityView();
            view.setId(worldId, cityId);
            repository.save(view);
        }
    }

    @OnEvent
    private void on(final CityInfectedEvent event) {
        CityView.PK pk = CityView.pk(event.worldId(), event.cityId());
        repository.modifyOrNotFound(pk, errorHandler, new Function<CityView>() {
            @Override
            public void apply(CityView cityView) {
                cityView.setNbCubes(event.disease(), event.nbCubes());
            }
        });
    }

    @OnEvent
    private void on(ResearchCenterBuiltEvent event) {
        CityView.PK pk = CityView.pk(event.entityId(), event.cityId());
        repository.modifyOrNotFound(pk, errorHandler, new Function<CityView>() {
            @Override
            public void apply(CityView cityView) {
                cityView.setResearchCenter(true);
            }
        });
    }
}
