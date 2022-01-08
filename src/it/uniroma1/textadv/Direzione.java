package it.uniroma1.textadv;

import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Rappresenta le varie direzioni in cui può muoversi il giocatore.
 */
public enum Direzione {

    NORD("north", "NORD", "N", "NORTH"),
    SUD("south", "SUD", "S", "SOUTH"),
    EST("est", "EST", "E"),
    OVEST("west", "OVEST", "O", "WEST", "W");

    /**
     * Possibili nomi con cui ci si può riferire alle varie direzioni.
     */
    private final Set<String> names;

    /**
     * Nome della direzione in inglese.
     */
    private final String eng;

    /**
     * Crea una direzione a partire dai suoi possibili nomi.
     *
     * @param eng   nome inglese della direzione
     * @param names sequenza di nomi
     */
    Direzione(String eng, String... names) {
        this.eng = eng;
        this.names = new HashSet<>(Arrays.asList(names));
    }

    /**
     * Restituisce una direzione dato un suo nome.
     *
     * @param name nome della direzione
     * @return direzione.
     */
    public static Direzione get(String name) {
        for (Direzione d : values()) {
            if (d.names.stream().anyMatch(name::equalsIgnoreCase))
                return d;
        }
        return null;
    }

    /**
     * Restituisce il nome della direzione nella lingua fornita.
     *
     * @param language la lingua di gioco
     * @return il nome della direzione
     */
    public String get(Language language) {
        if (language.equals(EnglishAndItalian.IT)) return toString().toLowerCase();
        else return eng;
    }
}
