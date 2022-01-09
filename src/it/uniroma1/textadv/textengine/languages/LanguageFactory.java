package it.uniroma1.textadv.textengine.languages;

import it.uniroma1.textadv.textengine.exceptions.LanguageNotKnownException;

/**
 * Factory che si occupa della creazione delle lingue di gioco.
 *
 * @see Language
 */
@FunctionalInterface
public interface LanguageFactory {

    /**
     * Restituisce la lingua richiesta.
     *
     * @param languageName il nome della lingua
     * @return la lingua
     * @throws LanguageNotKnownException se la lingua non è conosciuta dalla factory
     */
    Language getLanguage(String languageName) throws LanguageNotKnownException;
}
