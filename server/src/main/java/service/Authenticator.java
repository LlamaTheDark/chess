package service;

import java.util.UUID;

public class Authenticator {
    public static boolean authenticate(String authToken) {
        /*
        TODO: implement
         */
        return true;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
