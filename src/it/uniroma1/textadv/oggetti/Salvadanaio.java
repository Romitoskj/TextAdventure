package it.uniroma1.textadv.oggetti;

public class Salvadanaio extends Container implements Breakable {

    public Salvadanaio(String nome, String contents) {
        super(nome, contents, true);
    }

    @Override
    public String toString() {
        return nome + (isOpen()? (isEmpty()? " rotto" : " rotto con vicino dei " + getContentName()) : "");
    }
}
