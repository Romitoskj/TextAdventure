package it.uniroma1.textadv.textengine.languages;

import it.uniroma1.textadv.textengine.actions.ActionFactory;

import java.util.Set;

public interface Language {

    Set<String> getSTOP_WORDS();

    ActionFactory getActionFactory();

    String getAnswer(String key);
}
