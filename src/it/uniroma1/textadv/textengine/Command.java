package it.uniroma1.textadv.textengine;

import it.uniroma1.textadv.exceptions.ActionNotKnownException;

import java.util.*;
import java.util.stream.Collectors;

public class Command {

    private final Action action;

    private final List<String> arguments;

    public Command(Language l, String input) throws ActionNotKnownException {
        ActionFactory FACTORY = l.getActionFactory();
        // String[] words = input.split("\\s+", 2);
        String[] words = input.split("-+");
        action = FACTORY.createAction(words[0]);
        arguments = Arrays.stream(words) // TODO split riconoscendo argomenti
                .skip(1)
                .filter(w -> !l.getSTOP_WORDS().contains(w.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "*".repeat(40) + " " + action + " arguments=" + arguments;
    }

    public String run() {
        System.out.println(this);
        return action.execute(arguments);
    }
}
