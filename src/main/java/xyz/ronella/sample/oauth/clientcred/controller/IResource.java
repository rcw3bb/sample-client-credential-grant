package xyz.ronella.sample.oauth.clientcred.controller;

import xyz.ronella.sample.oauth.clientcred.wrapper.SimpleHttpExchange;

/**
 * Must be implemented to process a path resource.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public interface IResource {

    /**
     * Must have the logic that the particular implementation can handle the path resource.
     *
     * @param simpleExchange An instance of SimpleHttpExchange.
     * @return Returns true if the particular can handle the path resource.
     */
    boolean canProcess(SimpleHttpExchange simpleExchange);

    /**
     * Must hold the logic to process the path resource.
     * @param simpleExchange An instance of SimpleHttpExchange.
     */
    void process(SimpleHttpExchange simpleExchange);

    /**
     * Must return the regex of the target path pattern.
     * @return The target path pattern.
     */
    String getPathPattern();
}
