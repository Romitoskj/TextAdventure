package it.uniroma1.textadv.textengine;

import it.uniroma1.textadv.Direzione;
import it.uniroma1.textadv.Item;
import it.uniroma1.textadv.Mondo;
import it.uniroma1.textadv.exceptions.ActionNotKnownException;
import it.uniroma1.textadv.exceptions.ItemNotPresentException;
import it.uniroma1.textadv.exceptions.WrongParameterException;
import it.uniroma1.textadv.textengine.actions.Action;
import it.uniroma1.textadv.textengine.actions.ActionFactory;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 *
 */
public class Command {


    private static Language language;

    private static ActionFactory factory;

    private final Action action;

    private final List<String> arguments;

    private Command(Action action, List<String> arguments) {
        this.action = action;
        this.arguments = arguments;
    }

    /*
    public Command(String input) throws ActionNotKnownException {
        // String[] words = input.split("\\s+", 2);
        String[] words = input.split("-+");
        action = factory.getAction(words[0]);
        arguments = Arrays.stream(words)
                .skip(1)
                .filter(w -> !language.getSTOP_WORDS().contains(w.toLowerCase()))
                .collect(Collectors.toList());
    }
    */

    /**
     * Restituisce la lingua in cui sono scritti i comandi.
     *
     * @return la lingua di gioco
     */
    public static Language getLanguage() {
        return language;
    }

    /**
     * Cambia la lingua in cui sono scritti i comandi.
     *
     * @param language la nuova lingua
     */
    public static void setLanguage(Language language) {
        Command.language = language;
        setFactory(language.getActionFactory());
    }

    /**
     * Cambia la factory dei comandi.
     *
     * @param factory la nuova factory.
     */
    public static void setFactory(ActionFactory factory) {
        Command.factory = factory;
    }

    public static ActionFactory getFactory() {
        return factory;
    }

    @Override
    public String toString() {
        return action + (arguments.size() > 0? " arguments=" + arguments : "");
    }

    /**
     * Esegue il comando.
     *
     * @return Il risultato dell'esecuzione
     */
    public String execute() {
        return action.execute(arguments);
    }

    /**
     * Crea un nuovo comando a partire da una stringa.
     *
     * @param input la stringa da cui creare il comando
     * @return il comando creato
     */
    public static Command create(String input) throws ActionNotKnownException, WrongParameterException {
        List<String> args = new ArrayList<>(Arrays.asList(input.toLowerCase(Locale.ROOT).split("\\s+"))),
                realArgs = new ArrayList<>();
        String arg;

        Action action = factory.getAction(args.remove(0));

        while (!args.isEmpty()) {
            arg = findArg(args);
            if (arg != null) realArgs.add(arg);
        }

        return new Command(action, realArgs);
    }

    private static String findArg(List<String> args) throws WrongParameterException {
        Item res;
        String name;
        Direzione dir;
        int limit = args.size();
        while (limit > 0) {
            name = args.stream().limit(limit).collect(Collectors.joining(" "));
            dir = Direzione.get(name);
            if (dir != null) {
                args.subList(0, limit).clear();
                return dir.toString();
            }
            try {
                res = Mondo.getInstance().getItem(name);
                args.subList(0, limit).clear();
                return res.getNome();
            } catch (ItemNotPresentException e) {
                if (limit == 1) {
                    if (language.getSTOP_WORDS().stream().anyMatch(s -> s.equals(args.get(0)))){
                        args.remove(0);
                        return null;
                    }
                    throw new WrongParameterException();
                }
            }
            limit --;
        }
        return null;
    }
}
