package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

/**
 * Implements the {@link AuthDAO} interface using a local-memory database.
 */
public
class MemoryAuthDAO implements AuthDAO {
    /**
     * Maps <code>String</code> authToken to an <code>AuthData</code> object.
     */
    static final private Map<String, AuthData> AUTH = new HashMap<>();

    @Override
    public
    void createAuth(AuthData authData) throws DataAccessException {
        AUTH.put(authData.authToken(), authData);
    }

    @Override
    public
    AuthData getAuthByUsername(String username) throws DataAccessException {
        for (Map.Entry<String, AuthData> entry : AUTH.entrySet()) {
            if (entry.getValue().username().equals(username)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public
    AuthData getAuthByToken(String authToken) throws DataAccessException {
        if (AUTH.containsKey(authToken)) {
            return AUTH.get(authToken);
        }
        return null;
    }

    @Override
    public
    void deleteAuth(String auth) throws DataAccessException {
        AUTH.remove(auth);
    }

    @Override
    public
    void clear() throws DataAccessException {
        AUTH.clear();
    }
}
