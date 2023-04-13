package xyz.ronella.sample.oauth.clientcred;

import org.junit.jupiter.api.*;
import xyz.ronella.sample.oauth.clientcred.commons.ContentType;
import xyz.ronella.sample.oauth.clientcred.commons.ResponseStatus;
import xyz.ronella.sample.oauth.clientcred.model.Person;
import xyz.ronella.sample.oauth.clientcred.wrapper.SimpleHttpServer;
import xyz.ronella.sample.oauth.clientcred.wrapper.SimpleJson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SimpleHttpServerTest {

    private static SimpleHttpServer server = null;

    @BeforeAll
    public static void startServer() throws IOException {
        server = SimpleHttpServer.createServer();
        server.start();
        System.out.println("Server started");
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
        System.out.println("Server stopped");
    }

    @Test
    @Order(10)
    public void retrieveAllPersons() throws IOException, InterruptedException {
        final var client = HttpClient.newHttpClient();
        final var request = HttpRequest.newBuilder(URI.create("http://localhost:8080/person"))
                .GET()
                .build();
        final var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("[{\"id\":1,\"firstName\":\"Ronaldo\",\"lastName\":\"Webb\"},{\"id\":2,\"firstName\":\"Juan\",\"lastName\":\"Dela Cruz\"}]", response.body());
    }

    @Test
    @Order(20)
    public void retrieveAPerson() throws IOException, InterruptedException {
        final var client = HttpClient.newHttpClient();
        final var request = HttpRequest.newBuilder(URI.create("http://localhost:8080/person/1"))
                .GET()
                .build();
        final var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("{\"id\":1,\"firstName\":\"Ronaldo\",\"lastName\":\"Webb\"}", response.body());
    }

    @Test
    @Order(30)
    public void createAPerson() throws IOException, InterruptedException {
        final var client = HttpClient.newHttpClient();
        final var request = HttpRequest.newBuilder(URI.create("http://localhost:8080/person"))
                .header("Content-Type", ContentType.APPLICATION_JSON.toString())
                .POST(HttpRequest.BodyPublishers.ofString("""
                    {
                        "firstName": "Test2",
                        "lastName": "Only"
                    }
                    """))
                .build();
        final var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        final var json = response.body();
        final var person = new SimpleJson<Person>().toObjectType(json, Person.class);
        assertEquals(3, person.getId());
    }

    @Test
    @Order(40)
    public void updateAPerson() throws IOException, InterruptedException {
        final var client = HttpClient.newHttpClient();
        final var request = HttpRequest.newBuilder(URI.create("http://localhost:8080/person"))
                .header("Content-Type", ContentType.APPLICATION_JSON.toString())
                .PUT(HttpRequest.BodyPublishers.ofString("""
                    {
                        "id": 3,
                        "firstName": "Test3",
                        "lastName": "Only"
                    }
                    """))
                .build();
        final var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        final var json = response.body();
        final var person = new SimpleJson<Person>().toObjectType(json, Person.class);
        assertEquals("Test3", person.getFirstName());
    }

    @Test
    @Order(50)
    public void deleteAPerson() throws IOException, InterruptedException {
        final var client = HttpClient.newHttpClient();
        final var request = HttpRequest.newBuilder(URI.create("http://localhost:8080/person/3"))
                .DELETE()
                .build();
        final var response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(ResponseStatus.OK.getCode(), response.statusCode());
    }

    @Test
    @Order(60)
    public void updateAPersonNonExisting() throws IOException, InterruptedException {
        final var client = HttpClient.newHttpClient();
        final var request = HttpRequest.newBuilder(URI.create("http://localhost:8080/person"))
                .header("Content-Type", ContentType.APPLICATION_JSON.toString())
                .PUT(HttpRequest.BodyPublishers.ofString("""
                    {
                        "id": 3,
                        "firstName": "Test3",
                        "lastName": "Only"
                    }
                    """))
                .build();
        final var response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(ResponseStatus.NOT_FOUND.getCode(), response.statusCode());
    }

    @Test
    @Order(70)
    public void deleteAPersonNonExisting() throws IOException, InterruptedException {
        final var client = HttpClient.newHttpClient();
        final var request = HttpRequest.newBuilder(URI.create("http://localhost:8080/person/3"))
                .DELETE()
                .build();
        final var response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(ResponseStatus.NOT_FOUND.getCode(), response.statusCode());
    }
}