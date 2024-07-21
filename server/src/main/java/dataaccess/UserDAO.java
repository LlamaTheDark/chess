package dataaccess;

import model.UserData;

public
interface UserDAO {

    /**
     * Stores a <code>UserData</code> instance in the database.
     *
     * @param userData The <code>UserData</code> instance to be stored.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    void createUser(UserData userData) throws DataAccessException;

    /**
     * @param username The username of the <code>UserData</code> instance to be returned.
     *
     * @return The <code>UserData</code> instance containing <code>username</code>.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    UserData getUser(String username) throws DataAccessException;


    /**
     * Empties the user database. There will be no data in the database after this function is run.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    void clear() throws DataAccessException;
}
