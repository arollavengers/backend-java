package arollavengers.core.util.jackson;

import arollavengers.core.util.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.map.jsontype.NamedType;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JacksonAnnotationIntrospectorCustom extends JacksonAnnotationIntrospector {

    private Multimap<Class<?>, NamedType> subTypesPerClass = ArrayListMultimap.create();

    public JacksonAnnotationIntrospectorCustom registerSubTypes(Class<?> type, List<Class<?>> subtypes) {
        for(Class<?> subtype : subtypes) {
            subTypesPerClass.put(type, new NamedType(subtype));
        }
        return this;
    }

    @Override
    public List<NamedType> findSubtypes(Annotated a) {
        Class<?> rawType = a.getRawType();
        if (subTypesPerClass.containsKey(rawType)) {
            return Lists.newArrayList(subTypesPerClass.get(rawType));
        }
        return super.findSubtypes(a);
    }

    @Override
    public String findTypeName(AnnotatedClass ac) {
        if (subTypesPerClass.containsValue(new NamedType(ac.getRawType()))) {
            String simpleName = ac.getRawType().getSimpleName();
            simpleName = StringUtils.removeEndIgnoreCase(simpleName, "Event");
            return Strings.separate(simpleName, '-');
        }
        return super.findTypeName(ac);
    }

}
