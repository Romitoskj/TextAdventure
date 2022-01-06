package it.uniroma1.textadv.exceptions;

import it.uniroma1.textadv.links.Link;

/**
 * Sollevata se sui prova a creare una stanza senza che essa abbia la sezione dei {@link Link}. Questo perché ogni
 * stanza deve avere almeno un link.
 */
public class RoomWithoutLinksException extends Exception{
    public RoomWithoutLinksException(String message) {
        super(message);
    }
}
