package arollavengers.web.controller;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

import arollavengers.core.service.time.FrozenTimeService;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PingControllerTest {

    /**
     * Context spring utilisé par notre classe de test.
     * @see #initApplicationContext()
     */
    private static AnnotationConfigApplicationContext applicationContext;

    /**
     * On construit notre environement Spring de manière programmatique et spécialement pour notre classe
     * de test.
     *
     * @see BeforeClass
     */
    @BeforeClass
    public static void initApplicationContext() {
        applicationContext = new AnnotationConfigApplicationContext();

        // TODO: trouver l'équivalent de <mvc:annotation-driven /> de manière programmatique
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(applicationContext);
        xmlReader.loadBeanDefinitions(new ClassPathResource("spring/appContext-mvc-minimal.xml"));
        applicationContext.register(PingController.class);
        applicationContext.registerBeanDefinition("timeService", genericBeanDefinition(FrozenTimeService.class).getBeanDefinition());
        applicationContext.refresh();
    }

    @Autowired
    private FrozenTimeService frozenTimeService;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    //
    private MockHttpServletRequest httpRequest;
    private MockHttpServletResponse httpResponse;

    @Before
    public void setUp() {
        /*
           On s'auto-injecte nos propres dépendances à l'aide du context spring créé en BeforeClass
         */
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
        httpRequest = new MockHttpServletRequest();
        httpResponse = new MockHttpServletResponse();
    }

    @Test
    public void ping() throws Exception {
        // -- Given
        frozenTimeService.freezeTimeTo(17L);
        httpRequest.setMethod("GET");
        httpRequest.setRequestURI("/ping/Travis");

        // -- When: utilise la tuyauterie Spring pour récupérer le controlleur
        // adéquat (en se basant sur l'URI de la requête)

        HandlerExecutionChain handler = handlerMapping.getHandler(httpRequest);
        assertThat(handler).describedAs("Matching handler, check your request mapping").isNotNull();
        Object controller = handler.getHandler();
        ModelAndView modelAndView = handlerAdapter.handle(httpRequest, httpResponse, controller);
        assertThat(modelAndView).isNull();

        // -- Then: on vérifie le format Json et son contenu
        // le contentType peux aussi contenir des informations sur le charset => on utilise 'contains'
        assertThat(httpResponse.getContentType()).contains("application/json");
        assertThat(httpResponse.getCharacterEncoding()).isEqualToIgnoringCase("utf-8");

        JsonNode rootNode = parseJsonFromResponse(httpResponse);
        assertThat(rootNode).isNotNull();
        assertThat(rootNode.get("name").asText()).isEqualTo("Travis");
        assertThat(rootNode.get("timeMillis").asLong()).isEqualTo(17L);
    }

    /**
     * Méthode utilitaire récupérant le contenu de la réponse, et tente de le transformer
     * en arbre Json.
     */
    private static JsonNode parseJsonFromResponse(MockHttpServletResponse response)
            throws IOException
    {
        String srcJson = response.getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(srcJson, JsonNode.class);
    }
}
