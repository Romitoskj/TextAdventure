package it.uniroma1.textadv.textengine;

import it.uniroma1.textadv.Direzione;
import it.uniroma1.textadv.interfaces.Item;
import it.uniroma1.textadv.Mondo;
import it.uniroma1.textadv.textengine.actions.Action;
import it.uniroma1.textadv.textengine.actions.ActionFactory;
import it.uniroma1.textadv.textengine.exceptions.ActionNotKnownException;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rappresenta un comando inserito dall'utente. A ogni comando corrispondono un azione del giocatore e dei parametri.
 * Ogni azione viene creata sulla base della {@link ActionFactory } associata alla lingua. La lingua predefinita è
 * l'italiano.
 *
 * @see Language
 * @see Action
 * @see ActionFactory
 */
public class Command {


    /**
     * Lingua in cui vengono localizzati i comandi. La lingua predefinita è l'italiano.
     */
    private static Language language = EnglishAndItalian.IT;

    /**
     * Action factory con cui ottenere le varie azioni. Quella predefinita viene fornita dalla lingua italiana.
     */
    private static ActionFactory factory = language.getActionFactory();

    /**
     * Azione che il giocatore compie.
     */
    private final Action action;

    /**
     * Parametri con cui compie l'azione.
     */
    private final List<String> arguments;


    private Command(Action action, List<String> arguments) {
        this.action = action;
        this.arguments = arguments;
    }

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
     * Cambia la factory delle azioni.
     *
     * @param factory la nuova factory.
     */
    public static void setFactory(ActionFactory factory) {
        Command.factory = factory;
    }

    /**
     * Restituisci la factory delle azioni.
     *
     * @return l'action factory
     */
    public static ActionFactory getFactory() {
        return factory;
    }

    @Override
    public String toString() {
        return action + (arguments.size() > 0 ? " arguments=" + arguments : "");
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
    public static Command from(String input) throws ActionNotKnownException {
        List<String> args = new ArrayList<>(Arrays.asList(input.toLowerCase().split("\\s+")));
        Action action = factory.getAction(args.remove(0));
        return new Command(action, findArgs(new ArrayList<>(args)));
    }

    private static List<String> findArgs(List<String> args) {
        List<String> realArgs = new ArrayList<>();
        Item item;
        Direzione dir;
        String name, arg = null;
        int range;

        while (!args.isEmpty()) {
            range = args.size();
            while (range > 0) {

                name = args.stream().limit(range).collect(Collectors.joining(" "));

                item = Mondo.getInstance().getItem(name);
                if (item != null) {
                    args.subList(0, range).clear();
                    arg = item.getName();
                    break;
                }

                if (range == 1) {
                    dir = Direzione.get(name);
                    if (language.getSTOP_WORDS().stream().anyMatch(s -> s.equals(args.get(0))))
                        args.remove(0);
                    else if (dir != null) {
                        args.remove(0);
                        arg = dir.toString();
                    } else {
                        arg = String.join(" ", args);
                        args.subList(0, args.size()).clear();
                    }
                }

                range--;
            }
            if (arg != null) {
                realArgs.add(arg);
                arg = null;
            }
        }
        return realArgs;
    }
}
