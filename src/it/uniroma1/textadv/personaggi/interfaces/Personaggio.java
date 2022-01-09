package it.uniroma1.textadv.personaggi.interfaces;

import java.util.Map;
import java.util.TreeMap;

import it.uniroma1.textadv.interfaces.Item;
import it.uniroma1.textadv.interfaces.Storable;
import it.uniroma1.textadv.exceptions.ItemNotPresentException;
import it.uniroma1.textadv.textengine.languages.Language;

/**
 * Rappresenta un personaggio presente nel mondo di gioco, ogni personaggio ha un nome e un inventario dove può
 * conservare oggetti di tipo {@link Storable}.
 */
public abstract class Personaggio implements Item {

    /**
     * nome del personaggio.
     */
    protected final String nome;

    /**
     * inventario del personaggio.
     */
    protected final Map<String, Storable> inventario = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * crea un personaggio a partire dal suo nome.
     *
     * @param nome nome del personaggio
     */
    public Personaggio(String nome) {
        this.nome = nome;
    }

    /**
     * Crea un personaggio a partire dal suo nome e da un array di oggetti {@link Storable} da conservare.
     *
     * @param nome    nome del personaggio
     * @param toStore array di {@link Storable}
     */
    public Personaggio(String nome, Storable[] toStore) {
        this(nome);
        for (Storable s : toStore) {
            this.store(s);
        }
    }

    @Override
    public String getName() {
        return nome;
    }

    @Override
    public String getDescription(Language language) {
        return nome;
    }

    /**
     * Conserva un oggetto {@link Storable} nell'inventario del personaggio.
     *
     * @param toStore l'oggetto da aggiungere all'inventario
     */
    public void store(Storable toStore) {
        inventario.put(toStore.getName(), toStore);
    }

    /**
     * Restituisce un oggetto conservato nell'inventario del personaggio se presente, altrimenti solleva un eccezione.
     *
     * @param name nome dell'oggetto da prendere
     * @return l'oggetto cercato
     * @throws ItemNotPresentException se l'oggetto cercato non è presente
     */
    public Storable getInventoryItem(String name) throws ItemNotPresentException {
        Storable s = inventario.get(name);
        if (s == null) throw new ItemNotPresentException();
        return s;
    }

    /**
     * Rimuove un oggetto dall'inventario del personaggio se presente, altrimenti solleva un eccezione.
     *
     * @param name nome dell'oggetto
     * @return l'oggetto rimosso
     * @throws ItemNotPresentException se l'oggetto cercato non è presente
     */
    public Storable remove(String name) throws ItemNotPresentException {
        Storable s = inventario.remove(name);
        if (s == null) throw new ItemNotPresentException();
        return s;
    }

    /**
     * Da un oggetto a un altro personaggio, se presente nell'inventario altrimenti solleva un eccezione.
     *
     * @param itemName nome dell'oggetto da dare
     * @param p        {@link Personaggio} a cui dare l'oggetto
     * @throws ItemNotPresentException se l'oggetto da dare non è presente nell'inventario.
     */
    public void dai(String itemName, Personaggio p) throws ItemNotPresentException {
        Storable item = remove(itemName);
        p.store(item);
    }

    /**
     * Restituisce una stringa rappresentante ciò che il personaggio dice a seconda della lingua di gioco.
     *
     * @param language lingua di gioco
     * @return ciò che il personaggio dice
     */
    public String parla(Language language) {
        return nome;
    }
}