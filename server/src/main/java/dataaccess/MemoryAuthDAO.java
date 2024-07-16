package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    Map<String, String> AUTH = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        AUTH.put(authData.authToken(), authData.username());
    }

    @Override
    public AuthData getAuth() throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String auth) throws DataAccessException {

    }
}
