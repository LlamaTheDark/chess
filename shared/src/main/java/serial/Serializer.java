package serial;

import chess.ChessGame;
import chess.ChessMove;
import chess.rule.ChessRuleBook;
import chess.rule.FIDERuleBook;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import websocket.commands.*;
import websocket.commands.UserGameCommand.CommandType;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessage.ServerMessageType;

import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

/**
 * Serializes and deserializes JSON objects using <a href="https://github.com/google/gson">Google's Gson Library</a>.
 */
public
class Serializer {
    private static Gson gameGson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(
                    ChessRuleBook.class,
                    (JsonDeserializer<ChessRuleBook>)
                            (jsonElement, type, context) -> new FIDERuleBook()
            ).create();
    static         Gson gson     = new GsonBuilder()
            .registerTypeAdapter(
                    UserGameCommand.class,
                    (JsonDeserializer<UserGameCommand>)
                            (jsonElement, type, context) -> {
                                if (jsonElement.getAsJsonObject().has("commandType")) {
                                    var jsonObject = jsonElement.getAsJsonObject();
                                    CommandType commandType =
                                            CommandType.valueOf(jsonObject.get("commandType").getAsString());
                                    String authToken = jsonObject.get("authToken").getAsString();
                                    Integer gameID = jsonObject.get("gameID").getAsInt();
                                    return switch (commandType) {
                                        case CONNECT -> new ConnectCommand(commandType, authToken, gameID);
                                        case MAKE_MOVE -> new MakeMoveCommand(
                                                commandType,
                                                authToken,
                                                gameID,
                                                new Gson().fromJson(jsonObject.get("move"), ChessMove.class)
                                        );
                                        case LEAVE -> new LeaveGameCommand(
                                                commandType,
                                                authToken,
                                                gameID
                                        );
                                        case RESIGN -> new ResignGameCommand(commandType, authToken, gameID);
                                    };
                                } else {
                                    return null;
                                }
                            }
            )
            .registerTypeAdapter(
                    ServerMessage.class,
                    (JsonDeserializer<ServerMessage>)
                            (jsonElement, type, context) -> {
                                if (jsonElement.getAsJsonObject().has("serverMessageType")) {
                                    var jsonObject = jsonElement.getAsJsonObject();
                                    ServerMessageType serverMessageType =
                                            ServerMessageType.valueOf(jsonObject.get("serverMessageType")
                                                                                .getAsString());
                                    return switch (serverMessageType) {
                                        case LOAD_GAME -> new LoadGameMessage(
                                                serverMessageType,
                                                gameGson.fromJson(jsonObject.get("game"), ChessGame.class)
                                        );
                                        case ERROR -> new ErrorMessage(
                                                serverMessageType,
                                                jsonObject.get("errorMessage").getAsString()
                                        );
                                        case NOTIFICATION -> new NotificationMessage(
                                                serverMessageType,
                                                jsonObject.get("message").getAsString()
                                        );
                                    };
                                } else {
                                    return null;
                                }
                            }
            )
            .registerTypeAdapter(ChessGame.class, gameGson.getAdapter(ChessGame.class))
            .create();


    /**
     * Serializes an object.
     *
     * @param obj The object to be serialized.
     *
     * @return A <code>String</code> containing the serialized object.
     */
    static public
    String serialize(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * Deserializes an object.
     *
     * @param obj   The String containing the serialized object.
     * @param clazz The class of the object to
     * @param <T>   The type variable which is of the same type as the class.
     *
     * @return The deserialized object, or a new instance of the object if <code>obj</code> is <code>null</code>.
     */
    static public
    <T> T deserialize(String obj, Class<T> clazz)
    throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var deserialized = gson.fromJson(obj, clazz);

        if (deserialized == null) {
            deserialized = clazz.getDeclaredConstructor().newInstance();
        }

        return deserialized;
    }

    static public
    <T> T deserialize(InputStreamReader obj, Class<T> clazz)
    throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var deserialized = gson.fromJson(obj, clazz);

        if (deserialized == null) {
            deserialized = clazz.getDeclaredConstructor().newInstance();
        }

        return deserialized;
    }
}
