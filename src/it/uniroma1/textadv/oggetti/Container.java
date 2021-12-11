package it.uniroma1.textadv.oggetti;

public abstract class Container extends Oggetto {

    private String contents;

    public Container(String nome, String contents) {
        super(nome);
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "{" +
                "nome='" + nome + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
