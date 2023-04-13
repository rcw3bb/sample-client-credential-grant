package xyz.ronella.sample.oauth.clientcred.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Person model
 *
 * @author Ron Webb
 * @since 1.0.0
 */
@NoArgsConstructor
@Data
public class Person {
    
    private Long id;
    private String firstName;
    private String lastName;

}

