package it.uniroma1.textadv.exceptions;

import it.uniroma1.textadv.interfaces.Item;

/**
 * Eccezione sollevata se si cerca di ottenere un {@link Item} che non è presente.
 */
public class ItemNotPresentException extends Exception {

    /**
     * Crea una {@link ItemNotPresentException}.
     */
    public ItemNotPresentException() {
        super("The requested item is not in this room");
    }
}
