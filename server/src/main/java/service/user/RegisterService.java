package service.user;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryUserDAO;
import exchange.user.LoginRequest;
import exchange.user.RegisterRequest;
import exchange.user.RegisterResponse;
import model.UserData;
import service.Service;
import service.error.BadRequestException;
import service.error.ForbiddenException;
import service.error.ServiceException;

public class RegisterService implements Service<RegisterResponse, RegisterRequest> {
    @Override
    public RegisterResponse serve(RegisterRequest request) throws DataAccessException, ServiceException {

        var userDAO = new MemoryUserDAO();
        if (userDAO.getUser(request.getUsername()) != null) {
            throw new ForbiddenException("Error: already taken");
        }
        /*
        todo: handle password encryption
         */

        // test to make sure all fields are legitimate
        if(request.getUsername() == null || request.getPassword() == null || request.getEmail() == null){
            throw new BadRequestException("Error: bad request");
        }

        userDAO.createUser(new UserData(request.getUsername(), request.getPassword(), request.getEmail()));

        return new LoginService().serve(
                new LoginRequest(request.getUsername(), request.getPassword())
        );
    }
}
