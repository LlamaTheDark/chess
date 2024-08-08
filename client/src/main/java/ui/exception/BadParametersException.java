package ui.exception;

public
class BadParametersException extends UIException {
    public
    BadParametersException(String message) {super(message);}

    public
    BadParametersException() {this("invalid parameters provided.");}
}
