package service.game;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryGameDAO;
import exchange.game.ListGamesRequest;
import exchange.game.ListGamesResponse;
import service.Authenticator;
import service.Service;
import service.error.ServiceException;

public class ListGamesService implements Service<ListGamesResponse, ListGamesRequest> {
    @Override
    public ListGamesResponse serve(ListGamesRequest request) throws DataAccessException, ServiceException {
        // unauthenticated checks
        Authenticator.authenticate(request.getAuthToken());

        return new ListGamesResponse(new MemoryGameDAO().listGames());
    }
}
