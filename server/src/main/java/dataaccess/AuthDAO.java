package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuthByUsername(String username) throws DataAccessException;
    AuthData getAuthByToken(String authToken) throws DataAccessException;
    void deleteAuth(String auth) throws DataAccessException;

    void clear() throws DataAccessException;
}
