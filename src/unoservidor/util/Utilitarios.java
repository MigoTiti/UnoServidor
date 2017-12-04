package unoservidor.util;

import java.util.StringTokenizer;
import unoservidor.estruturas.Carta;

public class Utilitarios {
    
    public static Carta decodificarCarta(String cartaString) {
        StringTokenizer st = new StringTokenizer(cartaString, ",");
        return new Carta(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
    }
}
