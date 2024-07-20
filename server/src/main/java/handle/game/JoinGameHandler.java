package handle.game;

import exchange.game.JoinGameRequest;
import handle.HttpHandler;
import service.game.JoinGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public
class JoinGameHandler implements Route {
    @Override
    public
    Object handle(Request request, Response response) throws Exception {
        HttpHandler.handleHttpRoute(request, JoinGameRequest.class, new JoinGameService(), response);
        return response.body();
    }
}
