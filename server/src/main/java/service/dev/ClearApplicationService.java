package service.dev;

import dataaccess.DataAccessException;
import dataaccess.mysql.MySQLAuthDAO;
import dataaccess.mysql.MySQLGameDAO;
import dataaccess.mysql.MySQLUserDAO;
import exchange.dev.ClearApplicationRequest;
import exchange.dev.ClearApplicationResponse;
import service.Service;

public
class ClearApplicationService implements Service<ClearApplicationResponse, ClearApplicationRequest> {
    @Override
    public
    ClearApplicationResponse serve(ClearApplicationRequest request) throws DataAccessException {
        ClearApplicationResponse response = new ClearApplicationResponse();

        var auth = new MySQLAuthDAO();
        var user = new MySQLUserDAO();
        var game = new MySQLGameDAO();

        game.clear();
        auth.clear();
        user.clear();

        return response;
    }
}
