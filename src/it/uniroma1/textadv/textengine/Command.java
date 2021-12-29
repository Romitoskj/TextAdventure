package it.uniroma1.textadv.textengine;

import it.uniroma1.textadv.*;
import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.oggetti.Container;
import it.uniroma1.textadv.oggetti.Oggetto;
import it.uniroma1.textadv.oggetti.Opener;
import it.uniroma1.textadv.personaggi.Cane;
import it.uniroma1.textadv.personaggi.Gatto;
import it.uniroma1.textadv.personaggi.Personaggio;

import static it.uniroma1.textadv.personaggi.Giocatore.getInstance;

import java.util.*;
import java.util.function.Function;

public class Command {

    private static final Map<String, Function<Command, String>> ACTIONS = Map.ofEntries(
            Map.entry("aiuto", Command::help),
            Map.entry("esci", Command::quit),
            Map.entry("guarda", Command::look),
            Map.entry("inventario",Command::showInventory),
            Map.entry("vai", Command::go),
            Map.entry("entra", Command::go),
            Map.entry("prendi",Command::collect),
            Map.entry("apri",Command::open),
            Map.entry("parla",Command::speak),
            Map.entry("accarezza",Command::pet)
            /*
            Map.entry("rompi",""),
            Map.entry("usa",""),
            Map.entry("dai","")
            */
    ); // oppure enum di function?

    private static final Set<String> STOP_WORDS = Set.of("la", "da", "su", "a");

    private final Function<Command, String> action;

    private final String[] arguments;

    public Command(String input) {
        // String[] words = input.split("\\s+", 2);
        String[] words = input.split("-+");
        action = ACTIONS.get(words[0]);
        arguments = Arrays.copyOfRange(words, 1, words.length);
    }

    @Override
    public String toString() {
        return "arguments=" + Arrays.toString(arguments);
    }

    public String run() {
        System.out.println(this);
        if (action == null) return "Comando non riconosciuto.";
        return action.apply(this);
    }

    private String help() {
        return "I comandi disponibili sono:\t" + new TreeSet<>(ACTIONS.keySet());
    }

    private String quit() {
        return "Arrivederci!";
    }

    private String showInventory() {
        return getInstance().getInventario();
    }

    private String go() {
        if (arguments.length == 0) return "Devi specificare dove vuoi andare.";
        Link link;
        Direzione dir = Direzione.get(arguments[0]);
        if (dir != null) {
            link = getInstance().getPosizione().getLink(dir);
            if (link == null) return "Non c'è nessun passaggio in questa direzione.";
        } else {
            link = getInstance().getPosizione().getLink(arguments[0]);
            if (link == null) return "Non c'è nessun passaggio chiamato così.";
        }
        if (getInstance().goThrough(link)) return "Sei in " + getInstance().getPosizione().getNome();
        return link.toString();
    }

    private String look() {
        Stanza stanza = getInstance().getPosizione();
        if (arguments.length > 0) {
            Oggetto oggetto = stanza.getOggetto(arguments[0]);
            if (oggetto != null) return oggetto.toString();
            else return "In questa stanza non c'è nulla del genere.";
        }
        return stanza.toString();
    }

    private String collect() {
        if (arguments.length == 0) return "Devi specificare cosa vuoi prendere.";
        String name = arguments[0];
        if (name.equalsIgnoreCase("navetta")) return go();
        if (arguments.length > 1) return collectFrom(name, arguments[1]);
        Named item = getInstance().getPosizione().getOggetto(name);
        if (item == null) item = getInstance().getPosizione().getPersonaggio(name);
        if (item == null) return "In questa stanza non c'è nulla del genere.";
        if (!(item instanceof Storable)) return "L'oggetto non può essere raccolto.";
        getInstance().getPosizione().removePersonaggio(name);
        getInstance().getPosizione().removeOggetto(name);
        getInstance().store((Storable) item);
        return "Oggetto aggiunto all'inventario!";
    }

    private String collectFrom(String name, String container) {
        Named item = getInstance().getPosizione().getOggetto(container);
        if (item == null) return "In questa stanza non c'è nulla del genere.";
        if (!(item instanceof Container)) return container + " non è un contenitore.";
        Container cont = (Container) item;
        if (!cont.isOpen()) return container + " è chiuso.";
        Storable toStore = cont.getContent(name);
        if (toStore == null) return container + " non contiene nessun " + name + ".";
        getInstance().store(toStore);
        return "Oggetto aggiunto all'inventario!";
    }

    private String open() {
        if (arguments.length == 0) return "Devi specificare cosa vuoi aprire.";
        Lockable toOpen;
        String name = arguments[0];

        Named item = getInstance().getPosizione().getOggetto(name);
        if (item == null) item = getInstance().getPosizione().getLink(name);
        if (item == null) return "In questa stanza non c'è nulla del genere.";
        if (!(item instanceof Lockable)) return "Non è possibile aprire quest'oggetto.";

        toOpen = (Lockable) item;
        if (toOpen.isOpen()) return "E' già aperto.";
        if (!toOpen.isUnlocked()) {
            if (arguments.length > 1) {
                item = getInstance().getItem(arguments[1]);
                if (item == null) return "Non hai nulla del genere.";
                if (!(item instanceof Opener)) return "Non si apre con quest'oggetto";
                toOpen.unlock((Opener) item);
                if (!toOpen.isUnlocked()) return "Non si apre con quest'oggetto.";
            } else return "Serve qualcosa per aprire quest'oggetto.";
        }
        toOpen.open();
        return "Fatto!";
    }

    private String speak(Personaggio personaggio) {
        if (personaggio == null) return "Non c'è nessuno chiamato così qui.";
        return personaggio.getNome() +":'" + personaggio.parla() + "'";
    }

    private String speak() {
        if (arguments.length == 0) return "Devi specificare con chi vuoi parlare.";
        return speak(getInstance().getPosizione().getPersonaggio(arguments[0]));
    }

    private String pet() {
        if (arguments.length == 0) return "Devi specificare chi vuoi accarezzare.";
        Personaggio personaggio = getInstance().getPosizione().getPersonaggio(arguments[0]);
        if (personaggio instanceof Gatto || personaggio instanceof Cane || personaggio == null) return speak(personaggio);
        return "Non è carino accarezzare una persona che non conosci!";
    }
}
