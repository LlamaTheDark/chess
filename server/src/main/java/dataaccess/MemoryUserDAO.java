package dataaccess;

import model.UserData;
import java.util.HashMap;

/*
Figure out when the heck you're supposed to throw those exceptions.
*/

public class MemoryUserDAO implements UserDAO {
    private static final HashMap<String, UserData> USERS = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        USERS.clear();
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        USERS.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if(!USERS.containsKey(username)) {
            return null;
        }
        return USERS.get(username);
    }

    public UserData getUser(String username, String password) throws DataAccessException {
        if(USERS.containsKey(username) && USERS.get(username).password().equals(password)){
            return USERS.get(username);
        }
        return null;
    }
}
