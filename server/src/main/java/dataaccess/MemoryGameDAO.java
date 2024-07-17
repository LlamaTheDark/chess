package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    private static final HashMap<String, GameData> GAME = new HashMap<>();

    @Override
    public void createGame() {

    }

    @Override
    public ChessGame getGame() {
        return null;
    }

    @Override
    public Collection<ChessGame> listGames() {
        return List.of();
    }

    @Override
    public void updateGame() {

    }
}
