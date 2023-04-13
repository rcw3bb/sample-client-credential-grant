/**
 * The module definition.
 *
 * @author Ron Webb
 * @since 1.0.0
 */
open module xyz.ronella.sample.oauth.clientcred {
    requires static lombok;

    requires com.fasterxml.jackson.databind;

    requires com.auth0.jwt;
    requires com.google.guice;
    requires com.google.gson;

    requires java.net.http;

    requires jdk.httpserver;

    requires org.slf4j;
    requires org.apache.logging.log4j;

    requires xyz.ronella.casual.trivial;
    requires xyz.ronella.logging.logger.plus;

}