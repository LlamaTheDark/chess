package exchange.game;

import exchange.Response;
import model.GameData;

import java.util.Collection;

public
class ListGamesResponse implements Response {
    private Collection<GameData> games;

    public
    ListGamesResponse() {}

    public
    ListGamesResponse(Collection<GameData> games) {
        this.games = games;
    }
}
