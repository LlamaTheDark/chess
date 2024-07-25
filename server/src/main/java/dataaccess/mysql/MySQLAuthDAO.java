package dataaccess.mysql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.sql.SQLException;

public
class MySQLAuthDAO implements AuthDAO {

    private final static String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
                authToken VARCHAR(255) NOT NULL PRIMARY KEY,
                username VARCHAR(255) NOT NULL
            )
            """
    };

    public
    MySQLAuthDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    /**
     * Stores the passed <code>AuthData</code> in the database.
     *
     * @param authData The object to be stored.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void createAuth(AuthData authData) throws DataAccessException {
        DatabaseManager.executeUpdate("INSERT INTO auth VALUES (?, ?)", authData.authToken(), authData.username());
    }

    /**
     * @param authToken The authToken whose <code>AuthData</code> is desired.
     *
     * @return The first <code>AuthData</code> instance stored that has the specified authorization token.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    AuthData getAuthByToken(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (
                    var statement = conn.prepareStatement(
                            """
                             SELECT authToken, username FROM auth\s
                             WHERE authToken = ?
                            \s"""
                    )
            ) {
                statement.setString(1, authToken);

                var resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return new AuthData(
                            resultSet.getString("authToken"),
                            resultSet.getString("username")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes the <code>AuthData</code> instance which has the corresponding authorization token from the database.
     *
     * @param auth The authorization token whose <code>AuthData</code> instance is to be removed from the database.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void deleteAuth(String auth) throws DataAccessException {
        DatabaseManager.executeUpdate("DELETE FROM auth WHERE authToken = ?", auth);
    }

    /**
     * Empties the authorization database. There will be no data in the database after this function is run.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void clear() throws DataAccessException {
        DatabaseManager.executeUpdate("TRUNCATE auth");
    }
}
