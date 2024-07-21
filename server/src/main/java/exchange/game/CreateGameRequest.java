package exchange.game;

import exchange.Request;

public
class CreateGameRequest extends Request {
    String gameName;

    public
    CreateGameRequest(String gameName, String authToken) {
        super(authToken);
        this.gameName = gameName;
    }

    public
    CreateGameRequest() {}

    public
    String getGameName() {
        return gameName;
    }

    public
    void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
