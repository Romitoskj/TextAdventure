package it.uniroma1.textadv;

import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.oggetti.Oggetto;
import it.uniroma1.textadv.personaggi.Personaggio;

import java.util.HashMap;
import java.util.Map;

/**
 * Rappresenta una stanza del mondo di gioco, ossia un qualsiasi spazio in cui può trovarsi il giocatore (sia aperto sia
 * chiuso). Una stanza può inoltre contenere oggetti e personaggi. Ogni stanza è collegata a una o più stanze.
 */
public class Stanza implements Named {

    /**
     * Permette la costruzione e la creazione di una {@link Stanza}.
     */
    public static class Builder {
        // required parameters
        private final String nome;
        private final String descrizione;

        // optional parameters
        private final Map<String, Oggetto> oggetti;
        private final Map<String, Personaggio> personaggi;
        private final Map<Direzione, Link> links;

        /**
         * Crea un Builder dati il nome della stanza e la sua descrizione.
         * @param nome nome della stanza.
         * @param descrizione descrizione della stanza.
         */
        public Builder(String nome, String descrizione) {
            this.nome = nome;
            this.descrizione = descrizione;
            oggetti = new HashMap<>();
            personaggi = new HashMap<>();
            links = new HashMap<>();
        }

        /**
         * Aggiunge un oggetto alla stanza.
         * @param oggetto {@link Oggetto} da aggiungere.
         * @return Builder della stanza.
         */
        public Builder addOggetto(Oggetto oggetto) {
            oggetti.put(oggetto.getNome(), oggetto);
            return this;
        }

        /**
         * Aggiunge un personaggio alla stanza.
         * @param personaggio {@link Personaggio} alla stanza.
         * @return Builder della stanza.
         */
        public Builder addPersonaggio(Personaggio personaggio) {
            personaggi.put(personaggio.getNome(), personaggio);
            return this;
        }

        /**
         * Aggiunge un link a una determinata direzione della stanza.
         * @param dir {@link Direzione} in cui aggiungere il link.
         * @param link {@link Link} da aggiungere.
         * @return Builder della stanza.
         */
        public Builder addLink(Direzione dir, Link link) {
            links.put(dir, link);
            return this;
        }

        /**
         * Crea la stanza.
         * @return stanza.
         */
        public Stanza build() {
            return new Stanza(this);
        }
    }

    /**
     * Nome della stanza.
     */
    private final String nome;

    /**
     * Descrizione della stanza.
     */
    private final String descrizione;

    /**
     * Dizionario degli oggetti presenti nella stanza indicizzati dal loro nome.
     */
    private final Map<String, Oggetto> oggetti;

    /**
     * Dizionario dei personaggi presenti nella stanza indicizzati dal loro nome.
     */
    private final Map<String, Personaggio> personaggi;

    /**
     * Dizionario dei link presenti nella stanza indicizzati dal loro nome.
     */
    private final Map<Direzione, Link> links;

    private Stanza(Builder builder) {
        nome = builder.nome;
        descrizione = builder.descrizione;
        oggetti = builder.oggetti;
        personaggi = builder.personaggi;
        links = builder.links;
    }

    @Override
    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public String toString() {
        return "Stanza{" +
                "nome='" + nome + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", oggetti=" + oggetti +
                ", personaggi=" + personaggi +
                ", links=" + links +
                '}';
    }

    public Link getLink(Direzione dir) {
        return links.get(dir);
    }

    public Link getLink(String nome) {
        for (Link l : links.values()) {
            if (l.getNome().equalsIgnoreCase(nome))
                return l;
        }
        return null;
    }

    public Oggetto getOggetto(String nome) {
        return oggetti.get(nome);
    }

    public Oggetto removeOggetto(String nome) {
        return oggetti.remove(nome);
    }

    public Personaggio getPersonaggio(String nome) {
        return personaggi.get(nome);
    }

    public Personaggio removePersonaggio(String nome) {
        return personaggi.remove(nome);
    }
}
