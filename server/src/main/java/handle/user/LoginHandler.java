package handle.user;

import exchange.user.LoginRequest;
import handle.HttpHandler;
import service.user.LoginService;
import spark.Request;
import spark.Response;
import spark.Route;

public
class LoginHandler implements Route {
    @Override
    public
    Object handle(Request request, Response response) {
        HttpHandler.handleHttpRoute(request, LoginRequest.class, new LoginService(), response);

        return response.body();
    }
}
