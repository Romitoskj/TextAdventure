package it.uniroma1.textadv.interfaces;

/**
 * Rappresenta un oggetto che viene osservato da degli {@link Observer}.
 */
public interface Subject extends Item {

    /**
     * Registra un nuovo observer.
     *
     * @param o il nuovo observer
     */
    void registraObserver(Observer o);

    /**
     * Rimuove un observer dal subject.
     *
     * @param o observer da rimuovere
     */
    void rimuoviObserver(Observer o);

    /**
     * Notifica tutti gli observer che osservano il subject.
     */
    void notifyObservers();

    /**
     * Indica se il subject è osservato da degli observer.
     *
     * @return {@code true} se il subject ha degli observer, {@code false} altrimenti
     */
    boolean hasObservers();
}
