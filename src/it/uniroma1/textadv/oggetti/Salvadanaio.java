package it.uniroma1.textadv.oggetti;

import it.uniroma1.textadv.textengine.languages.EnglishAndItalian;
import it.uniroma1.textadv.textengine.languages.Language;

public class Salvadanaio extends Container implements Breakable {

    public Salvadanaio(String nome, String contents) {
        super(nome, contents, true);
    }

    @Override
    public String getDescription(Language language) {
        if (language.equals(EnglishAndItalian.IT)) return getName() + (isOpen()? (isEmpty()? " rotto" : " rotto con vicino dei " + getContentName()) : "");
        else  return (isOpen()? (isEmpty()? " broken" + getName() : " broken" + getName() + " con vicino dei " + getContentName()) : getName());
    }
}
