package it.uniroma1.textadv.textengine.languages;

import it.uniroma1.textadv.exceptions.LanguageNotKnownException;

@FunctionalInterface
public interface LanguageFactory {

    Language getLanguage(String languageName) throws LanguageNotKnownException;
}