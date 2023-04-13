package xyz.ronella.sample.oauth.clientcred.controller.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import xyz.ronella.sample.oauth.clientcred.config.AppConfig;
import xyz.ronella.sample.oauth.clientcred.controller.IResource;
import xyz.ronella.sample.oauth.clientcred.controller.impl.auth.AuthResources;
import xyz.ronella.sample.oauth.clientcred.service.IAuthService;
import xyz.ronella.sample.oauth.clientcred.wrapper.SimpleHttpExchange;
import xyz.ronella.trivial.handy.RegExMatcher;

import java.util.regex.Matcher;

abstract public class AbstractResource implements IResource {

    @Inject
    @Named(AuthResources.RESOURCE_NAME)
    private IAuthService authService;

    private Matcher pathMatcher;

    public IAuthService getAuthService() {
        return authService;
    }

    /**
     * The Matcher basted on path pattern.
     * @param simpleExchange An instance of SimpleHttpExchange
     * @return An instance of Matcher.
     */
    protected Matcher getPathMatcher(final SimpleHttpExchange simpleExchange) {
        if (null == pathMatcher) {
            pathMatcher = RegExMatcher.find(getPathPattern(), simpleExchange.getRequestPath());
        }

        return pathMatcher;
    }

    /**
     * The default logic canProcess logic.
     * @param simpleExchange An instance of SimpleHttpExchange.
     * @return Returns true if this particular implementation the target path pattern.
     */
    @Override
    public boolean canProcess(final SimpleHttpExchange simpleExchange) {
        return getPathMatcher(simpleExchange).matches();
    }

    /**
     * The base url configured in application.properties.
     * @return The base url.
     */
    protected String getBaseURL() {
        return AppConfig.INSTANCE.getBaseURL();
    }
}
