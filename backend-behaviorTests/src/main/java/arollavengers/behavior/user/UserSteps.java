package arollavengers.behavior.user;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;

import arollavengers.behavior.infra.StepsDefinition;

@StepsDefinition
public interface UserSteps {

    @Given("i am the user with nickname: \"$nickname\"")
    void defineCurrentNickname(String nickname);
    
    @When("i try to login using the password \"$password\"")
    void login(String password);
    
    @Then("i get an error message of type \"$errorCode\"")
    void assertErrorMessage(String errorCode);

    @Given("the following existing users: $users")
    void defineUsers(ExamplesTable users);
    
    @Then("i get logged")
    void assertUserIsLogged();
    
    @Then("a welcome message is displayed")
    void assertWelcomeMessageIsDisplayed();
    
    @Given("i'm on the $pageId page")
    void defineCurrentPage(String pageId);
    
    @Given("the \"$featureName\" behavior is allowed")
    void activateFeature(String featureName);
    
    @When("i create a new account with the following data: $userData")
    void createUser(ExamplesTable userData);

}
