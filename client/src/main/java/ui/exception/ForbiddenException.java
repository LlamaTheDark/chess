package ui.exception;

public
class ForbiddenException extends UIException {
    public
    ForbiddenException(String message) {
        super(message);
    }

    public
    ForbiddenException() {
        this("you don't have permission to perform this action.");
    }
}
