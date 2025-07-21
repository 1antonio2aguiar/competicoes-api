package codiub.competicoes.api.service.exceptions;

public class NegocioException extends RuntimeException{
    private final long serialVersionUID = 1L;

    public NegocioException(String message){
        super(message);
    }
}
