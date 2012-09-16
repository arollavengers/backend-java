package arollavengers.behavior.infra;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class SpringContextBuilder {
    
    private AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    
    public SpringContextBuilder usingResources(Resource...resources) {
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(applicationContext);
        xmlReader.loadBeanDefinitions(resources);
        return this;
    }
    
    public SpringContextBuilder usingClasspathResources(String...classPathResources) {
        ClassPathResource[] resources = new ClassPathResource[classPathResources.length];
        for(int i=0;i<classPathResources.length;i++) {
            resources[i] = new ClassPathResource(classPathResources[i]);
        }
        return usingResources(resources);
    }
    
    public SpringContextBuilder scanningBasePackages(String...basePackages) {
        applicationContext.scan(basePackages);
        return this;
    }
    
    public SpringContextBuilder usingClasses(Class<?>...annotatedClasses) {
        applicationContext.register(annotatedClasses);
        return this;
    }
    
    public AnnotationConfigApplicationContext get() {
        applicationContext.refresh();
        return applicationContext;
    }
}
