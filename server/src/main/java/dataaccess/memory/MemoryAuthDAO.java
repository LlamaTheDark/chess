package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    Map<String, AuthData> AUTH = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        AUTH.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String username) throws DataAccessException {
        for(Map.Entry<String, AuthData> entry : AUTH.entrySet()) {
            if(entry.getValue().username().equals(username)){
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(String auth) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        AUTH.clear();
    }
}
