package it.uniroma1.textadv.exceptions;

public class UnnamedPlayerException extends RuntimeException {
    public UnnamedPlayerException() {
        super("The player must have a name");
    }
}
