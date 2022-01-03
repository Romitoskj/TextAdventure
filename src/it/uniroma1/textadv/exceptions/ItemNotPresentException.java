package it.uniroma1.textadv.exceptions;

public class ItemNotPresentException extends Exception {

    public ItemNotPresentException() {
        super("The requested item is not in this room");
    } // TODO considerare cambiamento ad optional
}
