package it.uniroma1.textadv.textengine;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface Action extends Function<List<String>, String> {

}
