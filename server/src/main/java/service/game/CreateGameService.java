package service.game;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.memory.MemoryGameDAO;
import exchange.game.CreateGameRequest;
import exchange.game.CreateGameResponse;
import model.GameData;
import service.Service;
import service.error.BadRequestException;
import service.error.ServiceException;
import service.util.Authenticator;

public class CreateGameService implements Service<CreateGameResponse, CreateGameRequest> {
    @Override
    public CreateGameResponse serve(CreateGameRequest request) throws DataAccessException, ServiceException {
        // unauthorized checks
        Authenticator.authenticate(request.getAuthToken());

        // bad request checks
        if(request.getGameName() == null) { throw new BadRequestException(); }

        int gameID = MemoryGameDAO.getNextID();
        new MemoryGameDAO().createGame(new GameData(
                gameID, null, null, request.getGameName(), new ChessGame()
        ));

        return new CreateGameResponse(gameID);
    }
}
