package service.error;

/**
 * Indicates that a request was made with insufficient credentials.
 */
public
class UnauthorizedException extends ServiceException {
    public
    UnauthorizedException(String message) {super(message);}

    public
    UnauthorizedException() {
        super("Error: unauthorized");
    }
}