package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth() throws DataAccessException;
    void deleteAuth(String auth) throws DataAccessException;
}
