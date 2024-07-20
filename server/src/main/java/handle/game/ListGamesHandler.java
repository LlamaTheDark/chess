package handle.game;

import exchange.game.ListGamesRequest;
import handle.HttpHandler;
import service.game.ListGamesService;
import spark.Request;
import spark.Response;
import spark.Route;

public
class ListGamesHandler implements Route {
    @Override
    public
    Object handle(Request request, Response response) throws Exception {
        HttpHandler.handleHttpRoute(request, ListGamesRequest.class, new ListGamesService(), response);
        return response.body();
    }
}
