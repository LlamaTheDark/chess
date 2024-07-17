package handle;

import handle.util.Serializer;
import service.Service;

/**
 * Handler to reduce code duplication, seeing as most handler classes reuse the same basic algorithm.
 */
public class HttpHandler {
    public static
    <P extends exchange.Response, Q extends exchange.Request>
    void handleHttpRequest( spark.Request request, Class<Q> requestClass, Service<P,Q> service, spark.Response response ) {
        var exchangeResponse = service.serve(
                Serializer.deserialize(request.body(), requestClass)
        );
        response.type("text/json");
        response.body(
                Serializer.serialize(
                        exchangeResponse
                )
        );
        response.status(exchangeResponse.getStatusCode());
    }
}
