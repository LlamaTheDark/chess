package exchange.game;

import exchange.Request;

public
class JoinGameRequest extends Request {
    private String playerColor;
    private int    gameID;


    public
    JoinGameRequest(String playerColor, int gameID, String authToken) {
        super(authToken);
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public
    JoinGameRequest() {}

    public
    String getPlayerColor() {
        return playerColor;
    }

    public
    void setPlayerColor(String playerColor) {this.playerColor = playerColor;}

    public
    int getGameID() {
        return gameID;
    }

    public
    void setGameID(int gameID) {this.gameID = gameID;}
}
