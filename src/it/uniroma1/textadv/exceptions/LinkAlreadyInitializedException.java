package it.uniroma1.textadv.exceptions;

public class LinkAlreadyInitializedException extends RuntimeException {
    public LinkAlreadyInitializedException() {
        super("Links must be initialized only one time.");
    }
}
