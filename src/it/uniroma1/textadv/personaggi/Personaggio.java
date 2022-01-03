package it.uniroma1.textadv.personaggi;

import java.util.HashMap;
import java.util.Map;

import it.uniroma1.textadv.Item;
import it.uniroma1.textadv.Storable;
import it.uniroma1.textadv.exceptions.ItemNotPresentException;
import it.uniroma1.textadv.textengine.languages.Language;

public abstract class Personaggio implements Item {

	protected final String nome;
	
	protected final Map<String, Storable> inventario = new HashMap<>();
	
	public Personaggio(String nome) {
		this.nome = nome;
	}

	public Personaggio(String nome, Storable[] toStore) {
		this(nome);
		for (Storable s: toStore) {
			this.store(s);
		}
	}

	public String getNome() {
		return nome;
	}

	@Override
	public String getDescription(Language language) {
		return nome;
	}

	public void store(Storable toStore) {
		inventario.put(toStore.getNome(), toStore);
	}

	public Storable getInventoryItem(String name) throws ItemNotPresentException {
		Storable s = inventario.get(name);
		if (s == null)  throw new ItemNotPresentException();
		return s;
	}

	public Storable remove(String name) throws ItemNotPresentException {
		Storable s = inventario.remove(name);
		if (s == null)  throw new ItemNotPresentException();
		return s;
	}

	public void dai(String itemName, Personaggio p) throws ItemNotPresentException {
		Storable item = remove(itemName);
		p.store(item);
	}

	public String parla(Language language) {
		return nome;
	}
}