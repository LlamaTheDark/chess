package server;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public final
class SessionHandler {
    public static String              authToken = "";
    public static ArrayList<GameData> games     = new ArrayList<>();

    public static
    void setGameListMemory(Collection<GameData> listedGames) {
        games.clear();
        games.addAll(listedGames);
    }

    public static
    int getGameIDFromIndex(int index) {
        if (index > games.size()) {
            throw new IllegalArgumentException();
        }
        return games.get(index - 1).gameID();
    }
}
