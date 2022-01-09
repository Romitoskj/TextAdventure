package it.uniroma1.textadv.interfaces;

/**
 * Rappresenta un oggetto che può osservare un {@link Subject}.
 */
public interface Observer {

    /**
     * Metodo invocato dai {@link Subject} osservati per aggiornare lo stato di questo observer.
     */
    void update();
}
