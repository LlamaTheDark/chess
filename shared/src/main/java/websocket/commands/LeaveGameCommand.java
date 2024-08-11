package websocket.commands;

import chess.ChessGame;

public
class LeaveGameCommand extends UserGameCommand {
    ChessGame.TeamColor teamColor;

    public
    LeaveGameCommand(CommandType commandType, String authToken, Integer gameID, ChessGame.TeamColor teamColor) {
        super(commandType, authToken, gameID);
        this.teamColor = teamColor;
    }

    public
    ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
}
