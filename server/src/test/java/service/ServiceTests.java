package service;

import dataaccess.DataAccessException;
import exchange.dev.ClearApplicationRequest;
import exchange.game.CreateGameRequest;
import exchange.game.CreateGameResponse;
import exchange.game.JoinGameRequest;
import exchange.game.ListGamesRequest;
import exchange.user.LoginRequest;
import exchange.user.LoginResponse;
import exchange.user.LogoutRequest;
import exchange.user.RegisterRequest;
import org.junit.jupiter.api.*;
import service.dev.ClearApplicationService;
import service.exception.BadRequestException;
import service.exception.ForbiddenException;
import service.exception.ServiceException;
import service.exception.UnauthorizedException;
import service.game.CreateGameService;
import service.game.JoinGameService;
import service.game.ListGamesService;
import service.user.LoginService;
import service.user.LogoutService;
import service.user.RegisterService;
import service.util.Authenticator;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public
class ServiceTests {
    private static String ogltdAuthToken;
    private static String goofballAuthToken;

    @BeforeAll
    public static
    void setup() throws ServiceException, DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("ogltd", "beans123", "ogltd@gmail.com");
        var response = new RegisterService().serve(registerRequest);
        ogltdAuthToken = response.getAuthToken();
    }

    // REGISTRATION TESTS
    @Test
    @Order(1)
    @DisplayName("Normal Registration")
    public
    void normalRegistration() throws ServiceException, DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("darthtater13", "peachpit12", "darthtater13@gmail.com");
        var actualResponse = new RegisterService().serve(registerRequest);

        Assertions.assertEquals(actualResponse.getUsername(), "darthtater13");
        Assertions.assertNotNull(actualResponse.getAuthToken());
    }

    @Test
    @Order(2)
    @DisplayName("Register The Same User Twice")
    public
    void registerTheSameUserTwice() throws ServiceException, DataAccessException {
        RegisterRequest firstRegisterRequest = new RegisterRequest("goofball", "wackytotoro", "goofball@gmail.com");
        var response = new RegisterService().serve(firstRegisterRequest);
        goofballAuthToken = response.getAuthToken();

        Assertions.assertThrows(ForbiddenException.class, () -> new RegisterService().serve(firstRegisterRequest));
    }

    // LOGIN TESTS
    @Test
    @Order(3)
    @DisplayName("Normal User Login")
    public
    void normalUserLogin() throws ServiceException, DataAccessException {
        LoginRequest request = new LoginRequest("ogltd", "beans123");
        LoginResponse actualResponse = new LoginService().serve(request);

        Assertions.assertEquals(actualResponse.getUsername(), "ogltd");
        Assertions.assertNotNull(actualResponse.getAuthToken());
    }

    @Test
    @Order(4)
    @DisplayName("Bad Request Login Attempt")
    public
    void badRequestLoginAttempt() {
        Assertions.assertThrows(
                BadRequestException.class,
                () -> new LoginService().serve(new LoginRequest(null, "beans123"))
        );
    }

    // LOGOUT TESTS
    @Test
    @Order(5)
    @DisplayName("Normal Logout")
    public
    void normalLogout() {
        LogoutRequest request = new LogoutRequest(ogltdAuthToken);
        Assertions.assertDoesNotThrow(() -> {
                                          new LogoutService().serve(request);
                                      }
        );
    }

    @Test
    @Order(6)
    @DisplayName("Logout User Who Is Not Logged In")
    public
    void logoutUserWhoIsNotLoggedIn() {
        Assertions.assertThrows(
                UnauthorizedException.class,
                () -> new LogoutService().serve(new LogoutRequest(ogltdAuthToken))
        );
    }

    // CREATE GAME TESTS
    @Test
    @Order(7)
    @DisplayName("Normal Create Game")
    public
    void normalCreateGame() throws ServiceException, DataAccessException {
        var response = new CreateGameService().serve(new CreateGameRequest("Test Game", goofballAuthToken));
        var expectedResponse = new CreateGameResponse(1);

        Assertions.assertEquals(response.getGameID(), expectedResponse.getGameID());
    }

    @Test
    @Order(8)
    @DisplayName("Create Game No AuthToken")
    public
    void createGameNoAuthToken() {
        Assertions.assertThrows(
                UnauthorizedException.class,
                () -> new CreateGameService().serve(new CreateGameRequest("Test Game", null))
        );
    }

    // JOIN GAME TESTS
    @Test
    @Order(9)
    @DisplayName("Successfully Join Game White Team")
    public
    void successfullyJoinGameWhiteTeam() {
        Assertions.assertDoesNotThrow(
                () -> {
                    new JoinGameService().serve(new JoinGameRequest("WHITE", 1, goofballAuthToken));
                }
        );
    }

    @Test
    @Order(10)
    @DisplayName("Try to Join Game on Taken Team")
    public
    void tryToJoinGameOnTakenTeam() {
        Assertions.assertThrows(
                ForbiddenException.class,
                () -> new JoinGameService().serve(new JoinGameRequest("WHITE", 1, goofballAuthToken))
        );
    }

    // LIST GAMES TESTS
    @Test
    @Order(11)
    @DisplayName("Normal Request List of Games")
    public
    void normalRequestListOfGames() {
        Assertions.assertDoesNotThrow(() -> {
                                          new ListGamesService().serve(new ListGamesRequest(goofballAuthToken));
                                      }
        );
    }

    @Test
    @Order(12)
    @DisplayName("Unauthorized List Games")
    public
    void unauthorizedListGames() {
        Assertions.assertThrows(
                UnauthorizedException.class,
                () -> new ListGamesService().serve(new ListGamesRequest(null))
        );
    }

    // AUTHENTICATOR TESTS
    @Test
    @Order(13)
    @DisplayName("Authenticate Logged-in User")
    public
    void authenticateLoggedInUser() {
        Assertions.assertDoesNotThrow(() -> Authenticator.authenticate(goofballAuthToken));
    }

    @Test
    @Order(14)
    @DisplayName("Reject Logged-out User")
    public
    void rejectLoggedOutUser() {
        Assertions.assertThrows(UnauthorizedException.class, () -> Authenticator.authenticate(ogltdAuthToken));
    }

    @Test
    @Order(15)
    @DisplayName("Generate Unique Tokens")
    public
    void generateUniqueTokens() {
        Assertions.assertNotEquals(Authenticator.generateToken(), Authenticator.generateToken());
    }

    // CLEAR APPLICATION TESTS
    @Test
    @Order(17)
    @DisplayName("Normal Clear Application")
    public
    void normalClearApplication() {
        Assertions.assertDoesNotThrow(
                () -> {
                    new ClearApplicationService().serve(new ClearApplicationRequest());
                }
        );
    }
}
