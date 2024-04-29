package com.teste.userserver.constants;

public class ErrorMessage {

    public static final String NOT_FOUND = "NOT_FOUND";

    public static final String UNAUTHORIZED = "UNAUTHORIZED";

    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    public static final String UNAUTHORIZED_WITH_MESSAGE = "IN securityWebFilterChain - unauthorized error: {}";

    public static final String ACCESS_DENIED = "IN securityWebFilterChain - access denied: {}";

    public static final String ACCOUNT_DISABLED = "Account is disabled";

    public static final String PASSWORD_INVALID = "Invalid password";

    public static final String USERNAME_INVALID = "Invalid username";

    public static final String TOKEN_EXPIRED_WITH_MESSAGE = "Expired token: {}";

    public static final String TOKEN_EXPIRED = "Expired token";

    public static final String TOKEN_INVALID_WITH_MESSAGE = "Invalid token: {}";

    public static final String TOKEN_INVALID = "Invalid token";

    public static final String MISSED_AUTHORIZATION_HEADER = "Missed authorization header";

    public static final String USER_NOT_FOUND = "No user found with id: ";

    private ErrorMessage() {

    }
}
