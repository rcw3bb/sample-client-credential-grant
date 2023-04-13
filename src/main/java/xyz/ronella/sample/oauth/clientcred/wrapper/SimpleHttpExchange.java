package xyz.ronella.sample.oauth.clientcred.wrapper;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import org.slf4j.LoggerFactory;
import xyz.ronella.logging.LoggerPlus;
import xyz.ronella.sample.oauth.clientcred.commons.ContentType;
import xyz.ronella.sample.oauth.clientcred.commons.Method;
import xyz.ronella.sample.oauth.clientcred.commons.ResponseStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The only class that has the access to HttpExchange.
 * @author Ron Webb
 * @since 1.0.0
 */
public class SimpleHttpExchange {

    private static final LoggerPlus LOGGER_PLUS = new LoggerPlus(LoggerFactory.getLogger(SimpleHttpExchange.class));
    private final HttpExchange exchange;

    /**
     * Creates an instance of SimpleHttpExchange.
     * @param exchange An instance of HttpExchange.
     */
    public SimpleHttpExchange(final HttpExchange exchange) {
        this.exchange = exchange;
    }

    /**
     * The request uri.
     * @return An instance of URI.
     */
    protected URI getRequestURI() {
        return exchange.getRequestURI();
    }

    /**
     * The request path.
     * @return The request path.
     */
    public String getRequestPath() {
        return getRequestURI().getPath();
    }

    /**
     * The method of the request.
     * @return The request method.
     */
    public Optional<Method> getRequestMethod() {
        return Method.of(exchange.getRequestMethod());
    }

    /**
     * The query of the request.
     * @return The request query.
     */
    public String getRequestQuery() {
        return getRequestURI().getQuery();
    }

    /**
     * The content type of the request.
     * @return The content type.
     */
    public Optional<ContentType> getRequestContentType() {
        return ContentType.of(exchange.getRequestHeaders().getFirst("Content-type"));
    }

    /**
     * Sends a text response.
     * @param responseStatus The response status.
     * @param responseText The response text.
     */
    public void sendResponseText(final ResponseStatus responseStatus, final String responseText) {
        try(var mLOG = LOGGER_PLUS.groupLog("void sendResponseText(int,String)")) {
            mLOG.debug(() -> String.format("Response Code: %s; Response Text: %s", responseStatus, responseText));
            try {
                final var responseBytes = responseText.getBytes();
                final var contentLength = responseBytes.length;
                final var responseCode = responseStatus.getCode();

                exchange.sendResponseHeaders(responseCode, contentLength);

                if (ResponseStatus.NO_CONTENT.getCode() != responseCode) {
                    try (final var os = exchange.getResponseBody()) {
                        os.write(responseBytes);
                    }
                }

            } catch (IOException ioe) {
                mLOG.error(LOGGER_PLUS.getStackTraceAsString(ioe));
                throw new RuntimeException(ioe);
            }
        }
    }

    /**
     * Sends a response code only.
     * @param responseStatus The response status.
     */
    public void sendResponseCode(final ResponseStatus responseStatus) {
        sendResponseText(responseStatus, "");
    }

    /**
     * Sends a json response. The response content type will be application/json.
     * @param jsonResponse The json response text.
     */
    public void sendJsonResponse(String jsonResponse) {
        setResponseContentType(ContentType.APPLICATION_JSON);
        sendResponseText(ResponseStatus.OK, jsonResponse);
    }

    /**
     * Sets a response content type header.
     * @param contentType The content type.
     */
    public void setResponseContentType(final ContentType contentType) {
        final var headers = exchange.getResponseHeaders();
        headers.add("Content-type", contentType.toString());
    }

    /**
     * The payload of the request.
     * @return The payload.
     */
    public String getRequestPayload() {
        try(var mLOG = LOGGER_PLUS.groupLog("String getRequestPayload()")) {
            try (final var httpInput = new BufferedReader(new InputStreamReader(
                    exchange.getRequestBody()))) {

                var payload = httpInput.lines().collect(Collectors.joining());

                mLOG.debug(() -> "Request Payload: " + payload);

                return payload;

            } catch (IOException ioe) {
                mLOG.error(LOGGER_PLUS.getStackTraceAsString(ioe));
                throw new RuntimeException(ioe);
            }
        }
    }

    private Map<String, List<String>> parseQuery(String query) {
        final var queryParams = new HashMap<String, List<String>>();

        if (query != null) {
            final var pairs = query.split("&");

            for (String pair : pairs) {
                final var idx = pair.indexOf("=");
                final var key = idx > 0 ? pair.substring(0, idx) : pair;
                final var value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : null;

                if (!queryParams.containsKey(key)) {
                    queryParams.put(key, new ArrayList<>());
                }

                if (value != null) {
                    queryParams.get(key).add(value);
                }
            }
        }

        return queryParams;
    }

    public Map<String, List<String>> getRequestParameters() {
        final var queryString = exchange.getRequestURI().getQuery();
        return parseQuery(queryString);
    }

    public List<String> getRequestParameters(final String parameter) {
        final var requestParam = getRequestParameters();
        return requestParam.get(parameter);
    }

    public String getRequestParameter(final String parameter) {
        final var parameters = getRequestParameters(parameter);
        return parameters.isEmpty() ? null : parameters.get(0);
    }

    public void setAttribute(final String name, final Object value) {
        exchange.setAttribute(name, value);
    }

    public Object getAttribute(final String name) {
        return exchange.getAttribute(name);
    }

    public String getStringAttribute(final String name) {
        return (String) getAttribute(name);
    }

    public Headers getRequestHeaders() {
        return exchange.getRequestHeaders();
    }

    public List<String> getRequestHeaders(final String key) {
        final var headers = getRequestHeaders();
        return headers.get(key);
    }

    public String getRequestHeader(final String key) {
        final var headers = Optional.ofNullable(getRequestHeaders(key));
        return headers.orElse(Collections.emptyList()).stream().findFirst().orElse(null);
    }

}
