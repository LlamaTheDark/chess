package game;

import chess.ChessGame;
import model.GameData;
import websocket.WebSocketFacade;

public
class GameplayHandler extends GameHandler {
    private static final String GAMEPLAY_PROMPT = " [GAME COMMAND] >>> ";

    public
    GameplayHandler(ChessGame.TeamColor teamColor, GameData gameData) {
        super(teamColor, gameData, false);
    }

    public
    void setWebSocketFacade(WebSocketFacade wsFacade) {this.wsFacade = wsFacade;}

    @Override
    public
    void start() {
        GameHandler.repl(gameplayUI, threadManager, wsFacade, gameData, teamColor, false, GAMEPLAY_PROMPT);
    }

    @Override
    public
    void updateGame(ChessGame game) {
        this.gameData = GameHandler.updateGame(game, gameData, gameplayUI, teamColor, threadManager, false);
    }

    @Override
    public
    void printMessage(String message, boolean error) {
        GameHandler.printMessage(message, error, gameplayUI, threadManager);
    }
}
