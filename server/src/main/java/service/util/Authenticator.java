package service.util;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.mysql.MySQLAuthDAO;
import service.exception.UnauthorizedException;

import java.util.UUID;

/**
 * A utility class to provide for authentication-related needs.
 */
public
class Authenticator {

    /**
     * Authenticates an authorization token.
     *
     * @param authToken The authentication token to be authenticated.
     *
     * @throws DataAccessException   if there is an error connecting to the database.
     * @throws UnauthorizedException if the token is unauthorized.
     */
    public static
    void authenticate(String authToken) throws DataAccessException, UnauthorizedException {
        if (authToken == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        AuthDAO authDAO = new MySQLAuthDAO();
        if (authDAO.getAuthByToken(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    /**
     * @return A <a href="https://en.wikipedia.org/wiki/Universally_unique_identifier">Universally Unique Identifier</a>
     * in <code>String</code> format.
     */
    public static
    String generateToken() {
        return UUID.randomUUID().toString();
    }
}
