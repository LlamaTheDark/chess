package service.user;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryUserDAO;
import exchange.user.LoginRequest;
import exchange.user.LoginResponse;
import service.Authenticator;
import service.Service;
import model.AuthData;
import service.ServiceException;

public class LoginService implements Service<LoginResponse, LoginRequest> {

    @Override
    public LoginResponse serve(LoginRequest request) throws DataAccessException, ServiceException {
        LoginResponse response = new LoginResponse();
        /*
        TODO: see if the user is already logged in
         */
        boolean credentialsMatch =
                new MemoryUserDAO().getUser(request.getUsername(), request.getPassword()) != null;

        var authDAO = new MemoryAuthDAO();
        boolean alreadyLoggedIn = authDAO.getAuth(request.getUsername()) != null;

        if(!credentialsMatch || alreadyLoggedIn){
            throw new ServiceException(401, "Error: unauthorized");
        }else{
            String authToken = Authenticator.generateToken();
            authDAO.createAuth(
                new AuthData(authToken, request.getUsername())
            );

            response.setStatusCode(200);
            response.setAuthToken(authToken);
            response.setUsername(request.getUsername());
        }
        return response;
    }
}