package handle;

import dataaccess.DataAccessException;
import handle.util.Serializer;
import service.Service;
import service.ServiceException;

/**
 * Handler to reduce code duplication, seeing as most handler classes reuse the same basic algorithm.
 */
public final class HttpHandler {
    public static
    <P extends exchange.Response, Q extends exchange.Request>
    void handleHttpRoute(spark.Request request, Class<Q> requestClass, Service<P,Q> service, spark.Response response) {
        try{

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

        } catch (DataAccessException dae){
            response.status(500);
            response.body("{\"message\": " + dae.getMessage() + "}");
        } catch (ServiceException se){
            response.status(se.getStatusCode());
            response.body("{\"message\": " + se.getMessage() + "}");
        }
    }
}
