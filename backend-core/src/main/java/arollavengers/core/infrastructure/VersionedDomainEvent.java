package arollavengers.core.infrastructure;

import arollavengers.pattern.annotation.CanBeInvokedOnlyOnce;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import javax.annotation.Nonnull;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@JsonPropertyOrder({"aggregateId", "version", "event"})
public class VersionedDomainEvent<E extends DomainEvent> implements Message {

    @JsonProperty
    private final Id aggregateId;

    @JsonProperty
    private long version;

    @JsonProperty
    private final E event;

    @JsonCreator
    public VersionedDomainEvent(@JsonProperty("aggregateId") @Nonnull Id aggregateId,
                                @JsonProperty("event") @Nonnull E event)
    {
        this.aggregateId = aggregateId;
        this.event = event;
    }

    @CanBeInvokedOnlyOnce
    public void assignVersion(long version) {
        this.version = version;
    }

    public long version() {
        return version;
    }


    public Id aggregateId() {
        return aggregateId;
    }

    public E event() {
        return event;
    }

    @Override
    public String toString() {
        return "VersionedDomainEvent{" +
                "aggregateId=" + aggregateId +
                ", event=" + event +
                '}';
    }

    public static <E extends DomainEvent> VersionedDomainEvent<E> create(Id aggregateId, E event) {
        return new VersionedDomainEvent<E>(aggregateId, event);
    }
}
