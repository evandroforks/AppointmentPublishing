# Appointment Publishing

A Java Spring Boot + H2 Database backend application for publishing appointments.

Originally based on the tutorial [*How Spring Boot Can Level Up your Spring Application*](https://stackify.com/spring-boot-level-up/).


## Components

As database,
it uses a H2 in-memory database configured by **`src.main.java.com.appointment.publishing.config.PersistenceConfig`**.
The directory structure used follows the usual conventions as noted on
[*What is the recommended project structure for spring boot rest projects?*](https://stackoverflow.com/questions/40902280/).


### src.main.java.com.appointment.publishing.controller.GlobalExceptionHandler

Is a centralized/single place to handle all custom exceptions handling for this application.


### src.main.java.com.appointment.publishing.model

Those classes are used to infer how the SQL database tables and columns are created
for each object type.


### src.main.java.com.appointment.publishing.config

Hold all custom configuration classes for this application.
They are automatically loaded.


### src.main.java.com.appointment.publishing.repository

Central repository interfaces' maker.
Captures the domain type to manage as well as the domain type's id.
Domain repositories extending this interface can selectively expose
CRUD methods by simply declaring methods of the same signature as those declared in CrudRepository.


### src.main.java.com.appointment.publishing.service

Contains the
[*Service Components*](https://stackoverflow.com/questions/58234187/what-is-the-use-of-service-layer-in-spring-boot-applications)
which are used to write business logic in a different layer,
separated from **`@RestController`** class file.
For simplicity,
this repository chooses not to use intermediary Service interfaces.
See
[*Do I really need to create interfaces in Spring?*](https://stackoverflow.com/questions/55087578/) and
[*Spring boot autowiring an interface with multiple implementations*](https://stackoverflow.com/questions/51766013/).


### src.main.resources.application.properties

Controls the debug and runtime information about the system.
Use it to debug SQL database and access an SQL command line.


### src.test.java.com.appointment.publishing.test.ClippingControllerTest

Uncomment **`.alwaysDo(MockMvcResultHandlers.print())`** to debug the **`mockMvc`** tests,
forcing it print most things to the console when running the tests.
See also [*Spring MVC controller Test - print the result JSON String*](https://stackoverflow.com/questions/21495296/).


## Usage

To run this project, you need installed:
1. **`javac 1.8.0_131`** or superior
1. **`Apache Maven 3.5.0`** or superior

Then, just call **`mvn package exec:java`**

Alternatively, you can install **`IntelliJ IDEA 2020`**,
go to the menu **`File -> New -> Project from Existing Sources`** and select the **`pom.xml`** project file.

After starting the project, the API will be available on the URL: `http://127.0.0.1:8081/appointments/<API_NAME>`


### Restful API

1. **`/clipping`** is Restful endpoint. See **`com.appointment.publishing.controller.ClippingController`** and **`com.appointment.publishing.model.Clipping`** for the parameter details.

1. **`/notification`** is a Restful endpoint. See **`com.appointment.publishing.controller.NotificationController`** and **`com.appointment.publishing.model.Notification`** for the parameter details.

1. **`/appointment`** is a Restful endpoint. See **`com.appointment.publishing.controller.AppointmentController`** and **`com.appointment.publishing.model.Appointment`** for the parameter details.


## Contributing

See [*CONTRIBUTORS*](CONTRIBUTORS).

The project follows the [*Google Java Style Guide*](https://google.github.io/styleguide/javaguide.html).
You [*can configure*](https://github.com/google/google-java-format) your favorite editor to use it.

To simplify the history review of this [*README.md*](README.md) file,
the technique of [*semantic line break*](https://github.com/sembr/specification)
will be used.


### TODO

1. Create a test and ensure that when Jackson fails to parse an input value as **`classifiedTime: "10][:00"`**,
   the server returns `BAD_REQUEST` and a body explaining the problem.
   To implement this,
   just continue the implementation on **`src.main.java.com.appointment.publishing.config.JacksonUnknownPropertyConfig`**
   adding the corresponding handler for bad/invalid parsing.
1. See also the [*issue tracker*](/../../issues).


## License

See [*CONTRIBUTORS#License Cause*](CONTRIBUTORS).
