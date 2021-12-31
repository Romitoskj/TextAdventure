package it.uniroma1.textadv.textengine;

import it.uniroma1.textadv.*;

import java.util.*;
import java.util.stream.Collectors;

public class Command {

    private final Action action;

    private final List<String> arguments;

    public Command(Gioco g, String input) {
        ActionFactory FACTORY = g.getLanguage().getActionFactory();
        // String[] words = input.split("\\s+", 2);
        String[] words = input.split("-+");
        action = FACTORY.createAction(words[0]);
        arguments = Arrays.stream(words)
                .skip(1)
                .filter(w -> !g.getLanguage().getSTOP_WORDS().contains(w.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "*".repeat(40)+ " arguments=" + arguments;
    }

    public String run() {
        System.out.println(this);
        if (action == null) return "Comando non riconosciuto.";// TODO sollevare un eccezione
        return action.apply(arguments);
    }
}
