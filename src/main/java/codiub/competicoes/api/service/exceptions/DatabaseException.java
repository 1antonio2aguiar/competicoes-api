package codiub.competicoes.api.service.exceptions;

public class DatabaseException extends RuntimeException{
    private final long serialVersionUID = 1L;

    public DatabaseException(String msg)
    {
        super(msg);
    }
}
