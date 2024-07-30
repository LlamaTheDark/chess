package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.sql.SQLException;

public
class MySQLUserDAO implements UserDAO {

    private final static String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
                username VARCHAR(255) NOT NULL PRIMARY KEY,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL
            )
            """
    };

    public
    MySQLUserDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    /**
     * Stores a <code>UserData</code> instance in the database.
     *
     * @param userData The <code>UserData</code> instance to be stored.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void createUser(UserData userData) throws DataAccessException {
        DatabaseManager.executeUpdate(
                "INSERT INTO user (username, password, email) VALUES (?, ?, ?)",
                userData.username(),
                userData.password(),
                userData.email()
        );
    }

    /**
     * @param username The username of the <code>UserData</code> instance to be returned.
     *
     * @return The <code>UserData</code> instance containing <code>username</code>.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (
                    var statement = conn.prepareStatement(
                            """
                            SELECT username, password, email FROM user
                            WHERE username = ?
                            """)
            ) {
                statement.setString(1, username);

                var resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return new UserData(
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("email")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: failed to execute query: %s", e.getMessage()));
        }
    }

    /**
     * Empties the user database. There will be no data in the database after this function is run.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void clear() throws DataAccessException {
        DatabaseManager.executeUpdate("DELETE FROM user");
    }
}
