package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

/**
 * Implements the {@link GameDAO} interface using a local-memory database.
 */
public
class MemoryGameDAO implements GameDAO {
    /**
     * Maps <code>int</code> gameID to <code>GameData</code>.
     */
    private static final HashMap<Integer, GameData> GAME   = new HashMap<>();
    private static       int                        nextID = 1;

    public static
    int getNextID() {
        return nextID++;
    }

    @Override
    public
    GameData createGame(GameData data) throws DataAccessException {
        GAME.put(data.gameID(), data);
        return null;
    }

    @Override
    public
    GameData getGame(int id) {
        return GAME.get(id);
    }

    @Override
    public
    Collection<GameData> listGames() {
        return GAME.values();
    }

    @Override
    public
    void updateGame(GameData data) {
        GAME.put(data.gameID(), new GameData(
                data.gameID(), data.whiteUsername(), data.blackUsername(), data.gameName(), data.game()
        ));
    }

    @Override
    public
    void clear() throws DataAccessException {
        nextID = 1;
        GAME.clear();
    }
}
