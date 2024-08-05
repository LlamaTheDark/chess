package client;

import exchange.game.CreateGameRequest;
import exchange.game.ListGamesRequest;
import exchange.user.LoginRequest;
import exchange.user.LogoutRequest;
import exchange.user.RegisterRequest;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import server.SessionHandler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public
class ServerFacadeTests {

    private static Server       server;
    private static ServerFacade sf = new ServerFacade();

    @BeforeAll
    public static
    void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static
    void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    public
    void registerP() throws Exception {
        assertDoesNotThrow(() -> {sf.register(new RegisterRequest("new_user", "password", "email@email.com"));});
    }

    @Test
    @Order(2)
    public
    void registerN() {
        assertThrows(Exception.class, () -> {sf.register(new RegisterRequest(null, "password", "email@email.com"));});
    }

    @Test
    @Order(3)
    public
    void loginP() throws Exception {
        assertDoesNotThrow(() -> {sf.login(new LoginRequest("new_user", "password"));});
        SessionHandler.authToken = sf.login(new LoginRequest("new_user", "password")).getAuthToken();
    }

    @Test
    @Order(4)
    public
    void loginN() {
        assertThrows(Exception.class, () -> {sf.login(new LoginRequest("new_user", "badpassword"));});
    }

    @Test
    @Order(5)
    public
    void logoutP() {
        assertDoesNotThrow(() -> {sf.logout(new LogoutRequest());});
        SessionHandler.authToken = "";
    }

    @Test
    @Order(6)
    public
    void logoutN() {
        assertThrows(Exception.class, () -> {sf.logout(new LogoutRequest());});
    }

    @Test
    @Order(7)
    public
    void createGameP() throws Exception {
        SessionHandler.authToken = sf.login(new LoginRequest("new_user", "password")).getAuthToken();
        assertDoesNotThrow(() -> sf.createGame(new CreateGameRequest("newGameNameYeeeee")));
    }

    @Test
    @Order(8)
    public
    void createGameN() {
        assertThrows(Exception.class, () -> {sf.createGame(new CreateGameRequest(null));});
    }

    @Test
    @Order(9)
    public
    void listGamesP() {
        assertDoesNotThrow(() -> {sf.listGames(new ListGamesRequest());});
    }

    @Test
    @Order(10)
    public
    void listGamesN() {
        SessionHandler.authToken = "";
        assertThrows(Exception.class, () -> {sf.listGames(new ListGamesRequest());});
    }

}
