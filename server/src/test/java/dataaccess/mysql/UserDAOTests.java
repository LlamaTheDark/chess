package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    @BeforeAll
    static
    void clearAll() throws DataAccessException {
        new MySQLGameDAO().clear();
        new MySQLAuthDAO().clear();
        new MySQLUserDAO().clear();
    }

    @AfterEach
    void clearUserTable() throws DataAccessException {
        dao.clear();
    }

    @Test
    @Order(1)
    @DisplayName("CONSTRUCTOR: Successfully Instantiate MySQLUserDAO Instance")
    void testConstructor() {
        Assertions.assertDoesNotThrow(MySQLUserDAO::new);
    }

    @Test
    @Order(2)
    @DisplayName("+createUser: Add a user to the database")
    void addUser() {
        Assertions.assertDoesNotThrow(() -> {
            new MySQLUserDAO().createUser(new UserData("beans", "poop", "beanspoop@gmail.com"));
        });
    }

    @Test
    @Order(3)
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
    @Order(4)
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
    @Order(5)
    @DisplayName("-getUser: Request non-existent user")
    void requestNullUsername() throws DataAccessException {
        var user = new UserData("beans", "poop", "beanspoop@gmail.com");
        dao.createUser(user);

        String notUserUsername = "steeveewundah";
        var response = dao.getUser(notUserUsername);

        Assertions.assertNull(response);
    }

    @Test
    @Order(6)
    @DisplayName("+clear: Clear Database")
    void clearDatabase() {
        Assertions.assertDoesNotThrow(() -> dao.clear());
    }
}