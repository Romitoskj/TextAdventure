package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.oggetti.interfaces.Container;
import it.uniroma1.textadv.oggetti.interfaces.Opener;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

/**
 * Rappresenta un camino che può nascondere un oggetto tra le fiamme.
 */
public class Camino extends Container {

    /**
     * Crea un camino a partire dal nome e dal nome di ciò che nasconde.
     *
     * @param nome     nome del camino
     * @param contents nome del contenuto che nasconde
     */
    public Camino(String nome, String contents) {
        super(nome, contents, true);
    }

    @Override
    public void unlock(Opener opener) {
        if (opener instanceof Secchio) {
            Secchio s = (Secchio) opener;
            if (s.isPieno()) {
                s.svuota();
                super.unlock(opener);
            }
        }
    }

    @Override
    public String getDescription(Language language) {
        if (language.equals(EnglishAndItalian.IT))
            return nome + (isOpen() ? " spento" + (isEmpty() ? " che non contiene più nulla" : " che contiene " + getContentName()) : " acceso" + (isEmpty() ? "" : " (sembra che fra le fiamme ci sia qualcosa)"));
        else
            return (isOpen() ? "Empty " + nome + (isEmpty() ? " that does not contain anything" : " that contains " + getContentName()) : "Lit " + nome + (isEmpty() ? "" : " (it seems that there is something into the flames)"));
    }
}
