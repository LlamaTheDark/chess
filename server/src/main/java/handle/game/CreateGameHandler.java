package handle.game;

import exchange.game.CreateGameRequest;
import handle.util.HttpHandler;
import service.game.CreateGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public
class CreateGameHandler implements Route {
    @Override
    public
    Object handle(Request request, Response response) throws Exception {
        HttpHandler.handleHttpRoute(request, CreateGameRequest.class, new CreateGameService(), response);
        System.out.println("CreateGameHandler called");
        return response.body();
    }
}
