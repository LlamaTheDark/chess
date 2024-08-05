package server;

import exchange.game.*;
import exchange.user.*;
import serial.Serializer;
import ui.GamePlayUI;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

public
class ServerFacade {
    String hostUrl;

    public
    ServerFacade(String url) {
        this.hostUrl = url;
    }

    public
    ServerFacade() {
        this("http://localhost:8080");
    }

    <T> Object makeRequest(String endpoint, String method, String body, Class<T> clazz)
    throws Exception {
        /*
        PREPARE REQUEST
         */
        URI uri = new URI(hostUrl + endpoint);
        var http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);
        http.setRequestProperty("Authorization", SessionHandler.authToken);

        /*
        WRITE REQUEST BODY
         */
        if (!body.isEmpty() && method.equals("POST")) {
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

        /*
        TODO: allow program to print an error message based on the status code (unauthorized, bad request, etc.)
         */
        if (statusCode == 200) {
            Object response = "";
            try (InputStream respBody = http.getInputStream()) {
                var inputStreamReader = new InputStreamReader(respBody);
                response = Serializer.deserialize(inputStreamReader, clazz);
            }

            http.disconnect();

            return response;
        } else {
            throw new Exception("aaaaah");
        }
    }

    /*
    Pre-login requests
     */
    public
    LoginResponse login(LoginRequest request) throws Exception {
        return (LoginResponse) makeRequest("/session", "POST", Serializer.serialize(request), LoginResponse.class);
    }

    public
    RegisterResponse register(RegisterRequest request) throws Exception {
        return (RegisterResponse) makeRequest("/user", "POST", Serializer.serialize(request), RegisterResponse.class);
    }

    /*
    Post-login requests
     */
    public
    LogoutResponse logout(LogoutRequest request) throws Exception {
        return (LogoutResponse) makeRequest("/session", "DELETE", Serializer.serialize(request), LogoutResponse.class);
    }

    public
    CreateGameResponse createGame(CreateGameRequest request) throws Exception {
        return (CreateGameResponse) makeRequest(
                "/game",
                "POST",
                Serializer.serialize(request),
                CreateGameResponse.class
        );
    }

    public
    ListGamesResponse listGames(ListGamesRequest request) throws Exception {
        return (ListGamesResponse) makeRequest("/game", "GET", Serializer.serialize(request), ListGamesResponse.class);
    }

    public
    JoinGameResponse joinGame(JoinGameRequest joinGameRequest) throws Exception {
        GamePlayUI.play();
        return null;
    }

    public
    void observeGame(int gameID) {
        GamePlayUI.observe();
    }
}
