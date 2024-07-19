package dataaccess.memory;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    /**
     * Maps <code>int</code> gameID to <code>GameData</code>.
     */
    private static final HashMap<Integer, GameData> GAME = new HashMap<>();
    private static int nextID = 1;

    @Override
    public void createGame(GameData data) throws DataAccessException {
        GAME.put(data.gameID(), data);
    }

    @Override
    public ChessGame getGame() throws DataAccessException {
        return null;
    }

    @Override
    public Collection<ChessGame> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void updateGame() throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        nextID = 1;
        GAME.clear();
    }

    public static int getNextID() {
        return nextID++;
    }
}
