package handle.dev;

import exchange.dev.ClearApplicationRequest;
import handle.HttpHandler;
import service.dev.ClearApplicationService;
import spark.Request;
import spark.Response;
import spark.Route;

public
class ClearApplicationHandler implements Route {

    @Override
    public
    Object handle(Request request, Response response) {
        HttpHandler.handleHttpRoute(request, ClearApplicationRequest.class, new ClearApplicationService(), response);
        return response.body();
    }
}
