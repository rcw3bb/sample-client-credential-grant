package xyz.ronella.sample.oauth.clientcred.controller.impl.person;

import xyz.ronella.sample.oauth.clientcred.commons.ResponseStatus;
import xyz.ronella.sample.oauth.clientcred.wrapper.SimpleHttpExchange;

import java.util.Optional;

/**
 * A resource implementation for retrieving a Person by ID.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public class PersonRetrieveById extends PersonRetrieveAll {

    /**
     * Creates an instance of PersonRetrieveById.
     */
    public PersonRetrieveById() {
        super();
    }

    /**
     * The logic for declaring that it can do processing for retrieving a Person by ID.
     * @param simpleExchange An instance of SimpleHttpExchange.
     * @return Returns true to process.
     */
    @Override
    public boolean canProcess(final SimpleHttpExchange simpleExchange) {
        return super.canProcess(simpleExchange);
    }

    /**
     * The logic for retrieving a Person resource by ID.
     * @param simpleExchange An instance of SimpleHttpExchange.
     */
    @Override
    public void authProcess(final SimpleHttpExchange simpleExchange) {
        final var matchId = getPathMatcher(simpleExchange).group(1);
        final var id = Long.valueOf(matchId);
        final var person = Optional.ofNullable(getPersonService().findById(id));

        person.ifPresentOrElse(___person -> {
            final var response = personToJson(___person);
            simpleExchange.sendJsonResponse(response);
        }, () -> simpleExchange.sendResponseCode(ResponseStatus.NOT_FOUND));
    }

    /**
     * The path pattern for identifying a path for retrieving a Person resource by ID.
     * @return The path pattern.
     */
    @Override
    public String getPathPattern() {
        return String.format("^%s/person/(\\d*)$", getBaseURL());
    }
}
