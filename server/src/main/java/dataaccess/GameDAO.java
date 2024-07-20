package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void createGame(GameData data) throws DataAccessException;
    GameData getGame(int id) throws DataAccessException;
    Collection<ChessGame> listGames() throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;

    void clear() throws DataAccessException;
}
