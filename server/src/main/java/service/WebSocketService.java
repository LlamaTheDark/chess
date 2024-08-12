package service;

import dataaccess.DataAccessException;
import dataaccess.mysql.MySQLGameDAO;
import model.GameData;
import service.exception.UnauthorizedException;
import service.util.Authenticator;

public
class WebSocketService {

    private static MySQLGameDAO gameDAO;

    static {
        try {
            gameDAO = new MySQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public
    String getUsernameFromAuthToken(String authToken) throws UnauthorizedException, DataAccessException {
        return Authenticator.getUsername(authToken);
    }

    public
    GameData getGameDataFromID(int gameID) throws DataAccessException {
        return gameDAO.getGame(gameID);
    }

    public
    void updateGame(GameData gameData) throws DataAccessException {
        gameDAO.updateGame(gameData);
    }

    public
    void closeGame(int gameID) throws DataAccessException {
        gameDAO.closeGame(gameID);
    }
}