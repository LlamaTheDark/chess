package handle.user;

import exchange.user.LoginRequest;
import handle.HttpHandler;
import service.user.*;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
//        var loginResponse = new LoginService().serve(
//                Serializer.deserialize(request.body(), LoginRequest.class)
//        );
//        response.type("text/json");
//        response.body(
//                Serializer.serialize(
//                        loginResponse
//                )
//        );
//        response.status(loginResponse.getStatusCode());
        HttpHandler.handleHttpRequest(request, LoginRequest.class, new LoginService(), response);

        return response.body();
    }
}
