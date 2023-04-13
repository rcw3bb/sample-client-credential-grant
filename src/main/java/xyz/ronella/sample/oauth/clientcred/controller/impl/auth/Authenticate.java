package xyz.ronella.sample.oauth.clientcred.controller.impl.auth;

import xyz.ronella.sample.oauth.clientcred.commons.ResponseStatus;
import xyz.ronella.sample.oauth.clientcred.config.AppConfig;
import xyz.ronella.sample.oauth.clientcred.wrapper.SimpleHttpExchange;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Authenticate extends AbstractAuthResource {

    private String getBasicAuth() {
        final var appConfig = AppConfig.INSTANCE;
        final var clientId = appConfig.getClientId();
        final var clientSecret = appConfig.getClientSecret();
        return Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
    }

    private HttpRequest createHTTPRequest() {
        final var tokenEndpoint = AppConfig.INSTANCE.getTokenURL();

        final var params = new HashMap<String, String>();
        params.put("grant_type", "client_credentials");

        final var requestBody = params.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(params.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        return HttpRequest.newBuilder()
                .uri(URI.create(tokenEndpoint))
                .header("Authorization", "Basic " + getBasicAuth())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

    @Override
    public void process(SimpleHttpExchange simpleExchange) {
        final var request = createHTTPRequest();
        final var client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

        try {
            final var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == ResponseStatus.OK.getCode()) {
                simpleExchange.sendJsonResponse(response.body());
            }
        } catch (IOException | InterruptedException exception) {
            simpleExchange.sendResponseText(ResponseStatus.BAD_REQUEST, "Error authorizing");
            throw new RuntimeException(exception);
        }
    }

    @Override
    public String getPathPattern() {
        return String.format("^%s/auth", getBaseURL());
    }
}
