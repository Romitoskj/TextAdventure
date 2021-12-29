package it.uniroma1.textadv.personaggi;

import java.util.HashMap;
import java.util.Map;

import it.uniroma1.textadv.Named;
import it.uniroma1.textadv.Storable;
import it.uniroma1.textadv.oggetti.Oggetto;

public abstract class Personaggio implements Named {

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

	public void store(Storable toStore) {
		inventario.put(toStore.getNome(), toStore);
	}

	public Storable getItem(String name) {
		return inventario.get(name);
	}

	@Override
	public String toString() {
		return "{" +
				"nome='" + nome + '\'' +
				", inventario=" + inventario +
				'}';
	}

	public String parla() {
		return nome;
	}
}
