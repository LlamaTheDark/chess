package service.user;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryUserDAO;
import exchange.user.LoginRequest;
import exchange.user.LoginResponse;
import model.AuthData;
import service.Authenticator;
import service.Service;
import service.error.UnauthorizedException;

public class LoginService implements Service<LoginResponse, LoginRequest> {

    @Override
    public LoginResponse serve(LoginRequest request) throws DataAccessException, UnauthorizedException {
        LoginResponse response = new LoginResponse();
        boolean credentialsMatch =
                new MemoryUserDAO().getUser(request.getUsername(), request.getPassword()) != null;

        var authDAO = new MemoryAuthDAO();


        if(!credentialsMatch) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String authToken = Authenticator.generateToken();
        authDAO.createAuth(
                new AuthData(authToken, request.getUsername())
        );

        response.setAuthToken(authToken);
        response.setUsername(request.getUsername());

        return response;
    }
}