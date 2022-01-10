package it.uniroma1.textadv.personaggi;

import it.uniroma1.textadv.personaggi.interfaces.Animal;
import it.uniroma1.textadv.personaggi.interfaces.Personaggio;
import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

/**
 * Rappresenta un cane.
 */
public class Cane extends Personaggio implements Animal {

    /**
     * Crea un cane a partire dal suo nome.
     *
     * @param nome nome del cane
     */
    public Cane(String nome) {
        super(nome);

    }

    @Override
    public String parla(Language language) {
        if (language.equals(EnglishAndItalian.IT)) return "BAU!";
        else return "WOOF!";
    }

    @Override
    public String getDescription(Language language) {
        if (language.equals(EnglishAndItalian.IT)) return "un cane chiamato " + getName();
        else return "a dog called " + getName();
    }

}
