package game;

import chess.ChessGame;
import model.GameData;
import websocket.WebSocketFacade;

public
class ObserveGameHandler extends GameHandler {
    private static final String GAMEPLAY_PROMPT = " [GAME COMMAND] >>> ";

    public
    ObserveGameHandler(ChessGame.TeamColor teamColor, GameData gameData) {
        super(teamColor, gameData, true);
    }

    public
    void setWebSocketFacade(WebSocketFacade wsFacade) {this.wsFacade = wsFacade;}

    @Override
    public
    void start() {
        GameHandler.repl(gameplayUI, threadManager, wsFacade, gameData, teamColor, true, GAMEPLAY_PROMPT);
    }

    @Override
    public
    void updateGame(ChessGame game) {
        this.gameData = GameHandler.updateGame(game, gameData, gameplayUI, teamColor, threadManager, true);
    }

    @Override
    public
    void printMessage(String message, boolean error) {
        GameHandler.printMessage(message, error, gameplayUI, threadManager);
    }
}
