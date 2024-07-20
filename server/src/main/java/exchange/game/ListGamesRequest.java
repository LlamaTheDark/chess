package exchange.game;

import exchange.Request;

public class ListGamesRequest extends Request {
    public ListGamesRequest(String authToken) {
        super(authToken);
    }
}
