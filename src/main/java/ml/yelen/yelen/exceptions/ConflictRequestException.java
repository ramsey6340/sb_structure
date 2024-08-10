package ml.yelen.yelen.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictRequestException extends RuntimeException{
    public ConflictRequestException(String message) {
        super(message);
    }
}
