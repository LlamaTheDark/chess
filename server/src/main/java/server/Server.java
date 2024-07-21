package server;

import handle.dev.ClearApplicationHandler;
import handle.game.CreateGameHandler;
import handle.game.JoinGameHandler;
import handle.game.ListGamesHandler;
import handle.user.LoginHandler;
import handle.user.LogoutHandler;
import handle.user.RegisterHandler;
import spark.Spark;

public
class Server {

    public
    int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        registerUserEndpoints();
        registerGameEndpoints();
        registerDevEndpoints();

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private
    void registerUserEndpoints() {
        Spark.post("/user", new RegisterHandler());
        Spark.post("/session", new LoginHandler());
        Spark.delete("/session", new LogoutHandler());
    }

    private
    void registerGameEndpoints() {
        Spark.post("/game", new CreateGameHandler());
        Spark.put("/game", new JoinGameHandler());
        Spark.get("/game", new ListGamesHandler());
    }

    private
    void registerDevEndpoints() {
        Spark.delete("/db", new ClearApplicationHandler());
    }

    public
    void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
