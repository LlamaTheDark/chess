package exchange.game;

import exchange.Request;

public class CreateGameRequest extends Request {
    String gameName;
    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }
}
