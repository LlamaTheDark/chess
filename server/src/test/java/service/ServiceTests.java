package service;

import dataaccess.DataAccessException;
import exchange.game.CreateGameRequest;
import exchange.game.CreateGameResponse;
import exchange.game.JoinGameRequest;
import exchange.game.ListGamesRequest;
import exchange.user.LoginRequest;
import exchange.user.LoginResponse;
import exchange.user.LogoutRequest;
import exchange.user.RegisterRequest;
import org.junit.jupiter.api.*;
import service.error.BadRequestException;
import service.error.ForbiddenException;
import service.error.ServiceException;
import service.error.UnauthorizedException;
import service.game.CreateGameService;
import service.game.JoinGameService;
import service.game.ListGamesService;
import service.user.LoginService;
import service.user.LogoutService;
import service.user.RegisterService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {
    private static String ogltdAuthToken;
    private static String goofballAuthToken;

    @BeforeAll
    public static void setup() throws ServiceException, DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("ogltd", "beans123", "ogltd@gmail.com");
        var response = new RegisterService().serve(registerRequest);
        ogltdAuthToken = response.getAuthToken();
    }

    // REGISTRATION TESTS
    @Test
    @Order(1)
    @DisplayName("Normal Registration")
    public void normalRegistration() throws ServiceException, DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("darthtater13", "peachpit12", "darthtater13@gmail.com");
        var actualResponse = new RegisterService().serve(registerRequest);

        Assertions.assertEquals(actualResponse.getUsername(), "darthtater13");
        Assertions.assertNotNull(actualResponse.getAuthToken());
    }

    @Test
    @Order(2)
    @DisplayName("Register The Same User Twice")
    public void registerTheSameUserTwice() throws ServiceException, DataAccessException {
        RegisterRequest firstRegisterRequest = new RegisterRequest("goofball", "wackytotoro", "goofball@gmail.com");
        var response = new RegisterService().serve(firstRegisterRequest);
        goofballAuthToken = response.getAuthToken();

        Assertions.assertThrows(ForbiddenException.class,
                () -> {new RegisterService().serve(firstRegisterRequest); });
    }

    // LOGIN TESTS
    @Test
    @Order(3)
    @DisplayName("Normal User Login")
    public void normalUserLogin() throws ServiceException, DataAccessException {
        LoginRequest request = new LoginRequest("ogltd", "beans123");
        LoginResponse actualResponse = new LoginService().serve(request);

        Assertions.assertEquals(actualResponse.getUsername(), "ogltd");
        Assertions.assertNotNull(actualResponse.getAuthToken());
    }

    @Test
    @Order(4)
    @DisplayName("Bad Request Login Attempt")
    public void badRequestLoginAttempt() throws UnauthorizedException, DataAccessException {
        Assertions.assertThrows(BadRequestException.class,
                () -> {new LoginService().serve(new LoginRequest(null, "beans123"));}
        );
    }

    // LOGOUT TESTS
    @Test
    @Order(5)
    @DisplayName("Normal Logout")
    public void normalLogout() throws ServiceException, DataAccessException {
        LogoutRequest request = new LogoutRequest(ogltdAuthToken);
        Assertions.assertDoesNotThrow(
                () -> { new LogoutService().serve(request); }
        );
    }

    @Test
    @Order(6)
    @DisplayName("Logout User Who Is Not Logged In")
    public void logoutUserWhoIsNotLoggedIn() throws ServiceException, DataAccessException {
        Assertions.assertThrows(UnauthorizedException.class,
                () -> { new LogoutService().serve(new LogoutRequest(ogltdAuthToken)); }
        );
    }

    // CREATE GAME TESTS
    @Test
    @Order(7)
    @DisplayName("Normal Create Game")
    public void normalCreateGame() throws ServiceException, DataAccessException {
        CreateGameRequest request = new CreateGameRequest("testGame");
        request.setAuthToken(goofballAuthToken);

        var response = new CreateGameService().serve(new CreateGameRequest("Test Game", goofballAuthToken));
        var expectedResponse = new CreateGameResponse(1);

        Assertions.assertEquals(response.getGameID(), expectedResponse.getGameID());
    }

    @Test
    @Order(8)
    @DisplayName("Create Game No AuthToken")
    public void createGameNoAuthToken() throws ServiceException, DataAccessException {
        Assertions.assertThrows(UnauthorizedException.class,
                () -> { new CreateGameService().serve(new CreateGameRequest("Test Game", null)); }
        );
    }

    // JOIN GAME TESTS
    @Test
    @Order(9)
    @DisplayName("Successfully Join Game White Team")
    public void successfullyJoinGameWhiteTeam() throws ServiceException, DataAccessException {
        Assertions.assertDoesNotThrow(
                () -> {new JoinGameService().serve(new JoinGameRequest("WHITE", 1, goofballAuthToken));}
        );
    }

    @Test
    @Order(10)
    @DisplayName("Try to Join Game on Taken Team")
    public void tryToJoinGameOnTakenTeam() throws ServiceException, DataAccessException {
        Assertions.assertThrows(ForbiddenException.class,
                () -> {new JoinGameService().serve(new JoinGameRequest("WHITE", 1, goofballAuthToken)); }
        );
    }

    // LIST GAMES TESTS
    @Test
    @Order(11)
    @DisplayName("Normal Request List of Games")
    public void normalRequestListOfGames() throws ServiceException, DataAccessException {
        Assertions.assertDoesNotThrow(
                () -> { new ListGamesService().serve(new ListGamesRequest(goofballAuthToken)); }
        );
    }

    @Test
    @Order(12)
    @DisplayName("Unauthorized List Games")
    public void unauthorizedListGames() throws ServiceException, DataAccessException {
        Assertions.assertThrows(UnauthorizedException.class,
                () -> { new ListGamesService().serve(new ListGamesRequest(null)); }
        );
    }

    // CLEAR APPLICATION TESTS

}
