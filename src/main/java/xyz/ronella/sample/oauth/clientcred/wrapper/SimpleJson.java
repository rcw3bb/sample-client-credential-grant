package xyz.ronella.sample.oauth.clientcred.wrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The only class that knows about json conversion.
 * @param <T> The type of object that can be converted to json.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
public class SimpleJson<T> {

    private final ObjectMapper objectMapper;

    /**
     * Creates an instance of SimpleJson.
     */
    public SimpleJson() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Converts the object to json text.
     * @param object The object to be converted to json.
     * @return The json text.
     */
    public String toJsonText(final T object) {
        if (null != object) {
            try {
                return objectMapper.writeValueAsString(object);
            } catch (JsonProcessingException jpe) {
                throw new RuntimeException(jpe);
            }
        }
        return null;
    }

    /**
     * Converts a json text to object.
     * @param json The json text.
     * @param clazz The type of the object to converted to.
     * @return The object the corresponds to json.
     */
    public T toObjectType(final String json, final Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException jpe) {
            throw new RuntimeException(jpe);
        }
    }

}
