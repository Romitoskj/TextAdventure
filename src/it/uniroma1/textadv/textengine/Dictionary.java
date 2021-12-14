package it.uniroma1.textadv.textengine;

import java.util.List;
import java.util.Map;

public interface Dictionary {

    Map<String, String> VERBS = Map.ofEntries(
            Map.entry("guarda", ""),
            Map.entry("vai",""),
            Map.entry("entra",""),
            Map.entry("prendi",""),
            Map.entry("apri",""),
            Map.entry("accarezza",""),
            Map.entry("rompi",""),
            Map.entry("usa",""),
            Map.entry("dai",""),
            Map.entry("parla",""),
            Map.entry("inventario",""),
            Map.entry("help","")
    );

    List<String> STOP_WORDS = List.of("la", "da", "su", "a");

    static String getVerbAction(String key) {
        return VERBS.get(key);
    }
}
