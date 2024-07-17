package dataaccess;

import chess.ChessGame;

import java.util.Collection;

public interface GameDAO {
    void createGame();
    ChessGame getGame();
    Collection<ChessGame> listGames();
    void updateGame();

    void clear();
}
