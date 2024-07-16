package service.user;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import exchange.user.*;
import model.UserData;
import service.Service;

public class RegisterService implements Service<RegisterRequest, RegisterResponse> {
    @Override
    public RegisterResponse serve(RegisterRequest request) {
        RegisterResponse response = new RegisterResponse();

        try {
            var userDAO = new MemoryUserDAO();
            if (userDAO.getUser(request.getUsername()) != null) {
                response = new RegisterResponse();
                response.setMessage("Error: already taken");
                response.setStatusCode(403);
                return response;
            }
            /*
            todo: handle password encryption
             */
            userDAO.createUser(new UserData(request.getUsername(), request.getPassword(), request.getEmail()));

            response = new LoginService().serve(
                    new LoginRequest(request.getUsername(), request.getPassword())
            );
        }catch(DataAccessException e) {
            // This error would be thrown from
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }
}
