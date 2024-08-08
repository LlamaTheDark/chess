package handle.util;

import dataaccess.DataAccessException;
import serial.Serializer;
import service.Service;
import service.exception.BadRequestException;
import service.exception.ForbiddenException;
import service.exception.ServiceException;
import service.exception.UnauthorizedException;

import java.lang.reflect.InvocationTargetException;

/**
 * Handler to reduce code duplication, seeing as most handler classes reuse the same basic algorithm.
 */
public final
class HttpHandler {
    public static
    <P extends exchange.Response, Q extends exchange.Request>
    void handleHttpRoute(spark.Request request, Class<Q> requestClass, Service<P, Q> service, spark.Response response) {
        try {

            var deserializedRequest = Serializer.deserialize(request.body(), requestClass);
            String authorization = request.headers("Authorization");
            if (authorization != null) {
                deserializedRequest.setAuthToken(authorization);
            }

            var exchangeResponse = service.serve(deserializedRequest);

            response.type("text/json");
            response.body(
                    Serializer.serialize(
                            exchangeResponse
                    )
            );
            // default response code is 200 OK because if something went wrong, we'd be in an exception right now
            response.status(200);

        } catch (BadRequestException bre) {
            response.status(400);
            response.body("{\"message\": \"" + bre.getMessage() + "\"}");
        } catch (UnauthorizedException ue) {
            response.status(401);
            response.body("{\"message\": \"" + ue.getMessage() + "\"}");
        } catch (ForbiddenException fe) {
            response.status(403);
            response.body("{\"message\": \"" + fe.getMessage() + "\"}");
        } catch (ServiceException |
                 DataAccessException |
                 NoSuchMethodException |
                 InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException se) {
            response.status(500);
            response.body("{\"message\": \"" + se.getMessage() + "\"}");
        }
    }
}
