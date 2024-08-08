package service.user;

import dataaccess.DataAccessException;
import dataaccess.mysql.MySQLAuthDAO;
import exchange.user.LogoutRequest;
import exchange.user.LogoutResponse;
import service.Service;
import service.exception.ServiceException;
import service.util.Authenticator;

public
class LogoutService implements Service<LogoutResponse, LogoutRequest> {
    @Override
    public
    LogoutResponse serve(LogoutRequest request) throws DataAccessException, ServiceException {
        Authenticator.authenticate(request.getAuthToken());

        new MySQLAuthDAO().deleteAuth(request.getAuthToken());
        return new LogoutResponse();
    }
}
