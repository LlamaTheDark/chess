package service.game;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import exchange.game.JoinGameRequest;
import exchange.game.JoinGameResponse;
import model.GameData;
import service.Authenticator;
import service.Service;
import service.error.BadRequestException;
import service.error.ForbiddenException;
import service.error.ServiceException;

public class JoinGameService implements Service<JoinGameResponse, JoinGameRequest> {
    public enum PlayerColor {
        WHITE,
        BLACK
    }
    
    @Override
    public JoinGameResponse serve(JoinGameRequest request) throws DataAccessException, ServiceException {
        // unauthenticated checks
        Authenticator.authenticate(request.getAuthToken());

        var gameDAO = new MemoryGameDAO();
        var requestedGame = gameDAO.getGame(request.getGameID());

        // bad request checks
        if(request.getPlayerColor() == null || requestedGame == null) {
            throw new BadRequestException();
        }

        var playerColor = switch(request.getPlayerColor()) {
            case "WHITE" -> PlayerColor.WHITE;
            case "BLACK" -> PlayerColor.BLACK;
            default -> throw new BadRequestException();
        };

        // forbidden checks
        if(switch(playerColor) {
            case WHITE -> requestedGame.whiteUsername() != null;
            case BLACK -> requestedGame.blackUsername() != null;
        }){
            throw new ForbiddenException("Error: already taken");
        }

        // join the game
        var playerUsername = new MemoryAuthDAO().getAuthByToken(request.getAuthToken()).username();
        gameDAO.updateGame(switch(playerColor){
            case WHITE -> new GameData(requestedGame.gameID(), playerUsername, requestedGame.blackUsername(),
                                       requestedGame.gameName(), requestedGame.game());
            case BLACK -> new GameData(requestedGame.gameID(), requestedGame.whiteUsername(), playerUsername,
                                       requestedGame.gameName(), requestedGame.game());
        });

        return new JoinGameResponse();
    }
}