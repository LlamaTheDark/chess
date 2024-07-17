package service;

import exchange.Request;
import exchange.Response;

/*
TODO: use generic method instead of generic class
 */
public interface Service<P extends Response, Q extends Request> {
    P serve(Q request);
}
