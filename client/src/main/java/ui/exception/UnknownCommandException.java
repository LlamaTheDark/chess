package ui.exception;

public
class UnknownCommandException extends Exception {
    public
    UnknownCommandException(final String message) {super(message);}

    public
    UnknownCommandException() {super("Unknown command. Type 'help' for a list of possible commands.");}
}
