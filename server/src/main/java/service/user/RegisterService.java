package service.user;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryUserDAO;
import exchange.user.*;
import model.UserData;
import service.Service;
import service.error.ServiceException;

public class RegisterService implements Service<RegisterResponse, RegisterRequest> {
    @Override
    public RegisterResponse serve(RegisterRequest request) throws DataAccessException, ServiceException {

        var userDAO = new MemoryUserDAO();
        if (userDAO.getUser(request.getUsername()) != null) {
            throw new ServiceException(403, "Error: already taken");
        }
        /*
        todo: handle password encryption
         */
        userDAO.createUser(new UserData(request.getUsername(), request.getPassword(), request.getEmail()));

        return new LoginService().serve(
                new LoginRequest(request.getUsername(), request.getPassword())
        );
    }
}
