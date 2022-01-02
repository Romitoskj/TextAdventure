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

    private final Action action;

    private final List<String> arguments;

    public Command(String input) throws ActionNotKnownException {
        ActionFactory FACTORY = language.getActionFactory();
        // String[] words = input.split("\\s+", 2);
        String[] words = input.split("-+");
        action = FACTORY.getAction(words[0]);
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
    }

    @Override
    public String toString() {
        return "*".repeat(40) + " " + action + " arguments=" + arguments;
    }

    public String execute() {
        System.out.println(this);
        return action.execute(arguments);
    }
}
