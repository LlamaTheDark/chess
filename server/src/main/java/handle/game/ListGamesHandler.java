package handle.game;

import exchange.game.ListGamesRequest;
import handle.util.HttpHandler;
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
        System.out.println("ListGameHandler called");
        return response.body();
    }
}
