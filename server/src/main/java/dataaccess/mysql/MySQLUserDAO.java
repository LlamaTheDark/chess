package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

public
class MySQLUserDAO implements UserDAO {
    /**
     * Stores a <code>UserData</code> instance in the database.
     *
     * @param userData The <code>UserData</code> instance to be stored.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void createUser(UserData userData) throws DataAccessException {

    }

    /**
     * @param username The username of the <code>UserData</code> instance to be returned.
     *
     * @return The <code>UserData</code> instance containing <code>username</code>.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    UserData getUser(String username) throws DataAccessException {
        return null;
    }

    /**
     * Empties the user database. There will be no data in the database after this function is run.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void clear() throws DataAccessException {

    }
}
