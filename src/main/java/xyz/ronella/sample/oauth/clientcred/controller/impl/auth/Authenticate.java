package xyz.ronella.sample.oauth.clientcred.controller.impl.auth;

import org.slf4j.LoggerFactory;
import xyz.ronella.logging.LoggerPlus;
import xyz.ronella.sample.oauth.clientcred.commons.ResponseStatus;
import xyz.ronella.sample.oauth.clientcred.config.AppConfig;
import xyz.ronella.sample.oauth.clientcred.wrapper.SimpleHttpExchange;

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

    private static final LoggerPlus LOGGER_PLUS = new LoggerPlus(LoggerFactory.getLogger(Authenticate.class));

    private String getBasicAuth() {
        final var appConfig = AppConfig.INSTANCE;
        final var clientId = appConfig.getClientId();
        final var clientSecret = appConfig.getClientSecret();
        return Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
    }

    private HttpRequest createHTTPRequest() {
        final String tokenEndpoint = getAuthService().getTokenEndpoint(AppConfig.INSTANCE.getAuthIssuer());

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
    public void process(final SimpleHttpExchange simpleExchange) {
        try (final var mLOG = LOGGER_PLUS.groupLog("void process(SimpleHttpExchange simpleExchange)")) {

            try {
                final var request = createHTTPRequest();
                final var client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
                final var response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == ResponseStatus.OK.getCode()) {
                    simpleExchange.sendJsonResponse(response.body());
                }
            } catch (Exception exception) {
                mLOG.error(LOGGER_PLUS.getStackTraceAsString(exception));
                simpleExchange.sendResponseCode(ResponseStatus.SERVICE_UNAVAILABLE);
                throw new RuntimeException(exception);
            }
        }
    }

    @Override
    public String getPathPattern() {
        return String.format("^%s/auth", getBaseURL());
    }
}
