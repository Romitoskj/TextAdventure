package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.Mondo;
import it.uniroma1.textadv.interfaces.Observer;
import it.uniroma1.textadv.interfaces.Storable;
import it.uniroma1.textadv.exceptions.ItemNotPresentException;
import it.uniroma1.textadv.interfaces.Subject;
import it.uniroma1.textadv.personaggi.interfaces.Personaggio;
import it.uniroma1.textadv.textengine.Command;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un venditore ossia un personaggio che può vendere degli oggetti in cambio di qualcosa.
 */
public class Venditore extends Personaggio implements Observer {

    /**
     * Lista degli oggetti che vende.
     */
    private final List<Subject> toSell = new ArrayList<>();

    /**
     * Nome di cosa vuole in cambio degli oggetti che vende.
     */
    private final String needed = "soldi";

    /**
     * Crea un venditore a partire dal suo nome e dall'elenco dei nomi di ciò che vende.
     *
     * @param nome   nome del venditore
     * @param toSell elenco dei nomi di ciò che vende
     */
    public Venditore(String nome, String... toSell) {
        super(nome);
        Subject item;
        for (String name : toSell) {
            item = (Subject) Mondo.getInstance().getItem(name);
            this.toSell.add(item);
            item.registraObserver(this);
        }
    }

    /**
     * Restituisce il nome di ciò che vuole in cambio il venditore.
     *
     * @return ciò che vuole in cambio
     */
    public String getNeeded() {
        return needed;
    }

    @Override
    public void update() {
        System.out.println("\n" + getName() + ": " + (Command.getLanguage().equals(EnglishAndItalian.IT) ?
                "'Hey! Prima dovresti pagare...'" :
                "'Hey! You have to pay first..."));
    }

    @Override
    public void store(Storable toStore) {
        if (toStore.getName().equalsIgnoreCase(needed)) {
            for (Subject item : toSell) {
                item.rimuoviObserver(this);
                try {
                    Giocatore.getInstance().takeFromRoom(item.getName());
                } catch (ItemNotPresentException ignored) {
                    // se ha dato cosa vuole è per forza nella stessa stanza
                }
            }
        }
        super.store(toStore);
    }

    @Override
    public String parla(Language language) {
        if (language.equals(EnglishAndItalian.IT)) return "Benvenuto nella mia ferramenta!";
        else return "Welcome to my hardware store!";
    }

    @Override
    public String toString() {
        return "Venditore{" +
                "nome='" + nome + '\'' +
                ", inventario=" + inventario +
                ", toSell=" + toSell +
                ", needed='" + needed + '\'' +
                '}';
    }
}
