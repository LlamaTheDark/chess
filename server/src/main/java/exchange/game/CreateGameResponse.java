package exchange.game;

import exchange.Response;

public
class CreateGameResponse implements Response {
    private int gameID;

    public
    CreateGameResponse(int gameID) {
        this.gameID = gameID;
    }

    public
    int getGameID() {return gameID;}
}
