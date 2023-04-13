package xyz.ronella.sample.oauth.clientcred.config;

import org.slf4j.LoggerFactory;
import xyz.ronella.logging.LoggerPlus;
import xyz.ronella.trivial.handy.PathFinder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Reads the application.properties file inside.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
final public class AppConfig {

    private static final LoggerPlus LOGGER_PLUS = new LoggerPlus(LoggerFactory.getLogger(AppConfig.class));

    /**
     * Returns an instance of AppConfig.
     */
    public final static AppConfig INSTANCE = new AppConfig();

    final private ResourceBundle prop;

    private AppConfig() {
        try {
            final var confName = "application.properties";
            final var locations = List.of("../conf", "conf");
            final var confFound = PathFinder.getBuilder(confName).addPaths(locations).build().getFile();
            final var propFile = confFound.get();

            try(final var versionProp = new FileInputStream(propFile)) {
                this.prop = new PropertyResourceBundle(versionProp);
            }
        } catch (IOException exp) {
            LOGGER_PLUS.error(LOGGER_PLUS.getStackTraceAsString(exp));
            throw new RuntimeException(exp);
        }
    }

    private String getPropString(final String key) {
        try {
            return prop.getString(key);
        }
        catch (MissingResourceException mre) {
            return "";
        }
    }

    /**
     * Reads the value of the server.port.
     * @return The value of the server.port as integer.
     */
    public int getServerPort() {
        final var port = getPropString("server.port");
        return Integer.parseInt(port);
    }

    /**
     * Reads the value of the base.url.
     * @return The value of the base.url.
     */
    public String getBaseURL() {
        return getPropString("base.url").trim();
    }

    /**
     * Reads the value of the token.url.
     * @return The value of the token.url.
     */
    public String getTokenURL() {
        return getPropString("token.url").trim();
    }

    /**
     * Reads the value of the client.id.
     * @return The value of the client.id.
     */
    public String getClientId() {
        return getPropString("client.id").trim();
    }

    /**
     * Reads the value of the client.secret.
     * @return The value of the client.secret.
     */
    public String getClientSecret() {
        return getPropString("client.secret").trim();
    }

    /**
     * Reads the value of the auth.issuer.
     * @return The value of the auth.issuer.
     */
    public String getAuthIssuer() {
        return getPropString("auth.issuer").trim();
    }

    /**
     * Reads the value of the auth.audience.
     * @return The value of the auth.audience.
     */
    public String getAuthAudience() {
        return getPropString("auth.audience").trim();
    }
}