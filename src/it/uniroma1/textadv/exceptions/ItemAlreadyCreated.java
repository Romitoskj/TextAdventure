package it.uniroma1.textadv.exceptions;

import it.uniroma1.textadv.Item;

/**
 * Sollevata quando viene creato un {@link Item} con lo stesso nome di uno già presente.
 */
public class ItemAlreadyCreated extends Exception{
    public ItemAlreadyCreated(String message) {
        super(message);
    }
}
