package service.error;

public abstract class ServiceException extends Exception {
    public ServiceException(String message) { super(message); }
}


