package it.uniroma1.textadv.textengine.actions;

import it.uniroma1.textadv.textengine.exceptions.ActionNotKnownException;

/**
 * Factory che si occupa della creazione delle azioni che il giocatore può effettuare.
 *
 * @see Action
 */
public interface ActionFactory {

    /**
     * Restituisce un'azione dato il suo nome.
     *
     * @param name nome dell'azione
     * @return l'azione richiesta
     * @throws ActionNotKnownException se l'azione non è conosciuta dalla factory
     */
    Action getAction(String name) throws ActionNotKnownException;

    /**
     * Restituisce un array contenente tutte le azioni conosciute dalla factory.
     *
     * @return array delle azioni che la factory può creare
     */
    Action[] values();
}
