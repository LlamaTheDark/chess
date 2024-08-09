package server;

import exchange.game.*;
import exchange.user.*;
import serial.Serializer;
import ui.exception.BadParametersException;
import ui.exception.ForbiddenException;
import ui.exception.UIException;
import ui.exception.UnauthorizedException;
import ui.game.GamePlayUI;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public
class ServerFacade {
    private String hostUrl;
    private int    port;

    public
    ServerFacade(String url, int port) {
        hostUrl = url;
        this.port = port;
    }


    public
    ServerFacade() {
        this("http://localhost", 8080);
    }

    <T> Object makeRequest(String endpoint, String method, String body, Class<T> clazz)
    throws UIException {
        /*
        PREPARE REQUEST
         */
        URI uri = null;
        try {
            uri = new URI(hostUrl + ":" + port + endpoint);
            var http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(method);
            http.setRequestProperty("Authorization", SessionHandler.authToken);

        /*
        WRITE REQUEST BODY
         */
            if (!body.isEmpty() && (method.equals("POST") || method.equals("PUT") || method.equals("DELETE"))) {
                http.setDoOutput(true); // implicitly sets request method to "POST"
                try (var writer = http.getOutputStream()) {
                    writer.write(body.getBytes());
                }
            }

        /*
        SEND REQUEST
         */
            http.connect();

        /*
        RECEIVE AND PARSE RESPONSE
         */
            var statusCode = http.getResponseCode();

            if (statusCode == 200) {
                Object response;
                try (InputStream respBody = http.getInputStream()) {
                    var inputStreamReader = new InputStreamReader(respBody);
                    response = Serializer.deserialize(inputStreamReader, clazz);
                }

                http.disconnect();

                return response;
            } else if (statusCode == 400) {
                throw new BadParametersException();
            } else if (statusCode == 401) {
                throw new UnauthorizedException();
            } else if (statusCode == 403) {
                throw new ForbiddenException();
            } else {
                throw new UIException("something has gone terribly wrong.");
            }
        } catch (URISyntaxException |
                 IOException |
                 NoSuchMethodException |
                 InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException e) {
            throw new UIException("failed to connect to server!");
        }
    }

    /*
    Pre-login requests
     */
    public
    LoginResponse login(LoginRequest request) throws UIException {
        return (LoginResponse) makeRequest("/session", "POST", Serializer.serialize(request), LoginResponse.class);
    }

    public
    RegisterResponse register(RegisterRequest request) throws UIException {
        return (RegisterResponse) makeRequest("/user", "POST", Serializer.serialize(request), RegisterResponse.class);
    }

    /*
    Post-login requests
     */
    public
    LogoutResponse logout(LogoutRequest request) throws UIException {
        return (LogoutResponse) makeRequest("/session", "DELETE", Serializer.serialize(request), LogoutResponse.class);
    }

    public
    CreateGameResponse createGame(CreateGameRequest request) throws UIException {
        return (CreateGameResponse) makeRequest(
                "/game",
                "POST",
                Serializer.serialize(request),
                CreateGameResponse.class
        );
    }

    public
    ListGamesResponse listGames(ListGamesRequest request) throws UIException {
        return (ListGamesResponse) makeRequest("/game", "GET", Serializer.serialize(request), ListGamesResponse.class);
    }

    public
    JoinGameResponse joinGame(JoinGameRequest request) throws UIException {
        GamePlayUI.play();
        return (JoinGameResponse) makeRequest("/game", "PUT", Serializer.serialize(request), JoinGameResponse.class);
    }

    public
    void observeGame() {
        GamePlayUI.observe();
    }
}

