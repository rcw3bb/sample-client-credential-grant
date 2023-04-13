package xyz.ronella.sample.oauth.clientcred.controller.impl.person;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import xyz.ronella.sample.oauth.clientcred.commons.ResponseStatus;
import xyz.ronella.sample.oauth.clientcred.controller.impl.AbstractResource;
import xyz.ronella.sample.oauth.clientcred.controller.impl.auth.AuthResources;
import xyz.ronella.sample.oauth.clientcred.model.Person;
import xyz.ronella.sample.oauth.clientcred.service.IAuthService;
import xyz.ronella.sample.oauth.clientcred.service.IPersonService;

import xyz.ronella.sample.oauth.clientcred.wrapper.SimpleHttpExchange;
import xyz.ronella.sample.oauth.clientcred.wrapper.SimpleJson;

import java.util.List;
import java.util.Optional;

/**
 * A partial implementation of IResource.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public abstract class AbstractPersonResource extends AbstractResource {

    @Inject
    @Named(AuthResources.RESOURCE_NAME)
    private IAuthService authService;

    /**
     * An instance of IPersonService.
     */
    @Inject
    @Named(PersonResources.RESOURCE_NAME)
    private IPersonService personService;

    protected IPersonService getPersonService() {
        return personService;
    }

    public IAuthService getAuthService() {
        return authService;
    }

    /**
     * The default path pattern implementation.
     * @return The path pattern.
     */
    @Override
    public String getPathPattern() {
        return String.format("^%s/person$", getBaseURL());
    }

    /**
     * Converts the Person object to json text.
     * @param person An instance of Person.
     * @return The json text.
     */
    protected String personToJson(Person person) {
        final var mapper = new SimpleJson<Person>();
        return mapper.toJsonText(person);
    }

    /**
     * Coverts a list of Person object to json text.
     * @param persons A list of Person object.
     * @return The json text.
     */
    protected String personListToJson(final List<Person> persons) {
        final var mapper = new SimpleJson<List<Person>>();
        return mapper.toJsonText(persons);
    }

    /**
     * Converts a json text to Person object.
     * @param json The json text.
     * @return An instance of Person object.
     */
    protected Person jsonToPerson(String json) {
        final var simpleJson = new SimpleJson<Person>();
        return simpleJson.toObjectType(json, Person.class);
    }

    abstract void authProcess(final SimpleHttpExchange simpleExchange);

    @Override
    public void process(final SimpleHttpExchange simpleExchange) {

        final var auth = Optional.ofNullable(simpleExchange.getRequestHeader("Authorization"));

        if (auth.isPresent()) {
            final var accessToken = auth.get().substring(7); // Remove "Bearer " prefix

            try {
                final var userName = getAuthService().getUsername(accessToken);

                if (userName.isPresent()) {
                    authProcess(simpleExchange);
                }
            }
            catch(RuntimeException re){
                re.printStackTrace(System.out);
            }
        }

        simpleExchange.sendResponseCode(ResponseStatus.UNAUTHORIZED);
    }
}
