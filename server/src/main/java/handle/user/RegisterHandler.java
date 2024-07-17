package handle.user;

import exchange.user.RegisterRequest;
import service.user.RegisterService;
import spark.*;

import handle.HttpHandler;

public class RegisterHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        HttpHandler.handleHttpRoute(request, RegisterRequest.class, new RegisterService(), response);

        return response.body();
    }
}
