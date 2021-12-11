package it.uniroma1.textadv.oggetti;

public abstract class Opener extends Oggetto {

    private String toOpen;

    public Opener(String nome, String toOpen) {
        super(nome);
        this.toOpen = toOpen;
    }

    public String getToOpen() {
        return toOpen;
    }
}
