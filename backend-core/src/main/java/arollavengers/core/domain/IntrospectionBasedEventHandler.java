package arollavengers.core.domain;

import arollavengers.core.events.WorldCreatedEvent;
import arollavengers.core.events.WorldEvent;

public class IntrospectionBasedEventHandler implements WorldEventHandler {
  private final World world;

  /**
   * @param world The aggregate root instance this handler will work on
   */
  public IntrospectionBasedEventHandler(final World world) {
    this.world = world;
  }

  public void handle(final WorldEvent event) {
    if (event.getClass() == WorldCreatedEvent.class) {
      handle((WorldCreatedEvent) event);
      return;
    }

    throw new IllegalArgumentException("Unknow event type " + event.getClass());

  }

  private void handle(final WorldCreatedEvent event) {
    world.doCreateWorld(event);
  }
}
