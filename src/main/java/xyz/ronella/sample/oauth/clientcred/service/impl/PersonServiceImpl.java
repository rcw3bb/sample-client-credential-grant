package xyz.ronella.sample.oauth.clientcred.service.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import xyz.ronella.sample.oauth.clientcred.controller.impl.person.PersonResources;
import xyz.ronella.sample.oauth.clientcred.repository.IPersonRepository;
import xyz.ronella.sample.oauth.clientcred.model.Person;
import xyz.ronella.sample.oauth.clientcred.service.IPersonService;

import java.util.List;

/**
 * A simple implementation of a IPersonService.
 * @author Ron Webb
 * @since 1.0.0
 */
public class PersonServiceImpl implements IPersonService {

    private final IPersonRepository repository;

    /**
     * Creates an instance of PersonServiceImpl.
     * @param repository An implementation of  IPersonRepository.
     */
    @Inject
    public PersonServiceImpl(@Named(PersonResources.RESOURCE_NAME) final IPersonRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves a list of Person.
     * @return A list of Person.
     */
    public List<Person> findAll() {
        return repository.findAll();
    }

    /**
     * Retrieve a Person based on ID.
     * @param id The target ID.
     * @return An instance of located Person.
     */
    public Person findById(final Long id) {
        return repository.findById(id);
    }

    /**
     * Create a Person.
     * @param person An instance of Person to be created.
     * @return An instance of the Person created.
     */
    public Person create(final Person person) {
        return repository.create(person);
    }

    /**
     * Update a Person.
     * @param person An instance of Person to be updated.
     * @return An instance of the updated Person.
     */
    public Person update(final Person person) {
        return repository.update(person);
    }

    /**
     * Delete a Person by ID.
     * @param id The ID of the target Person to be deleted.
     */
    public void delete(final Long id) {
        repository.delete(id);
    }
}

