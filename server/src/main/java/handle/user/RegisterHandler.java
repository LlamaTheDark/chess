package handle.user;

import exchange.user.RegisterRequest;
import service.user.RegisterService;
import handle.util.Serializer;
import spark.*;

public class RegisterHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var registerResponse = new RegisterService().serve(
                Serializer.deserialize(request.body(), RegisterRequest.class)
        );
        response.type("text/json");
        response.body(
                Serializer.serialize(
                        registerResponse
                )
        );
        response.status(registerResponse.getStatusCode());
        return response.body();
    }
}
