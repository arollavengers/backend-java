package arollavengers.core.pattern;

import arollavengers.core.pattern.annotation.ValueObject;

import java.lang.reflect.Field;

/**
 * A domain event can only be constituted of immutable types.
 *
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class DomainEventRule {

  public void checkClass(Class<?> klazz) {
    for(Field field : klazz.getFields()) {
      checkField(field);
    }
  }

  private void checkField(Field field) {
    Class<?> type = field.getType();
    if(type.isPrimitive()
            || type.equals(String.class)
            || type.equals(Long.class)
            || type.equals(Integer.class)
            || type.equals(Short.class)
            || type.equals(Byte.class)
            || type.equals(Double.class)
            || type.equals(Float.class)
            || type.equals(Boolean.class)
            || type.equals(Character.class)
            || type.isAnnotationPresent(ValueObject.class)
            )
      return;
    throw new PatternViolationException();
  }
}
