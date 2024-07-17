package service.user;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryUserDAO;
import exchange.user.*;
import model.UserData;
import service.Service;
import service.ServiceException;

public class RegisterService implements Service<RegisterResponse, RegisterRequest> {
    @Override
    public RegisterResponse serve(RegisterRequest request) throws DataAccessException, ServiceException {
        RegisterResponse response = new RegisterResponse();

        var userDAO = new MemoryUserDAO();
        if (userDAO.getUser(request.getUsername()) != null) {
            /*
            TODO: maybe don't require the need for formatting the message in json (i.e. have this done at a higher level)?
             */
            throw new ServiceException(403, "{ \"message\": \"Error: already taken\"}");
        }
        /*
        todo: handle password encryption
         */
        userDAO.createUser(new UserData(request.getUsername(), request.getPassword(), request.getEmail()));

        response = new LoginService().serve(
                new LoginRequest(request.getUsername(), request.getPassword())
        );

        return response;
    }
}
