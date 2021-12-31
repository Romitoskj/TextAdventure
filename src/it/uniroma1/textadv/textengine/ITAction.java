package it.uniroma1.textadv.textengine;

import it.uniroma1.textadv.*;
import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.oggetti.Breakable;
import it.uniroma1.textadv.oggetti.Container;
import it.uniroma1.textadv.oggetti.Oggetto;
import it.uniroma1.textadv.oggetti.Opener;
import it.uniroma1.textadv.personaggi.Animal;
import it.uniroma1.textadv.personaggi.Personaggio;

import java.util.List;

import static it.uniroma1.textadv.personaggi.Giocatore.getInstance;

public enum ITAction implements Action {

    AIUTO("\t\tVisualizza la lista dei comandi o un comando specifico.") {
        @Override
        public String apply(List<String> args) {
            if (args.size() > 1) return "Questo comando accetta al massimo un argomento.";
            if (args.size() == 1) {
                try {
                    ITAction a = valueOf(args.get(0).toUpperCase());
                    return a.toString();
                } catch (IllegalArgumentException e) {
                    return "Non c'è nessun comando chiamato così...";
                }
            }
            StringBuilder res = new StringBuilder().append("COMANDO\t\t|\t\tDESCRIZIONE\n\n");
            for (ITAction a : values()) {
                res.append(a.toString()).append("\n");
            }
            return res.deleteCharAt(res.length() - 1).toString();
        }
    },

    ESCI("\t\tEsci dal gioco.") {
        @Override
        public String apply(List<String> args) {
            if (args.size() > 0) return "Questo comando non accetta argomenti.";
            return "Arrivederci!";
        }
    },

    GUARDA("\t\tDescrive l'oggetto richiesto, altrimenti, se non richiedi nulla, descrive la stanza in cui ti " +
            "trovi.") {
        @Override
        public String apply(List<String> args) {
            if (args.size() > 1) return "Puoi fornire al massimo un argomento.";
            Stanza stanza = getInstance().getPosizione();
            if (args.size() > 0) {
                Oggetto oggetto = stanza.getOggetto(args.get(0));
                if (oggetto != null) return oggetto.toString();
                else return "In questa stanza non c'è nulla del genere.";
            }
            return stanza.toString();
        }
    },

    INVENTARIO("\tVisualizza il tuo inventario.") {
        @Override
        public String apply(List<String> args) {
            if (args.size() > 0) return "Questo comando non accetta argomenti.";
            return getInstance().getInventario();
        }
    },

    VAI("\t\t\tUsalo per spostarti nelle varie stanze indicando il nome del passaggio per cui vuoi passare o la direzione (nord, sud, est, ovest)") {
        @Override
        public String apply(List<String> args) {
            if (args.size() == 0) return "Devi specificare dove vuoi andare.";
            if (args.size() > 1) return "Devi specificare un solo argomento.";
            Link link;
            Direzione dir = Direzione.get(args.get(0));
            if (dir != null) {
                link = getInstance().getPosizione().getLink(dir);
                if (link == null) return "Non c'è nessun passaggio in questa direzione.";
            } else {
                link = getInstance().getPosizione().getLink(args.get(0));
                if (link == null) return "Non c'è nessun passaggio chiamato così.";
            }
            if (getInstance().goThrough(link)) return "Sei in " + getInstance().getPosizione().getNome();
            return "Il passaggio è chiuso.";
        }
    },

    ENTRA("\t\tUsalo per entrare in un determinato passaggio.") {
        @Override
        public String apply(List<String> args) {
            if (args.size() == 0) return "Devi specificare dove vuoi andare.";
            if (args.size() > 1) return "Devi specificare un solo argomento.";
            Link link = getInstance().getPosizione().getLink(args.get(0));
            if (link == null) return "Non c'è nessun passaggio chiamato così.";
            if (getInstance().goThrough(link)) return "Sei in " + getInstance().getPosizione().getNome();
            return "Il passaggio è chiuso.";
        }
    },

    PRENDI("\t\tPrendi qualcosa dalla stanza in cui ti trovi, da dentro un'altro oggetto, o da un personaggio. Troverai ciò che hai preso nell'inventario.") {
        @Override
        public String apply(List<String> args) {
            if (args.size() == 0) return "Devi specificare cosa vuoi prendere.";
            if (args.size() > 2) return "Devi specificare massimo due argomenti.";
            String name = args.get(0);
            if (name.equalsIgnoreCase("navetta")) return ENTRA.apply(args);
            if (args.size() > 1) return collectFrom(name, args.get(1));
            Named item = getInstance().getPosizione().getOggetto(name);
            if (item == null) item = getInstance().getPosizione().getPersonaggio(name);
            if (item == null) return "In questa stanza non c'è nulla del genere...";
            if (!(item instanceof Storable)) return "Non può essere raccolto.";
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
            if (toStore == null) return name + " non contiene nessun " + name + ".";
            getInstance().store(toStore);
            return "Oggetto aggiunto all'inventario!";
        }
    },

    LASCIA("\t\tLascia un oggetto che hai nell'inventario nella stanza in cui ti trovi.") {
        @Override
        public String apply(List<String> args) {
            if (args.size() == 0) return "Devi specificare l'oggetto da lasciare.";
            if (args.size() > 1) return "Puoi lasciare solo un oggetto per volta.";
            if (getInstance().lascia(args.get(0))) return "Oggetto lasciato!";
            return "Non hai nulla del genere...";
        }
    },

    APRI("\t\tApri un oggetto o un passaggio chiuso. Se è chiuso a chiave o bloccato dovrai specificare con cosa aprirlo.") {
        @Override
        public String apply(List<String> args) {
            if (args.size() == 0) return "Devi specificare cosa vuoi aprire.";
            Lockable toOpen;
            String name = args.get(0);

            Named item = getInstance().getPosizione().getOggetto(name);
            if (item == null) item = getInstance().getPosizione().getLink(name);
            if (item == null) return "In questa stanza non c'è nulla del genere.";
            if (!(item instanceof Lockable)) return "Non è possibile aprire quest'oggetto.";

            toOpen = (Lockable) item;
            boolean brkble = item instanceof Breakable;
            if (toOpen.isOpen()) return "E' già " + (brkble ? "rotto" : "aperto") + "!";
            if (!toOpen.isUnlocked()) {
                if (args.size() < 2)
                    return "Serve qualcosa per " + (brkble ? "rompere" : "aprire") + " quest'oggetto...";
                if (args.size() > 2)
                    return "Devi specificare un solo oggetto che " + (brkble ? "rompe" : "apre") + " il primo.";
                item = getInstance().getItem(args.get(1));
                if (item == null) return "Non hai nulla del genere...";
                if (!(item instanceof Opener)) return "Non si " + (brkble ? "rompe" : "apre") + " con quest'oggetto...";
                toOpen.unlock((Opener) item);
                if (!toOpen.isUnlocked()) return "Non si " + (brkble ? "rompe" : "apre") + " con quest'oggetto...";
            } else if (args.size() > 1) return "Non è bloccato, " + (brkble ? "rompilo" : "aprilo") + " e basta!";
            toOpen.open();
            return "Fatto!";
        }
    },

    ROMPI("\t\tUsalo per rompere un oggetto nella stanza dove ti trovi (non tutti gli oggetti possono essere rotti.") {
        @Override
        public String apply(List<String> args) {
            if (args.size() == 0) return "Devi specificare cosa vuoi aprire.";
            if (args.size() > 2)
                return "Devi specificare solo due oggetti, uno da rompere e uno con cui rompere il primo.";
            Oggetto o = getInstance().getPosizione().getOggetto(args.get(0));
            if (o == null) return "Non c'è nessun oggetto chiamato così qui...";
            if (!(o instanceof Breakable)) return args.get(0) + " non può essere rotto.";
            return APRI.apply(args);
        }
    },

    PARLA("\t\tUsalo per dialogare con altri personaggi.") {
        @Override
        public String apply(List<String> args) {
            if (args.size() == 0) return "Devi specificare con chi vuoi parlare.";
            if (args.size() > 1) return "Puoi parlare solo ad un personaggio per volta.";
            Personaggio personaggio = getInstance().getPosizione().getPersonaggio(args.get(0));
            if (personaggio == null) return "Non c'è nessuno chiamato così qui...";
            return personaggio.getNome() + ":'" + personaggio.parla() + "'";
        }
    },

    ACCAREZZA("\tUsalo per accarezzare un animale.") {
        @Override
        public String apply(List<String> args) {
            if (args.size() == 0) return "Devi specificare chi vuoi accarezzare.";
            if (args.size() > 1) return "Puoi accarezzare solo un animale per volta.";
            Personaggio personaggio = getInstance().getPosizione().getPersonaggio(args.get(0));
            if (personaggio == null) return "Non c'è nessuno chiamato così qui...";
            if (!(personaggio instanceof Animal)) return "Non è carino accarezzare una persona che non conosci!";
            return personaggio.getNome() + ":'" + personaggio.parla() + "'";
        }
    },

    DAI("\t\t\tUsalo per dare un oggetto ad un altro personaggio.") {
        @Override
        public String apply(List<String> args) {
            if (args.size() < 2) return "Devi specificare cosa vuoi dare e a chi.";
            if (args.size() > 2) return "Devi specificare solamente cosa dare e a chi.";
            String itemName = args.get(0);
            Personaggio p = getInstance().getPosizione().getPersonaggio(args.get(1));
            if (p == null) return "Non c'è nessuno chiamato così qui...";
            if (!getInstance().dai(itemName, p)) return "Non hai nulla del genere...";
            return itemName + " dato a " + p.getNome() + ".";
        }
    },

    // TODO USA
    USA("\t\t\tServe ad usare un oggetto che porti nell'inventario o che si trova nella stanza.") {
        @Override
        public String apply(List<String> args) {
            return null;
        }
    };

    private final String DESCRIPTION;

    ITAction(String description) {
        DESCRIPTION = description;
    }

    @Override
    public String toString() {
        return super.toString() + DESCRIPTION;
    }
}
