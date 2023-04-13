package xyz.ronella.sample.oauth.clientcred.service;

import xyz.ronella.sample.oauth.clientcred.model.Person;

import java.util.List;


/**
 * Must hold the business logic for person resource.
 * @author Ron Webb
 * @since 1.0.0
 */
public interface IPersonService {

    /**
     * Must hold the business logic to return all the persons.
     * @return A list of Person.
     */
    List<Person> findAll();

    /**
     * Must hold the business logic to return a person by ID.
     * @param id The target ID of a Person.
     * @return The instance of the found Person.
     */
    Person findById(Long id);

    /**
     * Must hold the business logic to return create a person.
     * @param person An instance of Person to be created.
     * @return An instance of Person that was created.
     */
    Person create(Person person);

    /**
     * Must hold the business logic to update a person.
     * @param person An instance of Person to be updated.
     * @return The instance of Person that was updated.
     */
    Person update(Person person);

    /**
     * Must hold the business logic to delete a person by ID.
     * @param id The ID of the target Person to be deleted.
     */
    void delete(Long id);
}