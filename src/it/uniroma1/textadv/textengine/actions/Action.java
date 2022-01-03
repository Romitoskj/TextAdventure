package it.uniroma1.textadv.textengine.actions;

import it.uniroma1.textadv.Direzione;
import it.uniroma1.textadv.Lockable;
import it.uniroma1.textadv.Item;
import it.uniroma1.textadv.Storable;
import it.uniroma1.textadv.exceptions.ActionNotKnownException;
import it.uniroma1.textadv.exceptions.ItemNotPresentException;
import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.links.Teletrasporto;
import it.uniroma1.textadv.oggetti.*;
import it.uniroma1.textadv.personaggi.Animal;
import it.uniroma1.textadv.personaggi.Personaggio;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.List;

import static it.uniroma1.textadv.personaggi.Giocatore.getInstance;

public interface Action {

    String getDescription();

    String execute(List<String> args);

    static String help(List<String> args, Language language) {
        if (args.size() > 1) return "Questo comando accetta al massimo un argomento.";
        ActionFactory actionFactory = language.getActionFactory();
        if (args.size() == 1) {
            String actionName = args.get(0).toUpperCase();
            try {
                Action a = actionFactory.getAction(actionName);
                return a.getDescription();
            } catch (ActionNotKnownException e) {
                return "Non c'è nessun comando chiamato così...";
            }
        }
        StringBuilder res = new StringBuilder().append("COMANDO\t\t|\t\tDESCRIZIONE\n\n");
        for (Action a : actionFactory.values()) {
            res.append(a.getDescription()).append("\n");
        }
        return res.deleteCharAt(res.length() - 1).toString();
    }

    static String quit(List<String> args, Language language) {
        if (args.size() > 0) return "Questo comando non accetta argomenti.";
        return "Arrivederci!";
    }

    // TODO look nell'inventario
    static String look(List<String> args, Language language) {
        if (args.size() > 1) return "Puoi fornire al massimo un argomento.";
        if (args.size() > 0) {
            try {
                return getInstance().searchItem(args.get(0)).getDescription(language) + ".";
            } catch (ItemNotPresentException e) {
                return "In questa stanza non c'è nulla del genere.";
            }
        }
        return getInstance().getPosizione().getDescription(language);
    }

    static String inventory(List<String> args, Language language) {
        if (args.size() > 0) return "Questo comando non accetta argomenti.";
        return getInstance().getInventario();
    }

    static String go(List<String> args, Language language) {
        if (args.size() == 0) return "Devi specificare dove vuoi andare.";
        if (args.size() > 1) return "Devi specificare un solo argomento.";
        Link link;
        Direzione dir = Direzione.get(args.get(0));
        if (dir != null) {
            try {
                link = getInstance().searchLink(dir);
            } catch (ItemNotPresentException e) {
                return "Non c'è nessun passaggio in questa direzione.";
            }
        } else {
            try {
                link = getInstance().searchLink(args.get(0));
            } catch (ItemNotPresentException e) {
                return "Non c'è nessun passaggio chiamato così.";
            }
        }
        if (getInstance().goThrough(link)) return "Sei in " + getInstance().getPosizione().getNome() + ".";
        return "Il passaggio " + link.getNome() + " è chiuso.";
    }

    static String enter(List<String> args, Language language) {
        if (args.size() == 0) return "Devi specificare dove vuoi andare.";
        if (args.size() > 1) return "Devi specificare un solo argomento.";
        Link link;
        try {
            link = getInstance().searchLink(args.get(0));
            if (getInstance().goThrough(link)) return "Sei in " + getInstance().getPosizione().getNome() + ".";
            return "Il passaggio è chiuso.";
        } catch (ItemNotPresentException e) {
            return "Non c'è nessun passaggio chiamato così.";
        }
    }

    static String take(List<String> args, Language language) {
        if (args.size() == 0) return "Devi specificare cosa vuoi prendere.";
        if (args.size() > 2) return "Devi specificare massimo due argomenti.";
        String name = args.get(0);
        if (args.size() > 1) return collectFrom(name, args.get(1), language);
        try {
            Item item = getInstance().searchItem(name);
            if (item instanceof Link) return enter(args, language);
            if (!getInstance().takeFromRoom(name)) return "Non puoi prenderlo...";
            return "Oggetto aggiunto all'inventario!";
        } catch (ItemNotPresentException e) {
            return "In questa stanza non c'è nulla del genere...";
        }
    }

    private static String collectFrom(String name, String from, Language language) {
        try {
            Item item = getInstance().searchItem(from);
            if (item instanceof Personaggio) return collectFromCharacter(name, (Personaggio) item, language);
            if (item instanceof Container) return collectFromContainer(name, (Container) item, language);
            return from + " non è né un contenitore né un personaggio.";
        } catch (ItemNotPresentException e) {
            return "In questa stanza non c'è nulla del genere...";
        }
    }

    private static String collectFromCharacter(String name, Personaggio p, Language language) {
        try {
            p.dai(name, getInstance());
            return p.getNome() + " ti ha dato " + name + ".";
        } catch (ItemNotPresentException e) {
            return p.getNome() + " non ha nulla del genere...";
        }
    }

    private static String collectFromContainer(String name, Container container, Language language) {
        if (!container.isOpen()) return container + " è chiuso."; // TODO acceso se camino
        try {
            Storable toStore = container.takeContent(name);
            if(toStore != null) getInstance().store(toStore);
            return "Oggetto aggiunto all'inventario!";
        } catch (ItemNotPresentException e) {
            return container.getNome() + " non contiene " + name + ".";
        }
    }

    static String drop(List<String> args, Language language) {
        if (args.size() == 0) return "Devi specificare l'oggetto da lasciare.";
        if (args.size() > 1) return "Puoi lasciare solo un oggetto per volta.";
        try {
            getInstance().lascia(args.get(0));
            return "Oggetto lasciato!";
        } catch (ItemNotPresentException e) {
            return "Non hai nulla del genere...";
        }
    }

    static String open(List<String> args, Language language) {
        if (args.size() == 0) return "Devi specificare cosa vuoi aprire.";
        Lockable toOpen;
        String name = args.get(0);

        Item item;
        try {
            item = getInstance().searchItem(name);
        } catch (ItemNotPresentException e) {
            return "In questa stanza non c'è nulla del genere.";
        }
        if (!(item instanceof Lockable)) return "Non è possibile aprire quest'oggetto.";

        toOpen = (Lockable) item;
        boolean breakable = item instanceof Breakable;
        if (toOpen.isOpen()) return "E' già " + (breakable ? "rotto" : "aperto") + "!";
        if (!toOpen.isUnlocked()) {
            if (args.size() < 2)
                return "Serve qualcosa per " + (breakable ? "rompere" : "aprire") + " quest'oggetto...";
            if (args.size() > 2)
                return "Devi specificare un solo oggetto che " + (breakable ? "rompe" : "apre") + " il primo.";
            try {
                item = getInstance().getInventoryItem(args.get(1));
            } catch (ItemNotPresentException e) {
                return "Non hai nulla del genere...";
            }
            if (!(item instanceof Opener)) return "Non si " + (breakable ? "rompe" : "apre") + " con quest'oggetto...";
            toOpen.unlock((Opener) item);
            if (!toOpen.isUnlocked()) return "Non si " + (breakable ? "rompe" : "apre") + " con quest'oggetto...";
        } else if (args.size() > 1) return "Non è bloccato, " + (breakable ? "rompilo" : "aprilo") + " e basta!";
        toOpen.open();
        return "Fatto!"; // TODO sostituire con stringa migliore
    }

    static String breakItem(List<String> args, Language language) {
        if (args.size() == 0) return "Devi specificare cosa vuoi rompere.";
        if (args.size() > 2)
            return "Devi specificare solo due oggetti, uno da rompere e uno con cui rompere il primo.";
        try {
            Oggetto o =  (Oggetto) getInstance().searchItem(args.get(0));
            if (!(o instanceof Breakable)) return args.get(0) + " non può essere rotto.";
            return open(args, language);
        } catch (ClassCastException | ItemNotPresentException e) {
            return "Non c'è nessun oggetto chiamato così qui...";
        }
    }

    static String speak(List<String> args, Language language) {
        if (args.size() == 0) return "Devi specificare con chi vuoi parlare.";
        if (args.size() > 1) return "Puoi parlare solo ad un personaggio per volta.";
        try {
            Personaggio personaggio = (Personaggio) getInstance().searchItem(args.get(0));
            return personaggio.getNome() + ":'" + personaggio.parla(language) + "'";
        } catch (ClassCastException | ItemNotPresentException e) {
            return "Non c'è nessuno chiamato così qui...";
        }
    }

    static String pet(List<String> args, Language language) {
        if (args.size() == 0) return "Devi specificare chi vuoi accarezzare.";
        if (args.size() > 1) return "Puoi accarezzare solo un animale per volta.";
        try {
            Personaggio personaggio = (Personaggio) getInstance().searchItem(args.get(0));
            if (!(personaggio instanceof Animal)) return "Non è carino accarezzare una persona che non conosci!";
            return personaggio.getNome() + ":'" + personaggio.parla(language) + "'";
        } catch (ClassCastException | ItemNotPresentException e) {
            return "Non c'è nessuno chiamato così qui...";
        }
    }

    static String give(List<String> args, Language language) {
        if (args.size() < 2) return "Devi specificare cosa vuoi dare e a chi.";
        if (args.size() > 2) return "Devi specificare solamente cosa dare e a chi.";
        String itemName = args.get(0);
        try {
            Personaggio p = (Personaggio) getInstance().searchItem(args.get(1));
            try {
                getInstance().dai(itemName, p);
                return itemName + " dato a " + p.getNome() + ".";
            } catch (ItemNotPresentException e) {
                return "Non hai nulla del genere...";
            }
        } catch (ClassCastException | ItemNotPresentException e) {
            return "Non c'è nessuno chiamato così qui...";
        }
    }

    static String use(List<String> args, Language language) {
        if (args.size() == 0) return "Devi specificare cosa vuoi usare.";
        Item item , link;
        try {
            item = getInstance().getInventoryItem(args.get(0));
            if (item instanceof Opener) {
                if (args.size() < 2) return "Devi specificare su cosa usare l'oggetto.";
                if (item instanceof Secchio) {
                    Secchio secchio = (Secchio) item;
                    if (!secchio.isPieno()) return riempi(secchio, args.get(1), language); // TODO e secondo oggetto !instanceof Camino
                }
                link = getInstance().searchItem(args.get(1));
                if (link instanceof Teletrasporto) {
                    Teletrasporto t = (Teletrasporto) link;
                    String open = open(List.of(args.get(1), args.get(0)), language);
                    if (t.isOpen()) return go(List.of(t.getNome()), language);
                    return open;
                }
                return open(List.of(args.get(1), args.get(0)), language);
            }
            return "Sembra non faccia nulla...";
        } catch (ItemNotPresentException e) {
            try {
                getInstance().searchLink(args.get(0));
                return go(args, language);
            } catch (ItemNotPresentException ex) {
                return "Non hai nulla del genere e non c'è un passaggio chiamato così...";
            }
        }
    }

    private static String riempi(Secchio s, String arg, Language language) {
        try {
            s.riempi((Pozzo) getInstance().searchItem(arg));
            return "Secchio riempito!";
        } catch (ItemNotPresentException e) {
            return "In questa stanza non c'è nulla del genere...";
        } catch (ClassCastException e) {
            return "Puoi riempire il secchio solo al pozzo...";
        }
    }

}
