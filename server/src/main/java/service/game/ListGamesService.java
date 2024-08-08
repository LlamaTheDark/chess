package service.game;

import dataaccess.DataAccessException;
import dataaccess.mysql.MySQLGameDAO;
import exchange.game.ListGamesRequest;
import exchange.game.ListGamesResponse;
import service.Service;
import service.exception.ServiceException;
import service.util.Authenticator;

public
class ListGamesService implements Service<ListGamesResponse, ListGamesRequest> {
    @Override
    public
    ListGamesResponse serve(ListGamesRequest request) throws DataAccessException, ServiceException {
        // unauthenticated checks
        Authenticator.authenticate(request.getAuthToken());

        return new ListGamesResponse(new MySQLGameDAO().listGames());
    }
}
