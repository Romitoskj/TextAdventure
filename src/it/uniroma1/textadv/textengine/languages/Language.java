package it.uniroma1.textadv.textengine.languages;

import it.uniroma1.textadv.textengine.actions.ActionFactory;

import java.util.Set;

/**
 * Rappresenta una lingua in cui è possibile giocare.
 */
public interface Language {

    /**
     * Restituisce l'insieme delle stop word della lingua.
     *
     * @return l'insieme delle stop word
     */
    Set<String> getStopWords();

    /**
     * Restituisce l'{@link ActionFactory} relativa alla lingua.
     *
     * @return l'action factory
     */
    ActionFactory getActionFactory();

    /**
     * Restituisce un riscontro testuale in questa lingua usato da un'azione dato il suo identificatore.
     *
     * @param key la stringa identificativa del riscontro
     * @return il riscontro testuale
     */
    String getAnswer(String key);
}
