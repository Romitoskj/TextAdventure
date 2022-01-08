package it.uniroma1.textadv;

import it.uniroma1.textadv.textengine.languages.Language;

/**
 * Rappresenta tutti ciò che può essere inserito in un mondo di gioco.
 */
public interface Item {

    /**
     * Restituisce il nome dell'item.
     *
     * @return nome.
     */
    String getName();

    /**
     * Restituisce la descrizione dell'item in base alla lingua di gioco.
     *
     * @param language la lingua di gioco {@link Language}.
     * @return descrizione.
     */
    String getDescription(Language language);
}
