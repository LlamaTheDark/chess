package service.user;

import dataaccess.DataAccessException;
import dataaccess.mysql.MySQLAuthDAO;
import dataaccess.mysql.MySQLUserDAO;
import exchange.user.LoginRequest;
import exchange.user.LoginResponse;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.Service;
import service.error.BadRequestException;
import service.error.ServiceException;
import service.error.UnauthorizedException;
import service.util.Authenticator;

public
class LoginService implements Service<LoginResponse, LoginRequest> {

    @Override
    public
    LoginResponse serve(LoginRequest request) throws DataAccessException, ServiceException {
        // bad request checks
        if (request.getUsername() == null || request.getPassword() == null) {
            throw new BadRequestException();
        }

        // verify user
        UserData requestedUser = new MySQLUserDAO().getUser(request.getUsername());
        boolean credentialsMatch =
                requestedUser != null && BCrypt.checkpw(request.getPassword(), requestedUser.password());

        if (!credentialsMatch) {
            throw new UnauthorizedException();
        }

        String authToken = Authenticator.generateToken();
        new MySQLAuthDAO().createAuth(
                new AuthData(authToken, request.getUsername())
        );

        LoginResponse response = new LoginResponse();

        response.setAuthToken(authToken);
        response.setUsername(request.getUsername());

        return response;
    }
}