package service;

import exchange.Request;
import exchange.Response;

public interface Service<T extends Request, S extends Response> {
    S serve(T request);
}
