package xyz.ronella.sample.oauth.clientcred.repository.impl;

import xyz.ronella.sample.oauth.clientcred.model.Person;
import xyz.ronella.sample.oauth.clientcred.repository.IPersonRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * An implementation for IPersonRepository using list as storage.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public class PersonListRepository implements IPersonRepository {

    private static final List<Person> persons = new ArrayList<>();

    static {
        //Default sample contents [BEGIN].
        final var person1 = new Person();
        person1.setId(1L);
        person1.setFirstName("Ronaldo");
        person1.setLastName("Webb");

        final var person2 = new Person();
        person2.setId(2L);
        person2.setFirstName("Juan");
        person2.setLastName("Dela Cruz");

        persons.add(person1);
        persons.add(person2);
        //Default sample contents [END].
    }

    /**
     * Retrieves list of Person.
     * @return A list of Person.
     */
    @Override
    public List<Person> findAll() {
        return persons;
    }

    /**
     * Find a person based on ID.
     * @param id The id of the target person.
     * @return The instance of person.
     */
    @Override
    public Person findById(final Long id) {
        return persons.stream().filter(___person -> id.equals(___person.getId())).findFirst().orElse(null);
    }

    /**
     * Add a person to a list.
     * @param person An instance of Person to be created.
     * @return The created Person from the list.
     */
    @Override
    public Person create(final Person person) {
        person.setId(persons.stream().max(Comparator.comparingLong(Person::getId)).orElseGet(()-> {
            final var defaultPerson = new Person();
            defaultPerson.setId(-1L);
            return defaultPerson;
        }).getId() + 1);
        persons.add(person);
        return person;
    }

    /**
     * Update a person in the list.
     * @param person An instance of Person to be updated.
     * @return The updated Person from the list.
     */
    @Override
    public Person update(final Person person) {
        final var updatedPerson = findById(person.getId());
        updatedPerson.setFirstName(person.getFirstName());
        updatedPerson.setLastName(person.getLastName());
        return updatedPerson;
    }

    /**
     * Delete a Person based on ID.
     * @param id The id of the target person.
     */
    @Override
    public void delete(final Long id) {
        final var deletePerson = findById(id);
        persons.remove(deletePerson);
    }
}

