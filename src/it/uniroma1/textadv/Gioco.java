package it.uniroma1.textadv;

import it.uniroma1.textadv.textengine.Command;
import it.uniroma1.textadv.textengine.Language;

import java.nio.file.Path;
import java.util.Scanner;

public class Gioco {

	private Language language = Language.IT;

	public void play(Mondo world) {
		Scanner scanner = new Scanner(System.in);
		String input, output;
		Command command;

		System.out.println(world);
		System.out.println("In ogni momento potrai scrivere \"aiuto\" se hai bisogno di una mano con i comandi.");
		System.out.println("Scrivi guarda per guardarti intorno e iniziare il gioco!");
		do {
			System.out.print("> ");
			input = scanner.nextLine();
			command = new Command(this, input);
			output = command.run();
			System.out.println("\n" + output);
		} while (!input.equalsIgnoreCase("esci"));
	}

	public void play(Mondo world, Path scriptFF) {
		
	}

	public void localizza(Language language) {
		this.language = language;
	}

	public Language getLanguage() {
		return language;
	}
}
// TODO gioco observer giocatore, quando ottiene tesoro vittoria
