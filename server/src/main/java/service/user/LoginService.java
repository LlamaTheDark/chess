package service.user;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryUserDAO;
import exchange.user.LoginRequest;
import exchange.user.LoginResponse;
import model.AuthData;
import service.Service;
import service.error.BadRequestException;
import service.error.ServiceException;
import service.error.UnauthorizedException;
import service.util.Authenticator;

public class LoginService implements Service<LoginResponse, LoginRequest> {

    @Override
    public LoginResponse serve(LoginRequest request) throws DataAccessException, ServiceException {
        // bad request checks
        if(request.getUsername() == null || request.getPassword() == null) {
            throw new BadRequestException();
        }

        boolean credentialsMatch =
                new MemoryUserDAO().getUser(request.getUsername(), request.getPassword()) != null;

        var authDAO = new MemoryAuthDAO();

        if(!credentialsMatch) {
            throw new UnauthorizedException();
        }

        String authToken = Authenticator.generateToken();
        authDAO.createAuth(
                new AuthData(authToken, request.getUsername())
        );

        LoginResponse response = new LoginResponse();

        response.setAuthToken(authToken);
        response.setUsername(request.getUsername());

        return response;
    }
}