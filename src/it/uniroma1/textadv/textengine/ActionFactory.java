package it.uniroma1.textadv.textengine;

@FunctionalInterface
public interface ActionFactory {

    Action createAction(String name);
}
