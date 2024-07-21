package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.util.HashMap;

/**
 * Implements the {@link UserDAO} interface using a local-memory database.
 */
public
class MemoryUserDAO implements UserDAO {
    private static final HashMap<String, UserData> USERS = new HashMap<>();

    @Override
    public
    void createUser(UserData userData) throws DataAccessException {
        USERS.put(userData.username(), userData);
    }

    @Override
    public
    UserData getUser(String username) throws DataAccessException {
        if (!USERS.containsKey(username)) {
            return null;
        }
        return USERS.get(username);
    }

    @Override
    public
    void clear() throws DataAccessException {
        USERS.clear();
    }

    public
    UserData getUser(String username, String password) throws DataAccessException {
        if (USERS.containsKey(username) && USERS.get(username).password().equals(password)) {
            return USERS.get(username);
        }
        return null;
    }
}
