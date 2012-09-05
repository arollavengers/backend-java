package arollavengers.behavior.user;

import java.net.URL;
import java.util.List;

import org.jbehave.core.io.StoryFinder;
import org.springframework.context.ApplicationContext;

import arollavengers.behavior.infra.AbstractStory;
import arollavengers.behavior.infra.SpringContextBuilder;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;

public class UserStoriesTest extends AbstractStory {

    @Override
    protected List<String> storyPaths() {
        URL searchInURL = codeLocationFromClass(AbstractStory.class);
        return new StoryFinder().findPaths(searchInURL, "arollavengers/behavior/user/*.story", "**/fail/*");
    }
    
    @Override
    protected ApplicationContext createSpringContext() {
        return new SpringContextBuilder().usingClasses(UserStepsSupport.class).get();
    }
}
