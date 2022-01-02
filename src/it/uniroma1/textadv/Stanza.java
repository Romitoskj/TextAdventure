package it.uniroma1.textadv;

import it.uniroma1.textadv.exceptions.ItemNotPresentException;
import it.uniroma1.textadv.links.Link;
import it.uniroma1.textadv.oggetti.Container;
import it.uniroma1.textadv.oggetti.Oggetto;
import it.uniroma1.textadv.personaggi.Personaggio;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Rappresenta una stanza del mondo di gioco, ossia un qualsiasi spazio in cui può trovarsi il giocatore (sia aperto sia
 * chiuso). Una stanza può inoltre contenere oggetti e personaggi. Ogni stanza è collegata a una o più stanze attraverso
 * dei {@link Link}.
 */
public class Stanza implements Item {

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
         *
         * @param nome        nome della stanza.
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
         *
         * @param oggetto {@link Oggetto} da aggiungere.
         * @return Builder della stanza.
         */
        public Builder addOggetto(Oggetto oggetto) {
            oggetti.put(oggetto.getNome(), oggetto);
            return this;
        }

        /**
         * Aggiunge un personaggio alla stanza.
         *
         * @param personaggio {@link Personaggio} alla stanza.
         * @return Builder della stanza.
         */
        public Builder addPersonaggio(Personaggio personaggio) {
            personaggi.put(personaggio.getNome(), personaggio);
            return this;
        }

        /**
         * Aggiunge un link a una determinata direzione della stanza.
         *
         * @param dir  {@link Direzione} in cui aggiungere il link.
         * @param link {@link Link} da aggiungere.
         * @return Builder della stanza.
         */
        public Builder addLink(Direzione dir, Link link) {
            links.put(dir, link);
            return this;
        }

        /**
         * Crea la stanza.
         *
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

    private String mapList(Map<String, ? extends Item> m) {
        StringBuilder sb = new StringBuilder();
        int k = 0;
        Set<String> keySet = m.keySet();
        for (String s : keySet) {
            sb.append(m.get(s).toString())
                    .append((k == keySet.size() - 2) ? " e " : ", ");
            k++;
        }
        return sb.delete(sb.length() - 2, sb.length()).append(".").toString();
    }

    private String linkList() {
        StringBuilder sb = new StringBuilder();
        int k = 0;
        Set<Direzione> l = links.keySet();
        for (Direzione d : l) {
            sb.append("a ")
                    .append(d.toString().toLowerCase())
                    .append(" ")
                    .append(links.get(d).getNome())
                    .append((k == l.size() - 2) ? " e " : ", ");
            k++;
        }
        return sb.delete(sb.length() - 2, sb.length()).append(".").toString();
    }

    @Override
    public String toString() {
        return nome + ": " + descrizione + "." +
                (oggetti.isEmpty() ? "" : "\nAll'interno ci sono degli oggetti: " + mapList(oggetti)) +
                (personaggi.isEmpty() ? "" : "\nC'è qualcuno qui: " + mapList(personaggi)) +
                "\nPer uscire da questa stanza puoi sfruttare i seguenti passaggi: " + linkList();
    }

    public Link getLink(Direzione dir) throws ItemNotPresentException {
        Link l = links.get(dir);
        if (l == null) throw new ItemNotPresentException();
        return l;
    }

    public Link getLink(String nome) throws ItemNotPresentException {
        for (Link l : links.values()) {
            if (l.getNome().equalsIgnoreCase(nome))
                return l;
        }
        throw new ItemNotPresentException();
    }

    public void add(Storable s) {
        if (s instanceof Oggetto) oggetti.put(s.getNome(), (Oggetto) s);
        if (s instanceof Personaggio) personaggi.put(s.getNome(), (Personaggio) s);
    }

    public Item get(String nome) throws ItemNotPresentException {
        Item item = oggetti.get(nome.toLowerCase());
        if (item == null) {
            for (Oggetto o : oggetti.values()) {
                if (o instanceof Container) {
                    try {
                        item = ((Container) o).removeContent(nome);
                    } catch (ItemNotPresentException ignored) {
                    }
                }
            }
        }
        if (item == null) item = personaggi.get(nome.toLowerCase());
        if (item == null) item = getLink(nome);
        if (item == null) throw new ItemNotPresentException();
        return item;
    }

    public void remove(String nome) {
        oggetti.remove(nome);
        personaggi.remove(nome);
    }
}
