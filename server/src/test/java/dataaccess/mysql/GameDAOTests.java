package dataaccess.mysql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

public
class GameDAOTests {
    static
    private final GameDAO dao;

    private GameData gameData = new GameData(0, "", "", "", new ChessGame());

    static {
        try {
            dao = new MySQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void clearUserTable() throws DataAccessException {
        dao.clear();
    }

    @Test
    @DisplayName("CONSTRUCTOR: successfully instantiate MySQLGameDAO")
    void testConstructor() {
        Assertions.assertDoesNotThrow(MySQLGameDAO::new);
    }

    @Test
    @DisplayName("+createGame: add a new game to the database")
    void addGame() {
        Assertions.assertDoesNotThrow(() -> dao.createGame(gameData));
    }

    @Test
    @DisplayName("+createGame: add two games with the same name (different ID)")
    void addTwoGames() throws DataAccessException {
        dao.createGame(gameData);
        Assertions.assertThrows(
                DataAccessException.class,
                () -> dao.createGame(new GameData(
                        1,
                        gameData.whiteUsername(),
                        gameData.blackUsername(),
                        gameData.gameName(),
                        gameData.game()
                ))
        );
    }

    @Test
    @DisplayName("-createGame: create a new game with the same ID")
    void createGameSameID() throws DataAccessException {
        dao.createGame(gameData);
        Assertions.assertThrows(DataAccessException.class, () -> dao.createGame(gameData));
    }

    @Test
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
    @DisplayName("-getGame: Get game with an invalid ID")
    void getGameWithInvalidID() throws DataAccessException {
        dao.createGame(gameData);

        Assertions.assertThrows(DataAccessException.class, () -> dao.getGame(1235));
    }

    @Test
    @DisplayName("+listGames: Successfully get a list of games")
    void getAllGames() throws DataAccessException {
        HashSet<GameData> expectedGames = new HashSet<>();
        expectedGames.add(gameData);
        expectedGames.add(new GameData(
                1,
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName(),
                gameData.game()
        ));

        dao.createGame(gameData);
        dao.createGame(new GameData(
                1,
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
    @DisplayName("-listGames: Get games when there are no games")
    void listNoGames() throws DataAccessException {
        Assertions.assertNull(dao.listGames());
    }

    @Test
    @DisplayName("+updateGame: successfully update game")
    void updateGameTest() throws DataAccessException {
        dao.createGame(gameData);
        var game = gameData.game();
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
    @DisplayName("-updateGame: try to update a game that doesn't exist")
    void updateGameDoesNotExist() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> dao.updateGame(gameData));
    }

    @Test
    @DisplayName("+clear")
    void clearTest() {
        Assertions.assertDoesNotThrow(dao::clear);
    }
}
