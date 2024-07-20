package handle.user;

import exchange.user.LogoutRequest;
import handle.util.HttpHandler;
import service.user.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

public
class LogoutHandler implements Route {
    @Override
    public
    Object handle(Request request, Response response) throws Exception {
        HttpHandler.handleHttpRoute(request, LogoutRequest.class, new LogoutService(), response);
        return response.body();
    }
}
