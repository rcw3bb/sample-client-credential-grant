package xyz.ronella.sample.oauth.clientcred.controller.impl.person;

import xyz.ronella.sample.oauth.clientcred.commons.ResponseStatus;
import xyz.ronella.sample.oauth.clientcred.wrapper.SimpleHttpExchange;

import java.util.Optional;

import static xyz.ronella.sample.oauth.clientcred.commons.Method.GET;

/**
 * A resource implementation for retrieving all the Persons.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public class PersonRetrieveAll extends AbstractPersonResource {

    /**
     * Creates an instance of PersonRetrieveAll.
     */
    public PersonRetrieveAll() {
        super();
    }

    /**
     * The logic for declaring that it can do processing for returning all the Persons.
     * @param simpleExchange An instance of SimpleHttpExchange.
     * @return Returns true to process.
     */
    @Override
    public boolean canProcess(final SimpleHttpExchange simpleExchange) {
        final var method = simpleExchange.getRequestMethod().orElse(GET);
        return super.canProcess(simpleExchange) && GET.equals(method);
    }

    @Override
    void authProcess(final SimpleHttpExchange simpleExchange) {
        final var persons = getPersonService().findAll();
        final var response = personListToJson(persons);

        simpleExchange.sendJsonResponse(response);
    }

}
