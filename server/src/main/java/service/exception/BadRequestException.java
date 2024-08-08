package service.exception;

/**
 * Indicates that the request had invalid information and could therefore not be used.
 */
public
class BadRequestException extends ServiceException {
    public
    BadRequestException(String message) {super(message);}

    public
    BadRequestException() {
        super("Error: bad request");
    }
}
