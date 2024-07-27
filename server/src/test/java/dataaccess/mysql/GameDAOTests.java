package dataaccess.mysql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.HashSet;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public
class GameDAOTests {
    static
    private final GameDAO dao;

    private GameData gameData = new GameData(1, null, null, "very nice game", new ChessGame());

    static {
        try {
            new MySQLUserDAO();
            dao = new MySQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static
    void addUsers() throws DataAccessException {
        dao.clear();
        new MySQLUserDAO().clear();
        MySQLUserDAO userDAO = new MySQLUserDAO();
        userDAO.createUser(new UserData("largebananafriend", "goodtrustyolhash", "goosee@goosee.com"));
    }

    @AfterEach
    void clearUserTable() throws DataAccessException {
        dao.clear();
    }

    @AfterAll
    static
    void clearUsers() throws DataAccessException {
        dao.clear();
        new MySQLUserDAO().clear();
    }

    @Test
    @Order(1)
    @DisplayName("CONSTRUCTOR: successfully instantiate MySQLGameDAO")
    void testConstructor() {
        Assertions.assertDoesNotThrow(MySQLGameDAO::new);
    }

    @Test
    @Order(2)
    @DisplayName("+createGame: add a new game to the database")
    void addGame() {
        Assertions.assertDoesNotThrow(() -> dao.createGame(gameData));
    }

    @Test
    @Order(3)
    @DisplayName("+createGame: add two games with the same name")
    void addTwoGames() throws DataAccessException {
        dao.createGame(gameData);
        Assertions.assertDoesNotThrow(
                () -> dao.createGame(new GameData(
                        gameData.whiteUsername(),
                        gameData.blackUsername(),
                        gameData.gameName(),
                        gameData.game()
                ))
        );
    }

    @Test
    @Order(4)
    @DisplayName("-createGame: create a new game with bad username")
    void createGameSameID() {
        Assertions.assertThrows(
                DataAccessException.class,
                () -> dao.createGame(new GameData(
                        "iDoNotExist",
                        null,
                        "weeIloveChess",
                        gameData.game()
                ))
        );
    }

    @Test
    @Order(5)
    @DisplayName("+getGame: Successfully get a game by its reference id")
    void getGameWithID() throws DataAccessException {
        dao.createGame(gameData);
        Assertions.assertDoesNotThrow(() -> dao.getGame(gameData.gameID()));
        var actualGame = dao.getGame(gameData.gameID());

        Assertions.assertEquals(gameData.gameID(), actualGame.gameID());
        Assertions.assertEquals(gameData.whiteUsername(), actualGame.whiteUsername());
        Assertions.assertEquals(gameData.blackUsername(), actualGame.blackUsername());
        Assertions.assertEquals(gameData.gameName(), actualGame.gameName());
        Assertions.assertEquals(gameData.game(), actualGame.game());
    }

    @Test
    @Order(6)
    @DisplayName("-getGame: Get game with an invalid ID")
    void getGameWithInvalidID() throws DataAccessException {
        dao.createGame(gameData);

        Assertions.assertNull(dao.getGame(1235));
    }

    @Test
    @Order(7)
    @DisplayName("+listGames: Successfully get a list of games")
    void getAllGames() throws DataAccessException {
        HashSet<GameData> expectedGames = new HashSet<>();
        expectedGames.add(gameData);
        expectedGames.add(new GameData(
                2,
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName(),
                gameData.game()
        ));

        dao.createGame(gameData);
        dao.createGame(new GameData(
                2,
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName(),
                gameData.game()
        ));

        Assertions.assertDoesNotThrow(dao::listGames);
        var actualGames = dao.listGames();

        Assertions.assertEquals(actualGames, expectedGames);
    }

    @Test
    @Order(8)
    @DisplayName("-listGames: Get games when there are no games")
    void listNoGames() throws DataAccessException {
        Assertions.assertEquals(new HashSet<>(), dao.listGames());
    }

    @Test
    @Order(9)
    @DisplayName("+updateGame: successfully update game")
    void updateGameTest() throws DataAccessException {
        dao.createGame(gameData);
        var updatedGame = new GameData(
                gameData.gameID(),
                "largebananafriend",
                gameData.blackUsername(),
                gameData.gameName(),
                gameData.game()
        );

        Assertions.assertDoesNotThrow(() -> dao.updateGame(updatedGame));
        Assertions.assertEquals(updatedGame, dao.getGame(updatedGame.gameID()));
    }

    @Test
    @Order(10)
    @DisplayName("-updateGame: try to update a game with an incorrect username")
    void updateGameDoesNotExist() throws DataAccessException {
        dao.createGame(gameData);
        Assertions.assertThrows(DataAccessException.class, () -> dao.updateGame(
                new GameData(1, "iDoNotExistshehe", null, "good game", new ChessGame())
        ));
    }

    @Test
    @Order(11)
    @DisplayName("+clear")
    void clearTest() {
        Assertions.assertDoesNotThrow(dao::clear);
    }
}
