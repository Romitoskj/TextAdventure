package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.interfaces.Storable;
import it.uniroma1.textadv.personaggi.interfaces.Animal;
import it.uniroma1.textadv.personaggi.interfaces.Personaggio;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

/**
 * Rappresenta un gatto.
 */
public class Gatto extends Personaggio implements Animal, Storable {

    /**
     * Crea un gatto a partire dal suo nome.
     *
     * @param nome nome del gatto
     */
    public Gatto(String nome) {
        super(nome);

    }

    @Override
    public String parla(Language language) {
        if (language.equals(EnglishAndItalian.IT)) return "MIAO!";
        else return "MEOW!";
    }
}
