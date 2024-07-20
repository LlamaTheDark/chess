package service;

import dataaccess.DataAccessException;
import exchange.user.LoginRequest;
import exchange.user.LoginResponse;
import exchange.user.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.error.BadRequestException;
import service.error.ForbiddenException;
import service.error.ServiceException;
import service.error.UnauthorizedException;
import service.user.LoginService;
import service.user.RegisterService;

public class ServiceTests {
    private static String initialAuthToken;

    @BeforeAll
    public static void setup() throws ServiceException, DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("ogltd", "beans123", "ogltd@gmail.com");
        var response = new RegisterService().serve(registerRequest);
        initialAuthToken = response.getAuthToken();
    }

    // REGISTRATION TESTS
    @Test
    @DisplayName("Normal Registration")
    public void normalRegistration() throws ServiceException, DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("darthtater13", "peachpit12", "darthtater13@gmail.com");
        var actualResponse = new RegisterService().serve(registerRequest);

        Assertions.assertEquals(actualResponse.getUsername(), "darthtater13");
        Assertions.assertNotNull(actualResponse.getAuthToken());
    }

    @Test
    @DisplayName("Register The Same User Twice")
    public void registerTheSameUserTwice() throws ServiceException, DataAccessException {
        RegisterRequest firstRegisterRequest = new RegisterRequest("goofball", "wackytotoro", "goofball@gmail.com");
        new RegisterService().serve(firstRegisterRequest);

        Assertions.assertThrows(ForbiddenException.class,
                () -> {new RegisterService().serve(firstRegisterRequest); });
    }

    // LOGIN TESTS
    @Test
    @DisplayName("Normal User Login")
    public void normalUserLogin() throws ServiceException, DataAccessException {
        LoginRequest request = new LoginRequest("ogltd", "beans123");
        LoginResponse actualResponse = new LoginService().serve(request);

        Assertions.assertEquals(actualResponse.getUsername(), "ogltd");
        Assertions.assertNotNull(actualResponse.getAuthToken());
    }

    @Test
    @DisplayName("Bad Request Login Attempt")
    public void badRequestLoginAttempt() throws UnauthorizedException, DataAccessException {
        Assertions.assertThrows(BadRequestException.class,
                () -> {new LoginService().serve(new LoginRequest(null, "beans123"));}
        );
    }


    // LOGOUT TESTS


    // CREATE GAME TESTS


    // JOIN GAME TESTS


    // LIST GAMES TESTS


    // CLEAR APPLICATION TESTS

}
