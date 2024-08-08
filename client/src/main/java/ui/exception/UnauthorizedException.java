package ui.exception;

public
class UnauthorizedException extends UIException {
    public
    UnauthorizedException(String message) {
        super(message);
    }

    public
    UnauthorizedException() {
        this("unable to authenticate request");
    }
}
