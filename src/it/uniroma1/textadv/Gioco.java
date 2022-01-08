package it.uniroma1.textadv;

import it.uniroma1.textadv.exceptions.ActionNotKnownException;
import it.uniroma1.textadv.exceptions.LanguageNotKnownException;
import it.uniroma1.textadv.personaggi.Giocatore;
import it.uniroma1.textadv.personaggi.Observer;
import it.uniroma1.textadv.textengine.Command;
import it.uniroma1.textadv.textengine.languages.Language;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.LanguageFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Rappresenta un gioco che può essere giocato fornendo un mondo di gioco.
 *
 * @see Mondo
 * @see Language
 */
public class Gioco implements Observer {

    /**
     * La factory che fornisce la lingua scelta dal giocatore.
     */
    private LanguageFactory lanFactory = EnglishAndItalian.getFactory();

    /**
     * Indica se il gioco è stato vinto o no.
     */
    private boolean win;


    /**
     * Permette di giocare nel mondo di gioco fornito.
     *
     * @param world il {@link Mondo} di gioco;
     */
    public void play(Mondo world) {
        Scanner scanner = new Scanner(System.in);
        String input, output;
        Command command;
        Language language = null;

        Giocatore.getInstance().registraObserver(this);

        while (language == null) {
            System.out.print("Seleziona lingua / Select language: " + Arrays.toString(EnglishAndItalian.values()) + "\n> ");
            try {
                language = lanFactory.getLanguage(scanner.nextLine());
                localizza(language);
            } catch (LanguageNotKnownException e) {
                System.out.println("Lingua non riconosciuta / Unrecognized language.");
            }
        }

        System.out.println(world);
        System.out.println(Command.getLanguage().getAnswer("start").formatted(Giocatore.getInstance().getName()));
        do {
            if (win) {
                System.out.println("\n" + Command.getLanguage().getAnswer("win"));
                System.out.println(Command.getLanguage().getAnswer("end"));
            }
            System.out.print("> ");
            input = scanner.nextLine();
            try {
                command = Command.from(input.strip());
                output = command.execute();
            } catch (ActionNotKnownException e) {
                output = Command.getLanguage().getAnswer("not_found_action");
            }
            System.out.println("\n" + output);
        } while (!input.equalsIgnoreCase(Command.getLanguage().getAnswer("quit")));
    }

    /**
     * Permette di giocare nel {@link Mondo} di gioco in maniera fast-forward.
     *
     * @param world    il mondo di gioco
     * @param scriptFF il path del file fast-forward
     * @throws IOException se occorre un errore I/O leggendo il file
     */
    public void play(Mondo world, Path scriptFF) throws IOException {
        if (!scriptFF.toString().endsWith(".ff"))
            throw new IOException("The file '%s' is not a .ff file.".formatted(scriptFF));

        Giocatore.getInstance().registraObserver(this);
        localizza(EnglishAndItalian.IT);
        Iterator<String> lines = Files.readAllLines(scriptFF).iterator();
        String output, line;
        Command command;
        int k;

        System.out.println(world);

        while (lines.hasNext()) {
            System.out.print("> ");
            try {
                line = lines.next();
                k = line.indexOf("//");
                if (k >= 0) line = line.substring(0, k); // rimuove commenti
                System.out.println(line);
                command = Command.from(line.strip());
                output = command.execute();
            } catch (ActionNotKnownException e) {
                output = Command.getLanguage().getAnswer("not_found_action");
            }
            System.out.println("\n" + output);
        }
        if (win) System.out.println("\n" + Command.getLanguage().getAnswer("win"));
    }

    /**
     * Permette di fornire al gioco il metodo con cui selezionare il la lingua.
     *
     * @param lanFactory {@link LanguageFactory} con cui viene creata la lingua
     */
    public void setLanguageFactory(LanguageFactory lanFactory) {
        this.lanFactory = lanFactory;
    }

    /**
     * Localizza il gioco nella lingua fornita.
     *
     * @param language lingua di gioco
     */
    public void localizza(Language language) {
        Command.setLanguage(language);
    }

    @Override
    public void update() {
        win = true;
    }
}
