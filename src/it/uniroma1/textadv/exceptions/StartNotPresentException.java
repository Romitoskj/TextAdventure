package it.uniroma1.textadv.exceptions;

public class StartNotPresentException extends RuntimeException {

    public StartNotPresentException() {
        super("The start room does not exist in this world.");
    }
}
