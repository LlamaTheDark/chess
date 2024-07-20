package service.util;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import service.error.UnauthorizedException;

import java.util.UUID;

public class Authenticator {
    public static void authenticate(String authToken) throws DataAccessException, UnauthorizedException {
        if(authToken == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthDAO authDAO = new MemoryAuthDAO();
        if(authDAO.getAuthByToken(authToken) == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
