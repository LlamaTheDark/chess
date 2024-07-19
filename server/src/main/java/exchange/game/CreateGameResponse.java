package exchange.game;

import exchange.Response;

public class CreateGameResponse extends Response {
    int gameID;
    public CreateGameResponse(int gameID) {
        this.gameID = gameID;
    }
}
