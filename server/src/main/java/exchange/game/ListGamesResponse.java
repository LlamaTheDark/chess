package exchange.game;

import exchange.Response;
import model.GameData;

import java.util.Collection;

public
class ListGamesResponse implements Response {
    private Collection<GameData> games;

    public
    ListGamesResponse(Collection<GameData> games) {
        this.games = games;
    }

    public
    ListGamesResponse() {}
}
