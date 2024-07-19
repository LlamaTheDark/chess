package service.error;

public class BadRequestException extends ServiceException {
    public BadRequestException(String message) { super(message); }
    public BadRequestException(){
        super("Error: bad request");
    }
}
