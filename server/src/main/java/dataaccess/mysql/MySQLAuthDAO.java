package dataaccess.mysql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public
class MySQLAuthDAO implements AuthDAO {
    /**
     * Stores the passed <code>AuthData</code> in the database.
     *
     * @param authData The object to be stored.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void createAuth(AuthData authData) throws DataAccessException {

    }

    /**
     * @param username The username whose <code>AuthData</code> is desired.
     *
     * @return The first <code>AuthData</code> instance stored that has the specified username.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    AuthData getAuthByUsername(String username) throws DataAccessException {
        return null;
    }

    /**
     * @param authToken The authToken whose <code>AuthData</code> is desired.
     *
     * @return The first <code>AuthData</code> instance stored that has the specified authorization token.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    AuthData getAuthByToken(String authToken) throws DataAccessException {
        return null;
    }

    /**
     * Removes the <code>AuthData</code> instance which has the corresponding authorization token from the database.
     *
     * @param auth The authorization token whose <code>AuthData</code> instance is to be removed from the database.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void deleteAuth(String auth) throws DataAccessException {

    }

    /**
     * Empties the authorization database. There will be no data in the database after this function is run.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void clear() throws DataAccessException {

    }
}
