package net.getko.iilrepository.exceptions;

public class ActionValidationException extends Exception {
    public ActionValidationException(String message, Throwable t) {
        super(message, t);
    }
}
