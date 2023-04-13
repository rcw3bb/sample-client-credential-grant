package xyz.ronella.sample.oauth.clientcred.controller.impl.person;

import xyz.ronella.sample.oauth.clientcred.commons.ResponseStatus;
import xyz.ronella.sample.oauth.clientcred.wrapper.SimpleHttpExchange;

import java.util.Optional;

import static xyz.ronella.sample.oauth.clientcred.commons.Method.DELETE;
import static xyz.ronella.sample.oauth.clientcred.commons.Method.GET;

/**
 * A resource implementation for deleting a Person by ID.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public class PersonDeleteById extends AbstractPersonResource {

    /**
     * Creates an instance of PersonDeleteById.
     */
    public PersonDeleteById() {
        super();
    }

    /**
     * The logic for declaring that it can do processing for deleting a Person resource by ID.
     * @param simpleExchange An instance of SimpleHttpExchange.
     * @return Returns true to process.
     */
    @Override
    public boolean canProcess(final SimpleHttpExchange simpleExchange) {
        final var method = simpleExchange.getRequestMethod().orElse(GET);
        return super.canProcess(simpleExchange) && DELETE.equals(method);
    }

    /**
     * The logic for deleting a Person resource by ID.
     * @param simpleExchange An instance of SimpleHttpExchange.
     */
    @Override
    public void authProcess(final SimpleHttpExchange simpleExchange) {
        final var matchId = getPathMatcher(simpleExchange).group(1);
        final var id = Long.valueOf(matchId);
        final var person = Optional.ofNullable(getPersonService().findById(id));

        person.ifPresentOrElse(___person -> {
            getPersonService().delete(___person.getId());
            simpleExchange.sendResponseCode(ResponseStatus.OK);
        }, () -> simpleExchange.sendResponseCode(ResponseStatus.NOT_FOUND));
    }

    /**
     * The path pattern for identifying a path for deleting a Person resource by ID.
     * @return The path pattern.
     */
    @Override
    public String getPathPattern() {
        return String.format("^%s/person/(\\d*)$", getBaseURL());
    }
}
