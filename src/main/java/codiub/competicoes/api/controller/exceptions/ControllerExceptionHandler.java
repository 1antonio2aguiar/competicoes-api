package codiub.competicoes.api.controller.exceptions;

import codiub.competicoes.api.controller.exceptions.StandardError;
import codiub.competicoes.api.service.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
        String error = "Objeto não encontrado";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError stError = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(stError);
    }
}
