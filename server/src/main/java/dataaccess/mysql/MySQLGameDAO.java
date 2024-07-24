package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;
import java.util.List;

public
class MySQLGameDAO implements GameDAO {
    /**
     * Stores a <code>GameData</code> instance in the database.
     *
     * @param data The <code>GameData</code> instance to be stored.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void createGame(GameData data) throws DataAccessException {

    }

    /**
     * @param id The game ID belonging to the desired <code>GameData</code> object.
     *
     * @return The <code>GameData</code> object that has the corresponding <code>id</code>.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    GameData getGame(int id) throws DataAccessException {
        return null;
    }

    /**
     * @return A collection of all <code>GameData</code> instances stored in the database.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    /**
     * Replaces the <code>GameData</code> instance that shares the <code>gameID</code> of <code>game</code> with the
     * <code>game</code>.
     *
     * @param game The <code>GameData</code> instance to update
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void updateGame(GameData game) throws DataAccessException {

    }

    /**
     * Empties the game database. There will be no data in the database after this function is run.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    @Override
    public
    void clear() throws DataAccessException {

    }
}
