package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void createGame(GameData data) throws DataAccessException;
    ChessGame getGame() throws DataAccessException;
    Collection<ChessGame> listGames() throws DataAccessException;
    void updateGame() throws DataAccessException;

    void clear() throws DataAccessException;
}
