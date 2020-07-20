# Appointment Publishing

A Java Spring Boot + H2 Database backend application for publishing appointments.

Originally based on the tutorial [*How Spring Boot Can Level Up your Spring Application*](https://stackify.com/spring-boot-level-up/).


## Components

As database,
it uses a H2 in-memory database configured by **`src.main.java.com.appointment.publishing.config.PersistenceConfig`**.


### src.main.java.com.appointment.publishing.controller.GlobalExceptionHandler

Is a centralized/single place to handle all custom exceptions handling for this application.


### src.main.java.com.appointment.publishing.model

Those classes are used to infer how the SQL database tables and columns are created
for each object type.


### src.main.java.com.appointment.publishing.config

Hold all custom configuration classes for this application.
They are automatically loaded.


### src.main.java.com.appointment.publishing.repository

Central repository marker interfaces.
Captures the domain type to manage as well as the domain type's id.
Domain repositories extending this interface can selectively expose
CRUD methods by simply declaring methods of the same signature as those declared in CrudRepository.


### src.main.resources.application.properties

Controls the debug and runtime information about the system.
Use it to debug SQL database and access an SQL command line.


## Usage

To run this project, you need installed:
1. **`javac 1.8.0_131`** or superior
1. **`Apache Maven 3.5.0`** or superior

Then, just call **`mvn package exec:java`**

Alternatively, you can install **`IntelliJ IDEA 2020`**,
go to the menu **`File -> New -> Project from Existing Sources`** and select the **`pom.xml`** project file.

After starting the project, the API will be available on the URL: `http://127.0.0.1:8081/appointments/<API_NAME>`


### Restful API

1. **`clipping`** is a *POST* and *GET* endpoint. See **`com.appointment.publishing.controller.ClippingController`** and **`com.appointment.publishing.model.Clipping`** for the parameter details.


## Contributing

See [*CONTRIBUTORS*](CONTRIBUTORS).

The project follows the [*Google Java Style Guide*](https://google.github.io/styleguide/javaguide.html).
You [can configure](https://github.com/google/google-java-format) your favorite editor to use it.

To simplify the history review of this [*README.md*](README.md) file,
the technique of [*semantic line break*](http://translate.google.com.br/translate?hl=en&sl=en&u=https://github.com/sembr/specification)
will be used.


## License

See [*CONTRIBUTORS#License Cause*](CONTRIBUTORS).
