package service.error;

public
class UnauthorizedException extends ServiceException {
    public
    UnauthorizedException(String message) {super(message);}

    public
    UnauthorizedException() {
        super("Error: unauthorized");
    }
}