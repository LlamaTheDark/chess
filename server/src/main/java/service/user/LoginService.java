package service.user;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exchange.user.LoginRequest;
import exchange.user.LoginResponse;
import service.Authenticator;
import service.Service;
import model.AuthData;

public class LoginService implements Service<LoginRequest, LoginResponse> {

    @Override
    public LoginResponse serve(LoginRequest request) {
        LoginResponse response = new LoginResponse();
        try{
            var user = new MemoryUserDAO().getUser(request.getUsername(), request.getPassword());
            if(user == null){
                response.setStatusCode(401);
                response.setMessage("Error: unauthorized");
            }else{
                String authToken = Authenticator.generateToken();
                new MemoryAuthDAO().createAuth(
                    new AuthData(authToken, request.getUsername())
                );

                response.setStatusCode(200);
                response.setAuthToken(authToken);
                response.setUsername(request.getUsername());
            }
        }catch(DataAccessException e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}