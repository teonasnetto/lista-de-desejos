package com.teste.gatewayserver.constants;

public class PathConstants {

    public static final String AUTH_USER_ID_HEADER = "X-auth-user-id";

    public static final String AUTH_USERNAME_HEADER = "X-auth-username";

    public static final String API_V1 = "/api/v1";

    public static final String AUTH = "/auth";

    public static final String SIGN_UP = "/signup";

    public static final String SIGN_IN = "/signin";

    public static final String VALIDATE_TOKEN = "/validate?token=";

    public static final String API_V1_AUTH = API_V1 + AUTH;

    private PathConstants() {

    }
}
