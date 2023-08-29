package net.getko.iilrepository.exceptions;

public class ConditionValidationException extends Exception {

    /**
     * Instantiates a new Xml validation exception.
     *
     * @param message the message
     * @param t       the t
     */
    public ConditionValidationException(String message, Throwable t) {
        super(message, t);
    }
}
