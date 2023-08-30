package net.getko.iilrepository.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="conflict found")
public class NoCorrespondingGoalException extends RuntimeException{
    public NoCorrespondingGoalException(String message) {
        super(message);
    }

}
