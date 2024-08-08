package service;

import dataaccess.DataAccessException;
import exchange.Request;
import exchange.Response;
import service.exception.ServiceException;

public
interface Service<P extends Response, Q extends Request> {
    P serve(Q request) throws DataAccessException, ServiceException;
}
