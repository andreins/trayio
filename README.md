# Tray.io Challenge

Guide to running the code challenge, built by Andrei Nae-Stroie. The application was written in Java using the Spring Boot framework. 

### Installation and Running

To install the application you will need the following installed: 

* Gradle
* Java

To install the server, change to working directory to the root of the project and run `./gradlew build`.
To start the server, run `./gradlew bootRun`. This will then start the spring boot application at `localhost:8080`.

### Endpoint breakdown

For the implementation of the required features the following endpoints have been created (all can be accessed via GET methods):

* `/create-workflow?steps={steps}` - this endpoint takes an int parameter and creates a new workflow with unique ID and the given number of steps and returns a JSON with the new workflow  
* `/get-workflow?id={id}` - this endpoint takes an int parameter and retrieves the JSON representation of the 
