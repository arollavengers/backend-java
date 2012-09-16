Narrative: 
In order to play a game
As a player
I want to be able to create and manage my account

Scenario: An unknown user cannot be logged

Meta:
@author Bob
@skip

Given i am the user with nickname: "weird"
When i try to login using the password "soweird"
Then i get an error message of type "Wrong Credentials"


Scenario: A known user cannot be logged using a wrong password

Given the following existing users:
| nickname | password |
|   Travis |   PacMan |
Given i am the user with nickname: "Travis"
When i try to login using the password "McCallum"
Then i get an error message of type "Wrong Credentials"


Scenario: A known user can be logged using the right password

Given the following existing users:
| nickname | password |
|   Travis |   PacMan |
Given i am the user with nickname: "Travis"
When i try to login using the password "PacMan"
Then i get logged
And a welcome message is displayed


Scenario: A user can create a new account

Given i'm on the login page
And the "create account" behavior is allowed
When i create a new account with the following data:
| nickname | password1 | password2 |      email       |
|   Travis |   PacMan  |   PacMan  | travis@subsp.ace |
Then i get logged
And a welcome message is displayed


Scenario: Email is required to create a new account

Given i'm on the login page
And the "create account" behavior is allowed
When i create a new account with the following data:
| nickname | password |      email       |
|   Travis |   PacMan |                  |
Then i get an error message of type "Email Missing"


Scenario: Two identical passwords input are required to create a new account

Given i'm on the login page
And the "create account" behavior is allowed
When i create a new account with the following data:
| nickname | password1 | password2 |      email       |
|   Travis |   PacMan  |   PocMan  | travis@subsp.ace |
Then i get an error message of type "Password Double-Check Failed"


Scenario: Nickname must be unique on account creation

Given the following existing users:
| nickname | password |
|   Travis |   PacMan |
Given i'm on the login page
And the "create account" behavior is allowed
When i create a new account with the following data:
| nickname | password1 | password2 |      email       |
|   Travis |   PucMan  |   PucMan  | travis@subsp.ace |
Then i get an error message of type "Nickname Already Used"

