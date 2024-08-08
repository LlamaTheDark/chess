package handle.dev;

import exchange.halfduplex.dev.ClearApplicationRequest;
import handle.util.HttpHandler;
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
