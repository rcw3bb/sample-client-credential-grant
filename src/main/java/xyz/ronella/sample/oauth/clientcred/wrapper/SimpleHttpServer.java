package xyz.ronella.sample.oauth.clientcred.wrapper;

import com.sun.net.httpserver.HttpServer;

import xyz.ronella.sample.oauth.clientcred.controller.IResource;
import xyz.ronella.sample.oauth.clientcred.controller.impl.auth.AuthResources;
import xyz.ronella.sample.oauth.clientcred.controller.impl.person.PersonResources;
import xyz.ronella.sample.oauth.clientcred.commons.ResponseStatus;
import xyz.ronella.sample.oauth.clientcred.config.AppConfig;
import xyz.ronella.trivial.decorator.Mutable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Optional;

/**
 * The class that only knows about the HttpServer.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
final public class SimpleHttpServer {

    private static final AppConfig CONFIG= AppConfig.INSTANCE;

    private final HttpServer httpServer;

    private SimpleHttpServer() throws IOException {
        final var defaultContext = "/";
        final var baseUrl = CONFIG.getBaseURL();
        httpServer = HttpServer.create(new InetSocketAddress(CONFIG.getServerPort()), 0);
        final var context = httpServer.createContext(baseUrl.isEmpty() ? defaultContext : baseUrl);

        context.setHandler(___exchange -> {
            final var simpleExchange = new SimpleHttpExchange(___exchange);
            final var personResource = PersonResources.createResource(simpleExchange);
            final var authResource = AuthResources.createResource(simpleExchange);
            final var foundResource = new Mutable<IResource>(null);

            personResource.ifPresent(foundResource::set);
            authResource.ifPresent(foundResource::set);

            final var optResource = Optional.ofNullable(foundResource.get());

            optResource.ifPresentOrElse(___resource -> ___resource.process(simpleExchange),
                    () -> simpleExchange.sendResponseCode(ResponseStatus.NO_CONTENT));

        });
    }

    /**
     * Starts the server.
     */
    public void start() {
        httpServer.start();
    }

    /**
     * Stops the server.
     */
    public void stop() {
        httpServer.stop(1);
    }

    /**
     * Create an instance of SimpleHttpServer.
     * @return An instance of SimpleHttpServer.
     * @throws IOException The exception that must be handled.
     */
    public static SimpleHttpServer createServer() throws IOException {
        return new SimpleHttpServer();
    }

}
