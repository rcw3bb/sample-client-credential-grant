package xyz.ronella.sample.oauth.clientcred.commons;

import java.util.Arrays;
import java.util.Optional;

public enum Method {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE,
    COPY,
    HEAD,
    OPTIONS,
    LINK,
    UNLINK,
    PURGE,
    LOCK,
    UNLOCK,
    PROPFIND,
    VIEW;

    public static Optional<Method> of(String method) {
        return Arrays.stream(Method.values()).filter(___type -> ___type.toString().equals(method)).findFirst();
    }

}
