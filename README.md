# Java API Server Template

A basic gradle project for a lightweight java api server development.

This is a template that demonstrate the usage of java's HttpServer to implement a lightweight API. The sample resource is a Person, where you can do CRUD operations. 

This template is implemented as java module so that we can package it independently. Hence, you just need to extract it and run it without the need to install the required java. The package generated is for windows only.

Since this is just a template, you can always change any the implementations that you can see here.

## Pre-requisite

* Java 17

## Cloning

1. **Create a fork** of this repository.

2. **Clone the forked repository** to your machine.

3. **Test your cloned repository** using the following command:

   ```
   gradlew test
   ```

   > If it completes successfully your setup is good.

## Packages

### commons

Contains all the common codes that can usually cross to any available packages.

### config

Must have the logic that involves configurations. 

Aside from the **AppConfig class** *(i.e. more about this in **the application.properties file section**.)*, it also contains the **PersonModule class**. This class is where we should normally wires the services and respositories implementations. 

The wiring is done through a **guice framework**.

### controller

The classes here must be responsible for handling the **resource endpoints**. 

In this template, you will find all the Person resource CRUD implementations. Hence, one of this classes will be hit whenever you try to access a valid endpoint.

### model

Must hold the **persistence model** of the application. 

In this template, a model of a person is being used. To avoid the boilerplate of implementing all the setter and getter methods, **lombok** is being used for this.

### repository

The classes here must be responsible for **managing the storage of the model**. Also, avoid the controller classes to access the classes here directly.

The only implementation in this template is making the person model stored into an in-memory list. 

### service

The classes here must hold the **business logic** and must be the implementations that have access to repository classes.

This template contains simple person services.

### wrapper

The classes here **wraps all the third-party classes**. So that, the other application classes knows only the wrappers and not the third-party classes. This is for **managing the third-party classes in one place**. This is beneficial if a third-party class has deprecated a method and recommends to use a replacement method. With wrapper, you only need to update the wrapper class and not all the invocations that spreads to different classes.

## Application class

The **Application class** is the bootstrap of the server. By default, the server is listening on port **8080**. You can change this by updating it in the **application.properties** file. If you are planning to run the server using IDE. This is the class to be ran.

If you've packaged it and wants to run the server, execute the following batch file:

```
<APPLICATION_ROOT>\java-api-server.bat
```

> The <APPLICATION_ROOT> is the location where you've extracted the package. *See the [build document](BUILD.md) on how to package the project.*

If you ran the server, expect to see the following output:

```
The app started on port 8080
Press any key to stop...
```

To test it, while the server is running, **open a browser** and use the following address:

http://localhost:8080/person

Expect to see the following output:

```json
[{"id":1,"firstName":"Ronaldo","lastName":"Webb"},{"id":2,"firstName":"Juan","lastName":"Dela Cruz"}]
```

If you want to **stop the server**, just **press any key**.

## The application.properties file

The **application.properties file** holds the configurations specific to the application such as the following:

* server.port
* base.url

The application.properties file is located in the following directory:

```
<PROJECT_DIR>\conf
```

> The <PROJECT_DIR> is the location where you've cloned the repository.

The **class that reads this file** is the following:

```
AppConfig
```

If want to add more/update configuration settings you can **update this properties file** and don't forget to also update the class that reads it. 

In the **actual package**, you can find this file in the following directory:

```
<APPLICATION_ROOT>\conf
```

> The <APPLICATION_ROOT> is the location where you've extracted the package. *See the [build document](BUILD.md) on how to package the project.*

## The log4j2.xml file

The **log4j2.xml file** holds the logging configuration and it is located in the following location:

```
<PROJECT_DIR>\src\main\resources
```

> The <PROJECT_DIR> is the location where you've cloned the repository.

In the **actual package**, you can find this file in the following directory:

```
<APPLICATION_ROOT>\conf
```

> The <APPLICATION_ROOT> is the location where you've extracted the package. *See the [build document](BUILD.md) on how to package the project.*

## Swagger Definition

Load the following swagger definition to https://editor.swagger.io/:

```
<PROJECT_DIR>\swagger\person-api.yaml
```

> The <PROJECT_DIR> is the location where you've cloned the repository.

Doing this show load the available person resource endpoints.

## Testing the Endpoints

### Creating a Person

**Request Data**

| Field  | Value                                           |
| ------ | ----------------------------------------------- |
| Method | POST                                            |
| Header | Content-Type: application/json                  |
| URL    | http://localhost:8080/person                    |
| Body   | {"firstName": "Andrea","lastName": "Rodrigues"} |

*See the details of the URL pattern from the swagger definition.*

**Response Data**

| Field  | Value                                                |
| ------ | ---------------------------------------------------- |
| Status | 200                                                  |
| Header | Content-Type: application/json                       |
| Body   | {"id":3,"firstName":"Andrea","lastName":"Rodrigues"} |

> Notice that in the request data, the ID field is not provided. However in the generated response data, the generated ID for the person is also returned. 

> The ID 3, will only be returned if you've run the request data just after starting the server.

### Retrieving all the Persons

| Field  | Value                        |
| ------ | ---------------------------- |
| Method | GET                          |
| URL    | http://localhost:8080/person |

*See the details of the URL pattern from the swagger definition.*

**Response Data**

| Field  | Value                                                        |
| ------ | ------------------------------------------------------------ |
| Status | 200                                                          |
| Header | Content-Type: application/json                               |
| Body   | [{"id":1,"firstName":"Ronaldo","lastName":"Webb"},{"id":2,"firstName":"Juan","lastName":"Dela Cruz"},{"id":3,"firstName":"Andrea","lastName":"Rodrigues"}] |

### Retrieving a Person by Id

Retrieving the the person with the ID 1.

**Request Data**

| Field  | Value                          |
| ------ | ------------------------------ |
| Method | GET                            |
| URL    | http://localhost:8080/person/1 |

*See the details of the URL pattern from the swagger definition.*

**Response Data**

| Field  | Value                                            |
| ------ | ------------------------------------------------ |
| Status | 200                                              |
| Header | Content-Type: application/json                   |
| Body   | {"id":1,"firstName":"Ronaldo","lastName":"Webb"} |

### Updating a Person

Updating the the person with the ID 3.

**Request Data**

| Field  | Value                                               |
| ------ | --------------------------------------------------- |
| Method | PUT                                                 |
| URL    | http://localhost:8080/person                        |
| Body   | {"id":3,"firstName":"Andrea","lastName":"Guevarra"} |

*See the details of the URL pattern from the swagger definition.*

**Response Data**

| Field  | Value                                               |
| ------ | --------------------------------------------------- |
| Status | 200                                                 |
| Header | Content-Type: application/json                      |
| Body   | {"id":3,"firstName":"Andrea","lastName":"Guevarra"} |

### Deleting a Person

Deleting the the person with the ID 3.

**Request Data**

| Field  | Value                          |
| ------ | ------------------------------ |
| Method | DELETE                         |
| URL    | http://localhost:8080/person/3 |

*See the details of the URL pattern from the swagger definition.*

**Response Data**

| Field  | Value |
| ------ | ----- |
| Status | 200   |

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## [Build](BUILD.md)

## [Changelog](CHANGELOG.md)

## Author

* Ronaldo Webb
