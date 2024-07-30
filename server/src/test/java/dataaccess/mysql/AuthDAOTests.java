package dataaccess.mysql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public
class AuthDAOTests {
    static
    private final AuthDAO DAO;

    static {
        try {
            DAO = new MySQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void clearUserTable() throws DataAccessException {
        DAO.clear();
    }

    @Test
    @DisplayName("CONSTRUCTOR: Successful Initialization")
    void testConstructor() {
        Assertions.assertDoesNotThrow(MySQLAuthDAO::new);
    }

    @Test
    @DisplayName("+createAuth: Successfully add AuthData to table")
    void addAuthData() {
        Assertions.assertDoesNotThrow(() -> DAO.createAuth(new AuthData(
                "wheeeeeeeeeImanAuthTOken",
                "gooseboy"
        )));
    }

    @Test
    @DisplayName("-createAuth: Attempt to add the same auth twice")
    void addAuthTwice() throws DataAccessException {
        var authData = new AuthData("wheeeeeeeeImanAuthTOken", "gooseboy");
        DAO.createAuth(authData);
        Assertions.assertThrows(DataAccessException.class, () -> DAO.createAuth(authData));
    }

    @Test
    @DisplayName("+getAuthByToken: Get an auth from the table")
    void getAuthByToken() throws DataAccessException {
        var expectedAuthData = new AuthData("wheeeeeeeeImanAuthTOken", "gooseboy");
        DAO.createAuth(expectedAuthData);

        Assertions.assertDoesNotThrow(() -> DAO.getAuthByToken("wheeeeeeeeImanAuthTOken"));
        var actualData = DAO.getAuthByToken("wheeeeeeeeImanAuthTOken");

        Assertions.assertEquals(expectedAuthData.authToken(), actualData.authToken());
        Assertions.assertEquals(expectedAuthData.username(), actualData.username());
    }

    @Test
    @DisplayName("+deleteAuth: Delete an auth from the table")
    void deleteAuth() throws DataAccessException {
        var authData = new AuthData("wheeeeeeeeImanAuthTOken", "gooseboy");
        DAO.createAuth(authData);
        Assertions.assertDoesNotThrow(() -> DAO.deleteAuth("wheeeeeeeeImanAuthTOken"));

        Assertions.assertNull(DAO.getAuthByToken("wheeeeeeeeImanAuthTOken"));
    }

    @Test
    @DisplayName("+clear")
    void clearTest() {
        Assertions.assertDoesNotThrow(DAO::clear);
    }
}
