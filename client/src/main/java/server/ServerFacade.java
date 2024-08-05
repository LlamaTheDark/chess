package server;

import com.google.gson.Gson;
import exchange.game.CreateGameResponse;
import exchange.game.JoinGameResponse;
import exchange.game.ListGamesResponse;
import exchange.user.*;
import serial.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

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
    throws URISyntaxException, IOException {
        /*
        PREPARE REQUEST
         */
        URI uri = new URI(hostUrl + endpoint);
        var http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);

        /*
        WRITE REQUEST BODY
         */
        if (!body.isEmpty()) {
            http.setDoOutput(true);
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
                response = new Gson().fromJson(inputStreamReader, clazz);
            }

            return response;
        } else {
            return null;
        }
    }

    /*
    Pre-login requests
     */
    public
    LoginResponse login(LoginRequest request) throws URISyntaxException, IOException {
        return (LoginResponse) makeRequest("/session", "POST", Serializer.serialize(request), LoginResponse.class);
    }

    public
    RegisterResponse register(RegisterRequest request) throws URISyntaxException, IOException {
        return (RegisterResponse) makeRequest("/user", "POST", Serializer.serialize(request), RegisterResponse.class);
    }

    /*
    Post-login requests
     */
    public
    LogoutResponse logout() throws URISyntaxException, IOException {
        return (LogoutResponse) makeRequest("/login", "GET", "", LogoutResponse.class);
    }

    public
    CreateGameResponse createGame() throws URISyntaxException, IOException {

        return (CreateGameResponse) makeRequest(
                "/login",
                "GET",
                "",
                CreateGameResponse.class
        );
    }

    public
    ListGamesResponse listGames() throws URISyntaxException, IOException {

        return (ListGamesResponse) makeRequest("/login", "GET", "", ListGamesResponse.class);
    }

    public
    JoinGameResponse joinGame() throws URISyntaxException, IOException {

        return (JoinGameResponse) makeRequest("/login", "GET", "", JoinGameResponse.class);
    }
}

