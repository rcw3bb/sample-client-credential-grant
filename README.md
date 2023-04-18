# OAuth2 Client Credential Grant Sample Implementation

This is a sample implementation of client credential grant with java. The IDAM use is keycloak. 

## Pre-requisite

* Java 17

## [Keycloak Configuration](docs/keycloak-configuration.md)

**Keycloak** is the **IDAM** use in this sample implementation. If you don't have it configured yet do the procedure from [here](docs/keycloak-configuration.md).

## Classes of Interests

### Authenticate Class

The Authenticate class is controller class and is located in the following package:

```
xyz.ronella.sample.oauth.clientcred.controller.impl.auth
```

This controller is the one responsible for acquiring the access token. Do it you using the following endpoint:

http://localhost:9011/auth

> 9011 is the default port for this sample implementation.

> This will only work if the application.properties file was updated accordingly. See the following section for this.

### The AuthServiceImpl Classs

The AuthServiceImpl class is a service class the support the Authenticate class and is located in the following package:

```
xyz.ronella.sample.oauth.clientcred.service.impl
```

Some of its functions are:

* Retrieving the OIDC Configuration.
* Retrieving the JWKS URI.
* Retrieving the Claims.

## The application.properties file

The **application.properties file** holds the configurations specific to the application such as the following:

* server.port 

  This is the port to bind the API server. This has the default value of **9011**. 

  > All the reference related to API server port will be 9011. If the field was modified to another value. Use that new value to all the testing.

* client.id

  The registered client ID in IDAM. This has the default value of **clientcredential**.

* client.secret

  This is secret associated to client.id. Use the **Finding out the Client Secret section** in [Keycloak Configuration](docs/keycloak-configuration.md).

* auth.issuer

  The issuer of the authorization issuer. This has the default of **http://localhost:8080/realms/myrealm**.

* auth.audience

  Must be set if the audience is different from the client.id. In this sample implementation the audience is equals to client.id. Thus, no need to set this.

The application.properties file is located in the following directory:

```
<PROJECT_DIR>\conf
```

> The <PROJECT_DIR> is the location where you've cloned the repository.

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

Before testing always ensure the following:

* The keyclock server is running *(see the Pre-requisite section of [Keycloak Configuration](docs/keycloak-configuration.md))*.
* The client.secret in application.properties was updated accordingly.

### Retrieving Access Token

Do a GET request to the following endpoint:

http://localhost:9011/auth

This will return a JSON object with the following format:

```json
{
"access_token":<ACCESS_TOKEN>,
"expires_in":300,
"refresh_expires_in":0,
"token_type":"Bearer",
"not-before-policy":0,
"scope":"email clientcredential profile"
}
```

Use the **access_token field** as the bearer token to authorization header of all the API requests.

### Creating a Person

**Request Data**

| Field  | Value                                                        |
| ------ | ------------------------------------------------------------ |
| Method | POST                                                         |
| Header | Content-Type: application/json<br />Authorization: Bearer <ACCESS_TOKEN> |
| URL    | http://localhost:9011/person                                 |
| Body   | {"firstName": "Andrea","lastName": "Rodrigues"}              |

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

| Field  | Value                                |
| ------ | ------------------------------------ |
| Method | GET                                  |
| Header | Authorization: Bearer <ACCESS_TOKEN> |
| URL    | http://localhost:9011/person         |

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

| Field  | Value                                |
| ------ | ------------------------------------ |
| Method | GET                                  |
| Header | Authorization: Bearer <ACCESS_TOKEN> |
| URL    | http://localhost:9011/person/1       |

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
| Header | Authorization: Bearer <ACCESS_TOKEN>                |
| URL    | http://localhost:9011/person                        |
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

| Field  | Value                                |
| ------ | ------------------------------------ |
| Method | DELETE                               |
| Header | Authorization: Bearer <ACCESS_TOKEN> |
| URL    | http://localhost:9011/person/3       |

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
