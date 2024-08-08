package service.exception;

/**
 * Describes a type of error that occurred during one of the server's services attempting to carry out its
 * functionality.
 */
public abstract
class ServiceException extends Exception {
    public
    ServiceException(String message) {super(message);}
}


