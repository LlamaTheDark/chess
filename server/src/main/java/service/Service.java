package service;

import dataaccess.DataAccessException;
import exchange.halfduplex.Request;
import exchange.halfduplex.Response;
import service.exception.ServiceException;

public
interface Service<P extends Response, Q extends Request> {
    P serve(Q request) throws DataAccessException, ServiceException;
}
