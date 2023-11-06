package dev.jbcl.Excepcion;

public abstract class FunkoException extends RuntimeException {
    public FunkoException(String message) {
        super(message);
    }
}
