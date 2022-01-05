package it.uniroma1.textadv;

import it.uniroma1.textadv.exceptions.ActionNotKnownException;
import it.uniroma1.textadv.exceptions.LanguageNotKnownException;
import it.uniroma1.textadv.exceptions.WrongParameterException;
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
import java.util.List;
import java.util.Scanner;

public class Gioco implements Observer {

	private LanguageFactory lanFactory = EnglishAndItalian.getFactory();

	private boolean win;


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
		System.out.println(Command.getLanguage().getAnswer("start"));
		do {
			if (win) {
				System.out.println("\n" + Command.getLanguage().getAnswer("win"));
				System.out.println(Command.getLanguage().getAnswer("end"));
			}
			System.out.print("> ");
			input = scanner.nextLine();
			try {
				command = Command.create(input.strip());
				System.out.println("*".repeat(40) + " " + command);
				output = command.execute();
			} catch (ActionNotKnownException e) {
				output = Command.getLanguage().getAnswer("not_found_action");
			} catch (WrongParameterException e) {
                output = Command.getLanguage().getAnswer("wrong_args");
            }
            System.out.println("\n" + output);
		} while (!input.equalsIgnoreCase("esci") && !input.equalsIgnoreCase("quit"));
	}

	public void play(Mondo world, Path scriptFF) throws IOException {
		Giocatore.getInstance().registraObserver(this);
		localizza(EnglishAndItalian.IT);
		Iterator<String> lines = Files.readAllLines(scriptFF).iterator();
		String output, line;
		Command command;
		int k;

        while (lines.hasNext()) {
			System.out.print("> ");
			try {
				line = lines.next();
				k = line.indexOf("//");
				if (k >= 0) line = line.substring(0, k); // rimuove commenti
				System.out.println(line);
				command = Command.create(line.strip());
				System.out.println(command);
				output = command.execute();
			} catch (ActionNotKnownException e) {
				output = Command.getLanguage().getAnswer("not_found_action");
			} catch (WrongParameterException e) {
                output = Command.getLanguage().getAnswer("wrong_args");
            }
            System.out.println("\n" + output);
		}
		if (win) System.out.println("\n" + Command.getLanguage().getAnswer("win"));
	}

	public void setLanguageFactory(LanguageFactory lanFactory) {
		this.lanFactory = lanFactory;
	}

	public void localizza(Language language) {
		Command.setLanguage(language);
	}

	@Override
	public void update() {
		win = true;
	}
}
