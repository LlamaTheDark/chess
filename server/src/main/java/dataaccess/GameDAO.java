package dataaccess;

import model.GameData;

import java.util.Collection;

public
interface GameDAO {
    /**
     * Stores a <code>GameData</code> instance in the database.
     *
     * @param data The <code>GameData</code> instance to be stored.
     *
     * @return The <code>GameData</code> instance that was created, which now contains the <code>gameID</code>.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    GameData createGame(GameData data) throws DataAccessException;

    /**
     * @param id The game ID belonging to the desired <code>GameData</code> object.
     *
     * @return The <code>GameData</code> object that has the corresponding <code>id</code>.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    GameData getGame(int id) throws DataAccessException;

    /**
     * @return A collection of all <code>GameData</code> instances stored in the database.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    Collection<GameData> listGames() throws DataAccessException;

    /**
     * Replaces the <code>GameData</code> instance that shares the <code>gameID</code> of <code>game</code> with the
     * <code>game</code>.
     *
     * @param game The <code>GameData</code> instance to update
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    void updateGame(GameData game) throws DataAccessException;


    /**
     * Empties the game database. There will be no data in the database after this function is run.
     *
     * @throws DataAccessException if there is a failure to access the data.
     */
    void clear() throws DataAccessException;
}
