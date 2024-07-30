package model;

import chess.ChessGame;

/**
 * @param gameID        If <code>gameID</code> is 0, it is treated as though it were <code>null</code> (i.e. it does not
 *                      have an actual ID yet)
 * @param whiteUsername
 * @param blackUsername
 * @param gameName
 * @param game
 */
public
record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public
    GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public
    GameData(String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this(0, whiteUsername, blackUsername, gameName, game);
    }
}
