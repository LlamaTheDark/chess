package service.error;

/**
 * Indicates that a request was made to perform an action that is not allowed. In this case, the identity of the
 * requester has been authenticated.
 */
public
class ForbiddenException extends ServiceException {
    public
    ForbiddenException(String message) {super(message);}
}
