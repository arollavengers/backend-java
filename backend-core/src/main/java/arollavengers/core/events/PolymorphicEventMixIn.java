package arollavengers.core.events;

import arollavengers.core.exceptions.pandemic.PandemicRuntimeException;
import arollavengers.core.infrastructure.DomainEvent;
import com.google.common.collect.Lists;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
public abstract class PolymorphicEventMixIn {

    public static List<Class<?>> collectAllEventClasses() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new TypeFilter() {
            public boolean match(MetadataReader reader, MetadataReaderFactory readerFactory) throws IOException {
                ClassMetadata classMetadata = reader.getClassMetadata();
                return classMetadata.isConcrete() && extendsInHierarchy(reader, readerFactory);
            }

            private boolean extendsInHierarchy(MetadataReader reader, MetadataReaderFactory readerFactory)
                    throws IOException
            {
                ClassMetadata classMetadata = reader.getClassMetadata();
                for (String intf : classMetadata.getInterfaceNames()) {
                    if (intf.equals(DomainEvent.class.getName())) {
                        return true;
                    }

                    MetadataReader r = readerFactory.getMetadataReader(intf);
                    if (extendsInHierarchy(r, readerFactory)) {
                        return true;
                    }
                }

                String superClassName = classMetadata.getSuperClassName();
                if (superClassName != null) {
                    MetadataReader r = readerFactory.getMetadataReader(superClassName);
                    return extendsInHierarchy(r, readerFactory);
                }
                return false;
            }
        });

        String basePackage = PolymorphicEventMixIn.class.getPackage().getName();
        //
        List<Class<?>> founds = Lists.newArrayList();
        for (BeanDefinition def : scanner.findCandidateComponents(basePackage)) {
            try {
                founds.add(Class.forName(def.getBeanClassName()));
            }
            catch (ClassNotFoundException e) {
                throw new PandemicRuntimeException(e);
            }
        }
        return founds;
    }
}
