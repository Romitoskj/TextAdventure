package it.uniroma1.textadv.textengine;

import it.uniroma1.textadv.exceptions.ActionNotKnownException;
import it.uniroma1.textadv.textengine.actions.Action;
import it.uniroma1.textadv.textengine.actions.ActionFactory;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Command {

    private static Language language;

    private static ActionFactory factory;

    private final Action action;

    private final List<String> arguments;

    public Command(String input) throws ActionNotKnownException {
        // String[] words = input.split("\\s+", 2);
        String[] words = input.split("-+");
        action = factory.getAction(words[0]);
        arguments = Arrays.stream(words) // TODO split riconoscendo argomenti
                .skip(1)
                .filter(w -> !language.getSTOP_WORDS().contains(w.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static Language getLanguage() {
        return language;
    }

    public static void setLanguage(Language language) {
        Command.language = language;
        setFactory(language.getActionFactory());
    }

    public static void setFactory(ActionFactory factory) {
        Command.factory = factory;
    }

    @Override
    public String toString() {
        return action + (arguments.size() > 0? " arguments=" + arguments : "");
    }

    public String execute() {
        return action.execute(arguments);
    }
}
