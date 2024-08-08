package service.game;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.mysql.MySQLGameDAO;
import exchange.game.CreateGameRequest;
import exchange.game.CreateGameResponse;
import model.GameData;
import service.Service;
import service.exception.BadRequestException;
import service.exception.ServiceException;
import service.util.Authenticator;

public
class CreateGameService implements Service<CreateGameResponse, CreateGameRequest> {
    @Override
    public
    CreateGameResponse serve(CreateGameRequest request) throws DataAccessException, ServiceException {
        // unauthorized checks
        Authenticator.authenticate(request.getAuthToken());

        // bad request checks
        if (request.getGameName() == null) {throw new BadRequestException();}

        // create a GameData instance with ID = 0
        GameData gameData = new GameData(null, null, request.getGameName(), new ChessGame());

        // create game and receive game with updated ID from database
        gameData = new MySQLGameDAO().createGame(gameData);

        return new CreateGameResponse(gameData.gameID());
    }
}
