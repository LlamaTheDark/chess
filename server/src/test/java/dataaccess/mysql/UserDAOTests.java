package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public
class UserDAOTests {
    static
    private UserDAO dao;

    static {
        try {
            dao = new MySQLUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void clearUserTable() throws DataAccessException {
        dao.clear();
    }

    @Test
    @DisplayName("CONSTRUCTOR: Successfully Instantiate MySQLUserDAO Instance")
    void testConstructor() {
        Assertions.assertDoesNotThrow(MySQLUserDAO::new);
    }

    @Test
    @DisplayName("+createUser: Add a user to the database")
    void addUser() {
        Assertions.assertDoesNotThrow(() -> {
            new MySQLUserDAO().createUser(new UserData("beans", "poop", "beanspoop@gmail.com"));
        });
    }

    @Test
    @DisplayName("-createUser: Add the same user twice")
    void addSameUserTwice() throws DataAccessException {
        dao.createUser(new UserData("beans", "poop", "beanspoop@gmail.com"));
        Assertions.assertThrows(
                DataAccessException.class,
                () -> dao.createUser(
                        new UserData("beans", "poop", "beanspoop@gmail.com")
                )
        );
    }

    @Test
    @DisplayName("+getUser: Get User From Database")
    void getUserFromDatabase() throws DataAccessException {
        var expectedUser = new UserData("beans", "poop", "beanspoop@gmail.com");
        dao.createUser(expectedUser);
        var actualUser = dao.getUser("beans");

        Assertions.assertEquals(expectedUser.username(), actualUser.username());
        Assertions.assertEquals(expectedUser.password(), actualUser.password());
        Assertions.assertEquals(expectedUser.email(), actualUser.email());
    }

    @Test
    @DisplayName("-getUser: Request non-existent user")
    void requestNullUsername() throws DataAccessException {
        var user = new UserData("beans", "poop", "beanspoop@gmail.com");
        dao.createUser(user);

        String notUserUsername = "steeveewundah";
        var response = dao.getUser(notUserUsername);

        Assertions.assertNull(response);
    }

    @Test
    @DisplayName("+clear: Clear Database")
    void clearDatabase() {
        Assertions.assertDoesNotThrow(() -> dao.clear());
    }
}