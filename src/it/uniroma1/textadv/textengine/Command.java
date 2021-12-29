package it.uniroma1.textadv.textengine;

import it.uniroma1.textadv.*;
import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.oggetti.*;
import it.uniroma1.textadv.personaggi.Cane;
import it.uniroma1.textadv.personaggi.Gatto;
import it.uniroma1.textadv.personaggi.Personaggio;

import static it.uniroma1.textadv.personaggi.Giocatore.getInstance;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            Map.entry("rompi",Command::open),
            Map.entry("parla",Command::speak),
            Map.entry("accarezza",Command::pet),
            Map.entry("dai",Command::give),
            Map.entry("usa",Command::use)
    ); // oppure enum di function?

    private static final Set<String> STOP_WORDS = Set.of("il", "lo", "la", "i", "gli", "le", "un", "uno", "una", /*"di",*/
            /*"del",*/ "dello", "della", "dei", "degli", "delle", "a", "al", "allo", "alla", "ai", "agli", "alle", "da",
            "dal", "dallo", "dalla", "dai", "dagli", "dalle", "in", "nel", "nello", "nella", "nei", "negli", "nelle",
            "su", "sul", "sullo", "sulla", "sui", "sugli", "sulle", "con");

    private final Function<Command, String> action;

    private final List<String> arguments;

    public Command(String input) {
        // String[] words = input.split("\\s+", 2);
        String[] words = input.split("-+");
        action = ACTIONS.get(words[0]);
        arguments = Arrays.stream(words)
                .skip(1)
                .filter(w -> !STOP_WORDS.contains(w))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "*".repeat(40)+ " arguments=" + arguments;
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
        if (arguments.size() == 0) return "Devi specificare dove vuoi andare.";
        Link link;
        Direzione dir = Direzione.get(arguments.get(0));
        if (dir != null) {
            link = getInstance().getPosizione().getLink(dir);
            if (link == null) return "Non c'è nessun passaggio in questa direzione.";
        } else {
            link = getInstance().getPosizione().getLink(arguments.get(0));
            if (link == null) return "Non c'è nessun passaggio chiamato così.";
        }
        if (getInstance().goThrough(link)) return "Sei in " + getInstance().getPosizione().getNome();
        return link.toString();
    }

    private String look() {
        Stanza stanza = getInstance().getPosizione();
        if (arguments.size() > 0) {
            Oggetto oggetto = stanza.getOggetto(arguments.get(0));
            if (oggetto != null) return oggetto.toString();
            else return "In questa stanza non c'è nulla del genere.";
        }
        return stanza.toString();
    }

    private String collect() {
        if (arguments.size() == 0) return "Devi specificare cosa vuoi prendere.";
        String name = arguments.get(0);
        if (name.equalsIgnoreCase("navetta")) return go();
        if (arguments.size() > 1) return collectFrom(name, arguments.get(1));
        Named item = getInstance().getPosizione().getOggetto(name);
        if (item == null) item = getInstance().getPosizione().getPersonaggio(name);
        if (item == null) return "In questa stanza non c'è nulla del genere...";
        if (!(item instanceof Storable)) return "L'oggetto non può essere raccolto.";
        getInstance().getPosizione().removePersonaggio(name);
        getInstance().getPosizione().removeOggetto(name);
        getInstance().store((Storable) item);
        return "Oggetto aggiunto all'inventario!";
    }

    private String collectFrom(String name, String container) {
        Named item = getInstance().getPosizione().getOggetto(container);
        if (item == null) item = getInstance().getPosizione().getPersonaggio(container);
        if (item == null) return "In questa stanza non c'è nulla del genere...";
        if (item instanceof Personaggio) return collectFromCharacter(name, (Personaggio) item);
        if (item instanceof Container) return collectFromContainer(name, (Container) item);
        return container + " non è né un contenitore né un personaggio.";
    }

    private String collectFromCharacter(String name, Personaggio p) {
        if (!p.dai(name, getInstance())) return p.getNome() + " non ha nulla del genere...";
        return p.getNome() + " ti ha dato " + name + ".";
    }

    private String collectFromContainer(String name, Container container) {
        if (!container.isOpen()) return container + " è chiuso.";
        Storable toStore = container.getContent(name);
        if (toStore == null) return container + " non contiene nessun " + name + ".";
        getInstance().store(toStore);
        return "Oggetto aggiunto all'inventario!";
    }

    private String open() {
        if (arguments.size() == 0) return "Devi specificare cosa vuoi aprire.";
        Lockable toOpen;
        String name = arguments.get(0);

        Named item = getInstance().getPosizione().getOggetto(name);
        if (item == null) item = getInstance().getPosizione().getLink(name);
        if (item == null) return "In questa stanza non c'è nulla del genere.";
        if (!(item instanceof Lockable)) return "Non è possibile aprire quest'oggetto.";

        toOpen = (Lockable) item;
        if (toOpen.isOpen()) return "E' già aperto.";
        if (!toOpen.isUnlocked()) {
            if (arguments.size() < 2) return "Serve qualcosa per aprire quest'oggetto.";
            item = getInstance().getItem(arguments.get(1));
            if (item == null) return "Non hai nulla del genere...";
            if (!(item instanceof Opener)) return "Non si apre con quest'oggetto...";
            toOpen.unlock((Opener) item);
            if (!toOpen.isUnlocked()) return "Non si apre con quest'oggetto...";
        }
        toOpen.open();
        return "Fatto!";
    }

    private String speak(Personaggio personaggio) {
        if (personaggio == null) return "Non c'è nessuno chiamato così qui...";
        return personaggio.getNome() +":'" + personaggio.parla() + "'";
    }

    private String speak() {
        if (arguments.size() == 0) return "Devi specificare con chi vuoi parlare.";
        return speak(getInstance().getPosizione().getPersonaggio(arguments.get(0)));
    }

    private String pet() {
        if (arguments.size() == 0) return "Devi specificare chi vuoi accarezzare.";
        Personaggio personaggio = getInstance().getPosizione().getPersonaggio(arguments.get(0));
        if (personaggio instanceof Gatto || personaggio instanceof Cane || personaggio == null) return speak(personaggio);
        return "Non è carino accarezzare una persona che non conosci!";
    }

    private String give() {
        if (arguments.size() < 2) return "Devi specificare cosa vuoi dare e a chi.";
        String itemName = arguments.get(0);
        Personaggio p = getInstance().getPosizione().getPersonaggio(arguments.get(1));
        if (p == null) return "Non c'è nessuno chiamato così qui...";
        if (!getInstance().dai(itemName, p)) return "Non hai nulla del genere...";
        return itemName + " dato a " + p.getNome() + ".";
    }

    private String use() {
        if (arguments.size() == 0) return "Devi specificare cosa vuoi usare.";
        Storable toUse = getInstance().getItem(arguments.get(0));
        Link l = getInstance().getPosizione().getLink(arguments.get(0));
        if (l != null) return go();
        if(toUse == null) return "Non hai nulla del genere...";
        if (arguments.size() > 1) {
            if (arguments.get(1).equalsIgnoreCase("pozzo") && arguments.get(0).equalsIgnoreCase("secchio")) {
                return riempi();
            }
            if (toUse instanceof Opener) {
                String s = arguments.get(0);
                arguments.set(0, arguments.get(1));
                arguments.set(1, s);
                return open();
            }
        }
        return "Non fa niente...";
    }

    private String riempi() {
        Storable item = getInstance().getItem("secchio");
        if (item == null) return "Non hai un secchio da riempire...";
        Secchio s = (Secchio) item;
        Object o = getInstance().getPosizione().getOggetto("pozzo");
        if (o == null) return "Non c'è un pozzo qui...";
        s.riempi((Pozzo) o);
        return "Hai riempito il secchio";
    }
}
