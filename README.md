# Tray.io Challenge

Guide to running the code challenge built by Andrei Nae-Stroie. The application was written in Java using the Spring Boot framework. The application uses no database, it saves the objects created in memory. 

### Installation and Running

To install the application you will need the following installed: 

* Gradle
* Java

To install the server, change to working directory to the root of the project and run `./gradlew build`.
To start the server, run `./gradlew bootRun`. This will then start the spring boot application at `localhost:8080`.
To run the unit tests, run `./gradlew test`. 

### File Structure

`/src/main/java/trayio/controllers` - controllers for Workflow / Workflow Execution
`/src/main/java/trayio/models` -  models for Workflow / Workflow Execution
`/src/test/java/trayio/` - unit tests for Workflow / Workflow Execution controllers

### Endpoint breakdown

For the implementation of the required features the following endpoints have been created (all can be accessed via GET methods):

* `/create-workflow?steps={steps}` - takes an int parameter and creates a new workflow with unique ID and the given number of steps and returns a JSON with the new workflow or an error json 
* `/get-workflow?id={id}` - takes an int parameter and retrieves the JSON of the workflow with given id or an error json
* `/create-workflow-execution?workflow-id={workflow-id}` - takes an int parameter and creates a new workflow execution linked to the workflow with specified ID or an error if the workflow does not exist / the parameter is not int
* `/get-workflow-execution?id={id}` - takes an int parameter and retrieves the JSON of the workflow execution with given id or an error json
* `/increment-workflow-execution-step?id={id}` - takes an int parameter and increments the current step value of the workflow execution with id equal to the parameter and returns the JSON of the workflow execution or returns an error if the workflow execution does not exist or returns a warning if the workflow execution has already reached the maximum number of steps
* `/reset` - used for unit testing, I'm unable to mock a database so I've created this method to reset my "models"

### Model explanation

I've represented `Workflow` and `Workflow execution` as two entities with a one to many relationship, one `Workflow` can have many `Workflow executions` and a `Workflow execution` has only one `Workflow`.

### Manual testing example

An example to test the functionality can be achieved like this (in this example I use the browser because the requests are GET , alternatively tools such as `curl` can be used).

* Open a browser and access `http://localhost:8080/create-workflow?steps=9`, `http://localhost:8080/create-workflow?steps=3`, `http://localhost:8080/create-workflow?steps=10`. This has now created and saved three workflows.
* Access then `http://localhost:8080/create-workflow-execution?workflow-id=1`, `http://localhost:8080/create-workflow-execution?workflow-id=2`, `http://localhost:8080/create-workflow-execution?workflow-id=2`. This has now created and saved 3 workflow executions, first linked with the workflow with id 1, the last two linked with the workflow with id 2.
* To increment the step, access `http://localhost:8080/increment-workflow-execution-step?id=2`. This should return the updated workflow execution. Running this command 3 more times should then output a warn, signalling that the workflow execution reached its final step.
* Try creatng workflow executions with invalid ids
* Try specifying different parameter types




