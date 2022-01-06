package it.uniroma1.textadv;

import java.nio.file.Path;

public class Test {

	public static void main(String[] args) throws Exception {
		Gioco g = new Gioco();
		Mondo m = Mondo.fromFile("minizak.game");
		// Mondo m = Mondo.fromFile("english.game");
		g.play(m, Path.of("minizak.ff"));
		//g.play(m);
	}
}