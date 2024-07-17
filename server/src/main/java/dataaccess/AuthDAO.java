package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth(String authData) throws DataAccessException;
    void deleteAuth(String auth) throws DataAccessException;

    void clear() throws DataAccessException;
}
