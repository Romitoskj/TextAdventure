package it.uniroma1.textadv.textengine.actions;

import it.uniroma1.textadv.Direzione;
import it.uniroma1.textadv.interfaces.Lockable;
import it.uniroma1.textadv.interfaces.Item;
import it.uniroma1.textadv.interfaces.Storable;
import it.uniroma1.textadv.oggetti.interfaces.Breakable;
import it.uniroma1.textadv.oggetti.interfaces.Container;
import it.uniroma1.textadv.oggetti.interfaces.Oggetto;
import it.uniroma1.textadv.oggetti.interfaces.Opener;
import it.uniroma1.textadv.textengine.exceptions.ActionNotKnownException;
import it.uniroma1.textadv.exceptions.ItemNotPresentException;
import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.links.Teletrasporto;
import it.uniroma1.textadv.oggetti.*;
import it.uniroma1.textadv.personaggi.interfaces.Animal;
import it.uniroma1.textadv.personaggi.interfaces.Personaggio;
import it.uniroma1.textadv.personaggi.Venditore;
import it.uniroma1.textadv.textengine.Command;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.List;

import static it.uniroma1.textadv.personaggi.Giocatore.getInstance;

public interface Action {

    String getDescription();

    String execute(List<String> args);

    static String help(List<String> args, Language l) {
        if (args.size() > 1) return l.getAnswer("arg_1");
        ActionFactory actionFactory = Command.getFactory();
        if (args.size() == 1) {
            String actionName = args.get(0).toUpperCase();
            try {
                Action a = actionFactory.getAction(actionName);
                return a.getDescription();
            } catch (ActionNotKnownException e) {
                return l.getAnswer("cmd_not_found");
            }
        }
        StringBuilder res = new StringBuilder().append(l.getAnswer("help_headers"));
        for (Action a : actionFactory.values()) {
            res.append(a.getDescription()).append("\n");
        }
        return res.deleteCharAt(res.length() - 1).toString();
    }

    static String quit(List<String> args, Language l) {
        if (args.size() > 0) return l.getAnswer("arg_0");
        return l.getAnswer("greeting");
    }

    static String look(List<String> args, Language l) {
        if (args.size() > 1) return l.getAnswer("arg_1");
        if (args.size() > 0) {
            try {
                return getInstance().searchItem(args.get(0)).getDescription(l) + ".";
            } catch (ItemNotPresentException e) {
                return l.getAnswer("item_not_found");
            }
        }
        return getInstance().getPosizione().getDescription(l);
    }

    static String inventory(List<String> args, Language l) {
        if (args.size() > 0) return l.getAnswer("arg_0");
        return getInstance().getInventario(l);
    }

    static String go(List<String> args, Language l) {
        if (args.size() == 0) return l.getAnswer("go_arg_0");
        if (args.size() > 1) return l.getAnswer("go_too_many_args");
        Link link;
        Direzione dir = Direzione.get(args.get(0));
        if (dir != null) {
            try {
                link = getInstance().searchLink(dir);
            } catch (ItemNotPresentException e) {
                return l.getAnswer("go_d_link_not_found");
            }
        } else {
            try {
                link = getInstance().searchLink(args.get(0));
            } catch (ItemNotPresentException e) {
                return l.getAnswer("go_n_link_not_found");
            }
        }
        if (getInstance().goThrough(link)) return l.getAnswer("go_succ").formatted(getInstance().getPosizione().getName());
        return l.getAnswer("go_fail").formatted(link.getName());
    }

    static String enter(List<String> args, Language l) {
        if (args.size() == 0) return l.getAnswer("go_arg_0");
        if (args.size() > 1) return l.getAnswer("go_too_many_args");
        Link link;
        try {
            link = getInstance().searchLink(args.get(0));
            if (getInstance().goThrough(link)) return l.getAnswer("go_succ").formatted(getInstance().getPosizione().getName());
            return l.getAnswer("go_fail").formatted(link.getName());
        } catch (ItemNotPresentException e) {
            return l.getAnswer("go_n_link_not_found");
        }
    }

    static String take(List<String> args, Language l) {
        if (args.size() == 0) return l.getAnswer("take_args_0");
        if (args.size() > 2) return l.getAnswer("take_too_many_args");
        String name = args.get(0);
        if (args.size() > 1) return collectFrom(name, args.get(1), l);
        try {
            Item item = getInstance().searchItem(name);
            if (item instanceof Link) return enter(args, l);
            if (getInstance().takeFromRoom(name)) return l.getAnswer("take_succ");
            return l.getAnswer("take_not_storable");

        } catch (ItemNotPresentException e) {
            return l.getAnswer("item_not_found");
        }
    }

    private static String collectFrom(String name, String from, Language l) {
        try {
            Item item = getInstance().searchItem(from);
            if (item instanceof Personaggio) return collectFromCharacter(name, (Personaggio) item, l);
            if (item instanceof Container) return collectFromContainer(name, (Container) item, l);
            return l.getAnswer("collect_not_char_cont").formatted(from);
        } catch (ItemNotPresentException e) {
            return l.getAnswer("item_not_found");
        }
    }

    private static String collectFromCharacter(String name, Personaggio p, Language l) {
        try {
            p.dai(name, getInstance());
            return l.getAnswer("take_from_char_succ").formatted(p.getName(), name);
        } catch (ItemNotPresentException e) {
            return l.getAnswer("take_from_char_fail").formatted(p.getName());
        }
    }

    private static String collectFromContainer(String name, Container container, Language l) {
        if (!container.isOpen())
            return l.getAnswer("take_from_cont_closed").formatted(container,  l.getAnswer((container instanceof Camino ? "lit" : "closed")));
        try {
            Storable toStore = container.takeContent(name);
            if (toStore != null) getInstance().store(toStore);
            return l.getAnswer("take_succ");
        } catch (ItemNotPresentException e) {
            return l.getAnswer("container_fail").formatted(container.getName(), name);
        }
    }

    static String drop(List<String> args, Language l) {
        if (args.size() == 0) return l.getAnswer("drop_args_0");
        if (args.size() > 1) return l.getAnswer("drop_too_many_args");
        try {
            getInstance().lascia(args.get(0));
            return l.getAnswer("drop_succ");
        } catch (ItemNotPresentException e) {
            return l.getAnswer("not_found_in_inventory");
        }
    }

    static String open(List<String> args, Language l) {
        if (args.size() == 0) return l.getAnswer("open_args_0");
        Lockable toOpen;
        String name = args.get(0);

        Item item;
        try {
            item = getInstance().searchItem(name);
        } catch (ItemNotPresentException e) {
            return l.getAnswer("item_not_found");
        }
        if (!(item instanceof Lockable)) return l.getAnswer("open_not_loackable");

        toOpen = (Lockable) item;
        boolean breakable = item instanceof Breakable;
        if (toOpen.isOpen()) return l.getAnswer("open_already_open").formatted(l.getAnswer((breakable ? "broken" : "opened")));
        if (!toOpen.isUnlocked()) {
            if (args.size() < 2)
                return l.getAnswer("open_args_1").formatted(l.getAnswer((breakable ? "break" : "open")));
            if (args.size() > 2)
                return l.getAnswer("open_too_many_args").formatted(l.getAnswer((breakable ? "break" : "open")));
            try {
                item = getInstance().getInventoryItem(args.get(1));
            } catch (ItemNotPresentException e) {
                return l.getAnswer("not_found_in_inventory");
            }
            if (!(item instanceof Opener)) return l.getAnswer("open_wrong_opener").formatted(l.getAnswer((breakable ? "breaks" : "opens")));
            toOpen.unlock((Opener) item);
            if (!toOpen.isUnlocked()) return l.getAnswer("open_wrong_opener").formatted((breakable ? "breaks" : "opens"));
        } else if (args.size() > 1) return l.getAnswer("open_unlocked").formatted(l.getAnswer((breakable ? "break_it" : "open_it")));
        toOpen.open();
        if (toOpen instanceof Camino) return l.getAnswer("fire_succ");
        return l.getAnswer(breakable? "break_succ" : "open_succ");
    }

    static String breakItem(List<String> args, Language l) {
        if (args.size() == 0) return l.getAnswer("break_args_0");
        if (args.size() > 2)
            return l.getAnswer("break_too_many_args");
        try {
            Oggetto o = (Oggetto) getInstance().searchItem(args.get(0));
            if (!(o instanceof Breakable)) return l.getAnswer("break_not_breakable").formatted(args.get(0));
            return open(args, l);
        } catch (ClassCastException | ItemNotPresentException e) {
            return l.getAnswer("item_not_found");
        }
    }

    static String speak(List<String> args, Language l) {
        if (args.size() == 0) return l.getAnswer("speak_args_0");
        if (args.size() > 1) return l.getAnswer("speak_too_many_args");
        try {
            Personaggio personaggio = (Personaggio) getInstance().searchItem(args.get(0));
            return "%s:'%s'".formatted(personaggio.getName(), personaggio.parla(l));
        } catch (ClassCastException | ItemNotPresentException e) {
            return l.getAnswer("char_not_found");
        }
    }

    static String pet(List<String> args, Language l) {
        if (args.size() == 0) return l.getAnswer("pet_args_0");
        if (args.size() > 1) return l.getAnswer("pet_too_many_args");
        try {
            Personaggio personaggio = (Personaggio) getInstance().searchItem(args.get(0));
            if (!(personaggio instanceof Animal)) return l.getAnswer("pet_person");
            return "%s:'%s'".formatted(personaggio.getName(), personaggio.parla(l));
        } catch (ClassCastException | ItemNotPresentException e) {
            return l.getAnswer("char_not_found");
        }
    }

    static String give(List<String> args, Language l) {
        if (args.size() < 2) return l.getAnswer("give_few_args");
        if (args.size() > 2) return l.getAnswer("give_too_many_args");
        String itemName = args.get(0);
        try {
            Personaggio p = (Personaggio) getInstance().searchItem(args.get(1));
            try {
                getInstance().dai(itemName, p);
                if (p instanceof Venditore) {
                    if (itemName.equals(((Venditore) p).getNeeded())) return l.getAnswer("give_vendor").formatted(p.getName());
                }
                return l.getAnswer("give_succ") .formatted(itemName, p.getName());
            } catch (ItemNotPresentException e) {
                return l.getAnswer("not_found_in_inventory");
            }
        } catch (ClassCastException | ItemNotPresentException e) {
            return l.getAnswer("char_not_found");
        }
    }

    static String use(List<String> args, Language l) {
        if (args.size() == 0) return l.getAnswer("use_args_0");
        Item item, link;
        try {
            item = getInstance().getInventoryItem(args.get(0));
            if (item instanceof Opener) {
                if (args.size() < 2) return l.getAnswer("use_args_1");
                if (args.size() > 2) return l.getAnswer("use_too_many_args");
                try {
                    link = getInstance().searchItem(args.get(1));
                } catch (ItemNotPresentException e) {
                    return l.getAnswer("use_not_found");
                }
                if (item instanceof Secchio) {
                    Secchio secchio = (Secchio) item;
                    if (!secchio.isPieno() && !(link instanceof Lockable))
                        return riempi(secchio, args.get(1), l);
                }
                if (link instanceof Teletrasporto) {
                    Teletrasporto t = (Teletrasporto) link;
                    String open = open(List.of(args.get(1), args.get(0)), l);
                    if (t.isOpen()) return go(List.of(t.getName()), l);
                    return open;
                }
                return open(List.of(args.get(1), args.get(0)), l);
            }
            return l.getAnswer("use_fail");
        } catch (ItemNotPresentException e) {
            try {
                getInstance().searchLink(args.get(0));
                return go(args, l);
            } catch (ItemNotPresentException ex) {
                return l.getAnswer("use_not_found_inventory");
            }
        }
    }

    private static String riempi(Secchio s, String arg, Language l) {
        try {
            s.riempi((Pozzo) getInstance().searchItem(arg));
            return l.getAnswer("fill_succ");
        } catch (ItemNotPresentException e) {
            return l.getAnswer("item_not_found");
        } catch (ClassCastException e) {
            return l.getAnswer("fill_fail");
        }
    }

}
