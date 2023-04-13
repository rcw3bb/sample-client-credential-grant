package xyz.ronella.sample.oauth.clientcred.commons;

/**
 * Must hold all the constants used in the application.
 *
 * @author Ron Webb
 */
public interface Constant {

    String ATTR_STATE = "state";
    String RQT_PARAM_CODE = "redirect_url";
    String RQT_PARAM_STATE = ATTR_STATE;
    String URL_ENTRY = "/entry";
    String URL_HOME = "/";
    String URL_REFRESH = "/refresh";
    String ACCESS_TOKEN = "ACCESS_TOKEN";
    String REFRESH_TOKEN = "SAMPLE_REFRESH_TOKEN";
    String SESSION_STATE = "SAMPLE_SESSION_STATE";

}
