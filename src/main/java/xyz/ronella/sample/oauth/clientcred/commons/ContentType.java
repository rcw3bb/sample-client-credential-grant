package xyz.ronella.sample.oauth.clientcred.commons;

import java.util.Arrays;
import java.util.Optional;

public enum ContentType {
    APPLICATION_ATOM_XML("application/atom+xml"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_JSON("application/json"),
    APPLICATION_JSON_PATCH_JSON("application/json-patch+json"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    APPLICATION_SVG_XML("application/svg+xml"),
    APPLICATION_XHTML_XML("application/xhtml+xml"),
    APPLICATION_XML("application/xml"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    TEXT_EVENT_STREAM("text/event-stream"),
    TEXT_HTML("text/html"),
    TEXT_PLAIN("text/plain"),
    TEXT_XML("text/xml"),
    WILDCARD("*/*")
    ;

    private final String text;

    ContentType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static Optional<ContentType> of(String contentType) {
        return Arrays.stream(ContentType.values()).filter(___type -> ___type.toString().equals(contentType)).findFirst();
    }
}
