package service.user;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import exchange.user.LogoutRequest;
import exchange.user.LogoutResponse;
import service.Service;
import service.error.ServiceException;
import service.error.UnauthorizedException;

public class LogoutService implements Service<LogoutResponse, LogoutRequest> {
    @Override
    public LogoutResponse serve(LogoutRequest request) throws DataAccessException, ServiceException {
        AuthDAO authDAO = new MemoryAuthDAO();
        if(authDAO.getAuthByToken(request.getAuthToken()) == null){
            throw new UnauthorizedException("Error: unauthorized");
        }

        authDAO.deleteAuth(request.getAuthToken());
        return new LogoutResponse();
    }
}
