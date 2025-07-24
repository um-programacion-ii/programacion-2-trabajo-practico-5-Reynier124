package project.TP5.exceptions;

public abstract class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensage) {
        super(mensage);
    }
}
