package dataaccess.mysql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import handle.util.Serializer;
import model.GameData;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public
class MySQLGameDAO implements GameDAO {

    private final static String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
                gameID INT NOT NULL AUTO_INCREMENT,
                whiteUsername VARCHAR(255) NULL,
                blackUsername VARCHAR(255) NULL,
                gameName VARCHAR(255) NOT NULL,
                game TEXT NOT NULL,
                PRIMARY KEY (gameID),
                FOREIGN KEY(whiteUsername) REFERENCES user(username),
                FOREIGN KEY(blackUsername) REFERENCES user(username)
            )
            """
    };

    public
    MySQLGameDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }


    /**
     * Stores a <code>GameData</code> instance in the database.
     *
     * @param data The <code>GameData</code> instance to be stored.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    GameData createGame(GameData data) throws DataAccessException {
        int gameID = DatabaseManager.executeUpdate(
                """
                INSERT INTO game (whiteUsername, blackUsername, gameName, game)
                VALUES (?, ?, ?, ?)
                """,
                data.whiteUsername(),
                data.blackUsername(),
                data.gameName(),
                data.game()
        );
        return new GameData(gameID, data.whiteUsername(), data.blackUsername(), data.gameName(), data.game());
    }

    /**
     * @param id The game ID belonging to the desired <code>GameData</code> object.
     *
     * @return The <code>GameData</code> object that has the corresponding <code>id</code>.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    GameData getGame(int id) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (
                    var statement = conn.prepareStatement(
                            """
                             SELECT * FROM game\s
                             WHERE gameID = ?
                            \s"""
                    )
            ) {
                statement.setInt(1, id);

                var resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return new GameData(
                            resultSet.getInt("gameID"),
                            resultSet.getString("whiteUsername"),
                            resultSet.getString("blackUsername"),
                            resultSet.getString("gameName"),
                            Serializer.deserialize(resultSet.getString("game"), ChessGame.class)
                    );
                }
            } catch (NoSuchMethodException |
                     InvocationTargetException |
                     InstantiationException |
                     IllegalAccessException e) {
                throw new DataAccessException(String.format(
                        "Error: failed to deserialize ChessGame object: %s",
                        e.getMessage()
                ));
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: failed to execute query %s", e.getMessage()));
        }
    }

    /**
     * @return A collection of all <code>GameData</code> instances stored in the database.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    Collection<GameData> listGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (
                    var statement = conn.prepareStatement(
                            """
                             SELECT * FROM game\s
                            \s"""
                    )
            ) {
                HashSet<GameData> games = new HashSet<>();

                var resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    games.add(new GameData(
                            resultSet.getInt("gameID"),
                            resultSet.getString("whiteUsername"),
                            resultSet.getString("blackUsername"),
                            resultSet.getString("gameName"),
                            Serializer.deserialize(resultSet.getString("game"), ChessGame.class)
                    ));
                }
                return games;

            } catch (NoSuchMethodException |
                     InvocationTargetException |
                     InstantiationException |
                     IllegalAccessException e) {
                throw new DataAccessException(String.format(
                        "Error: failed to deserialize ChessGame object: %s",
                        e.getMessage()
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: failed to execute query %s", e.getMessage()));
        }

    }

    /**
     * Replaces the <code>GameData</code> instance that shares the <code>gameID</code> of <code>game</code> with the
     * <code>game</code>.
     *
     * @param game The <code>GameData</code> instance to update
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void updateGame(GameData game) throws DataAccessException {
        DatabaseManager.executeUpdate(
                """
                UPDATE game
                SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ?
                WHERE gameID = ?
                """,
                game.whiteUsername(),
                game.blackUsername(),
                game.gameName(),
                game.game(),
                game.gameID()
        );
    }

    /**
     * Empties the game database. There will be no data in the database after this function is run.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void clear() throws DataAccessException {
        DatabaseManager.executeUpdate("TRUNCATE game");
    }
}
