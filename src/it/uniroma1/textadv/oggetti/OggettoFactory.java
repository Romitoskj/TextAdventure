package it.uniroma1.textadv.oggetti;

import java.lang.reflect.Constructor;

public class OggettoFactory {

	@SuppressWarnings("unchecked")
	public Oggetto creaOggetto(String type) {
		Class<? extends Oggetto> cl = null;
		Constructor<? extends Oggetto> constr = null;
		Oggetto res = null;
		
		try {
			cl = (Class<? extends Oggetto>) Class.forName("it.uniroma1.textadv.oggetti." + type);
			constr = cl.getConstructor();
			res = constr.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
