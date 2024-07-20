package handle.user;

import exchange.user.RegisterRequest;
import handle.HttpHandler;
import service.user.RegisterService;
import spark.Request;
import spark.Response;
import spark.Route;

public
class RegisterHandler implements Route {
    @Override
    public
    Object handle(Request request, Response response) {
        HttpHandler.handleHttpRoute(request, RegisterRequest.class, new RegisterService(), response);

        return response.body();
    }
}
