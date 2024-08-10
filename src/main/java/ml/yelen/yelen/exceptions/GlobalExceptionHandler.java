package ml.yelen.yelen.exceptions;

import ml.yelen.yelen.pojo.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler  {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseMessage> handleNotFoundException(WebRequest webRequest, NotFoundException ex) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setDate(LocalDateTime.now());
        responseMessage.setStatus(HttpStatus.NOT_FOUND.value());
        responseMessage.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        responseMessage.setMessage(ex.getMessage());
        responseMessage.setPath( webRequest.getDescription(false));

        return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> handleBadRequestException(WebRequest webRequest, BadRequestException ex) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setDate(LocalDateTime.now());
        responseMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        responseMessage.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        responseMessage.setMessage(ex.getMessage());
        responseMessage.setPath( webRequest.getDescription(false));

        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictRequestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseMessage> handleConflictRequestException(WebRequest webRequest, ConflictRequestException ex) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setDate(LocalDateTime.now());
        responseMessage.setStatus(HttpStatus.CONFLICT.value());
        responseMessage.setError(HttpStatus.CONFLICT.getReasonPhrase());
        responseMessage.setMessage(ex.getMessage());
        responseMessage.setPath( webRequest.getDescription(false));

        return new ResponseEntity<>(responseMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenRequestException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseMessage> handleForbiddenRequestException(WebRequest webRequest, ForbiddenRequestException ex) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setDate(LocalDateTime.now());
        responseMessage.setStatus(HttpStatus.FORBIDDEN.value());
        responseMessage.setError(HttpStatus.FORBIDDEN.getReasonPhrase());
        responseMessage.setMessage(ex.getMessage());
        responseMessage.setPath( webRequest.getDescription(false));

        return new ResponseEntity<>(responseMessage, HttpStatus.FORBIDDEN);
    }

}
