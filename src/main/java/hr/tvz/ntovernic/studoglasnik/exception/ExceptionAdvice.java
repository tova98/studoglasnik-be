package hr.tvz.ntovernic.studoglasnik.exception;

import hr.tvz.ntovernic.studoglasnik.dto.ErrorResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(final EntityNotFoundException e) {
        final ErrorResponse body = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<ErrorResponse> handleEntityExistsException(final EntityExistsException e) {
        final ErrorResponse body = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(final BadCredentialsException e) {
        final ErrorResponse body = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(final AccessDeniedException e) {
        final ErrorResponse body = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({WrongPasswordException.class})
    public ResponseEntity<ErrorResponse> handleEntityExistsException(final WrongPasswordException e) {
        final ErrorResponse body = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleConstrainViolationException() {
        final ErrorResponse body = new ErrorResponse("Can't delete! Entity is still in use!");
        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
