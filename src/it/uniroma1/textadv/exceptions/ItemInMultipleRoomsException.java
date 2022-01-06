package it.uniroma1.textadv.exceptions;

import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.oggetti.Oggetto;
import it.uniroma1.textadv.personaggi.Personaggio;

/**
 * Sollevata quando un {@link Oggetto} o un personaggio {@link Personaggio} sono presenti più di una volta nel mondo di
 * gioco, oppure se un {@link Link} è presente più di due volte.
 */
public class ItemInMultipleRoomsException extends Exception{
    public ItemInMultipleRoomsException(String itemName) {
        super(itemName);
    }
}
