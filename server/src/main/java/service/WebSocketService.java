package service;

import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import dataaccess.mysql.MySQLGameDAO;
import model.GameData;
import service.exception.UnauthorizedException;
import service.util.Authenticator;

public
class WebSocketService {

    public
    String getUsernameFromAuthToken(String authToken) throws UnauthorizedException, DataAccessException {
        return Authenticator.getUsername(authToken);
    }

    public
    GameData getGameDataFromID(int gameID) throws DataAccessException {
        return new MySQLGameDAO().getGame(gameID);
    }

    public
    void makeMove(ChessMove move, GameData gameData) throws DataAccessException, InvalidMoveException {
        gameData.game().makeMove(move);
        new MySQLGameDAO().updateGame(gameData);
    }
}
