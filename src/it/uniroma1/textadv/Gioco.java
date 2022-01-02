package it.uniroma1.textadv;

import it.uniroma1.textadv.exceptions.ActionNotKnownException;
import it.uniroma1.textadv.exceptions.LanguageNotKnownException;
import it.uniroma1.textadv.textengine.Command;
import it.uniroma1.textadv.textengine.Language;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class Gioco {

	private Language language;

	public void play(Mondo world) {
		Scanner scanner = new Scanner(System.in);
		String input, output;
		Command command;

		while (language == null) {
			System.out.print("Seleziona lingua / Select language: " + Arrays.toString(Language.values()) + "\n> ");
			try {
				localizza(Language.get(scanner.nextLine()));
			} catch (LanguageNotKnownException e) {
				System.out.println("Lingua non riconosciuta / Unrecognized language.");
			}
		}

		System.out.println(world);
		System.out.println(switch (language) {
			case IT -> "In ogni momento potrai scrivere \"aiuto\" se hai bisogno di una mano con i comandi.\n" +
					"Scrivi \"guarda\" per guardarti intorno e iniziare l'avventura!";
			case EN -> "You can write \"help\" in any moment if your struggle with the commands.\n" +
					"Write \"look\" to look around the room and start the adventure!";
		});
		do {
			System.out.print("> ");
			input = scanner.nextLine();
			try {
				command = new Command(language, input);
				output = command.run();
			} catch (ActionNotKnownException e) {
				output = switch (language) {
					case IT -> "Azione non riconosciuta.";
					case EN -> "Unrecognized action.";
				};
			}
			System.out.println("\n" + output);
		} while (!input.equalsIgnoreCase("esci") && !input.equalsIgnoreCase("quit"));
	}

	public void play(Mondo world, Path scriptFF) {
		localizza(Language.IT);
	}

	public void localizza(Language language) {
		this.language = language;
	}

	public Language getLanguage() {
		return language;
	}
}
// TODO gioco observer giocatore, quando ottiene tesoro vittoria
