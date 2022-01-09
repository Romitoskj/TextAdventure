package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.interfaces.Storable;
import it.uniroma1.textadv.oggetti.interfaces.Opener;
import it.uniroma1.textadv.interfaces.Subject;
import it.uniroma1.textadv.interfaces.Observer;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un secchio che può essere riempito d'acqua.
 */
public class Secchio extends Opener implements Storable, Subject {

    /**
     * Indica se è pieno.
     */
    private boolean pieno = false;

    /**
     * Lista degli observers.
     */
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Crea un secchio a partire dal suo nome.
     *
     * @param nome nome del secchio
     */
    public Secchio(String nome) {
        super(nome, "camino");
    }

    /**
     * Indica se il secchio è pieno o meno.
     *
     * @return {@code true} se è pieno, {@code false} altrimenti
     */
    public boolean isPieno() {
        return pieno;
    }

    /**
     * Svuota il secchio. Se è già vuoto lo rimane.
     */
    public void svuota() {
        pieno = false;
    }

    @Override
    public void registraObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void rimuoviObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    @Override
    public boolean hasObservers() {
        return observers.size() > 0;
    }

    public void riempi(Pozzo p) {
        pieno = true;
    }

    @Override
    public String getDescription(Language language) {
        if (language.equals(EnglishAndItalian.IT)) return nome + (isPieno() ? " pieno" : " vuoto");
        return nome + (isPieno() ? " full" : " empty");
    }
}
