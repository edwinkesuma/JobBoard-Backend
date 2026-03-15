package com.edwinkesuma.jobboard.constants;

public class ApplicationConstants {
    private ApplicationConstants() {
        throw new AssertionError("Utility class cannot be instantiated.");
    }

    public static final String JWT_SECRET_KEY = "JWT_SECRET";
    public static final String JWT_HEADER = "Authorization";
}
