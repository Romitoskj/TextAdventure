package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

public class Camino extends Container {

    public Camino(String nome, String contents) {
        super(nome, contents, true);
    }

    @Override
    public void unlock(Opener opener) { // TODO dire che va riempito se vuoto (ora "non si apre con quest'oggetto")
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
            return nome + (isOpen() ? " spento" : " acceso") + (isEmpty() ? " che non contiene più nulla" : " che contiene " + getContentName());
        else
            return (isOpen() ? "Empty " : "Lit ") + nome + (isEmpty() ? " that does not contain anything" : " that contains " + getContentName());
    } // TODO deve dire che non può prenderlo se è chiuso non che non lo trova
}
