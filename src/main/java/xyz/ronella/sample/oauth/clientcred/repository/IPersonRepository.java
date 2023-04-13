package xyz.ronella.sample.oauth.clientcred.repository;

import xyz.ronella.sample.oauth.clientcred.model.Person;

import java.util.List;

/**
 * Must hold the implementation of how to manage a person's repository.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public interface IPersonRepository {

    /**
     * Must hold the logic for retrieving all the person.
     * @return A list of Person.
     */
    List<Person> findAll();

    /**
     * Must hold the logic for finding a person by ID.
     * @param id The id of the target person.
     * @return An instance of Person.
     */
    Person findById(Long id);

    /**
     * Must hold the logic for creating a person.
     * @param person An instance of Person to be created.
     * @return The created instance of a Person.
     */
    Person create(Person person);

    /**
     * Must hold the logic for updating a person.
     * @param person An instance of Person to be updated.
     * @return The updated instance of a Person.
     */
    Person update(Person person);

    /**
     * Must hold the logic for deleting a person.
     * @param id The id of the target person.
     */
    void delete(Long id);
}
