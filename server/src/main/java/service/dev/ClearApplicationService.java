package service.dev;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import exchange.dev.ClearApplicationRequest;
import exchange.dev.ClearApplicationResponse;
import service.Service;

public class ClearApplicationService implements Service<ClearApplicationResponse, ClearApplicationRequest> {
    @Override
    public ClearApplicationResponse serve(ClearApplicationRequest request) throws DataAccessException {
        ClearApplicationResponse response = new ClearApplicationResponse();

        MemoryAuthDAO auth = new MemoryAuthDAO();
        MemoryUserDAO user = new MemoryUserDAO();
        MemoryGameDAO game = new MemoryGameDAO();

        auth.clear();
        user.clear();
        game.clear();

        return response;
    }
}
