package unoservidor.estruturas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baralho {

    private Carta cartaNaMesa;
    private List<Carta> cartasJogadas;
    private List<Carta> cartas;

    public Baralho() {
        this.cartas = new ArrayList<>();
        
        for (int i = 0; i <= 12; i++) {
            if (i == 0) {
                this.cartas.add(new Carta(Carta.COR_VERMELHA, 0));
                this.cartas.add(new Carta(Carta.COR_AMARELA, 0));
                this.cartas.add(new Carta(Carta.COR_AZUL, 0));
                this.cartas.add(new Carta(Carta.COR_VERDE, 0));
            } else {
                this.cartas.add(new Carta(Carta.COR_VERMELHA, i));
                this.cartas.add(new Carta(Carta.COR_VERMELHA, i));
                this.cartas.add(new Carta(Carta.COR_AMARELA, i));
                this.cartas.add(new Carta(Carta.COR_AMARELA, i));
                this.cartas.add(new Carta(Carta.COR_AZUL, i));
                this.cartas.add(new Carta(Carta.COR_AZUL, i));
                this.cartas.add(new Carta(Carta.COR_VERDE, i));
                this.cartas.add(new Carta(Carta.COR_VERDE, i));
            }
        }

        this.cartas.add(new Carta(Carta.COR_PRETA, Carta.CORINGA));
        this.cartas.add(new Carta(Carta.COR_PRETA, Carta.CORINGA));
        this.cartas.add(new Carta(Carta.COR_PRETA, Carta.CORINGA));
        this.cartas.add(new Carta(Carta.COR_PRETA, Carta.CORINGA));
        this.cartas.add(new Carta(Carta.COR_PRETA, Carta.MAIS_QUATRO));
        this.cartas.add(new Carta(Carta.COR_PRETA, Carta.MAIS_QUATRO));
        this.cartas.add(new Carta(Carta.COR_PRETA, Carta.MAIS_QUATRO));
        this.cartas.add(new Carta(Carta.COR_PRETA, Carta.MAIS_QUATRO));

        Collections.shuffle(cartas);
        
        this.cartasJogadas = new ArrayList<>();
    }

    public void jogarCarta(Carta c) {
        cartasJogadas.add(cartaNaMesa);
        cartaNaMesa = c;
    }

    public Carta getCartaNoTopo() {
        Carta c = cartas.remove(cartas.size() - 1);
        return c;
    }

    public Carta getCartaNaMesa() {
        return cartaNaMesa;
    }

    public void setCartaNaMesa(Carta cartaNaMesa) {
        this.cartaNaMesa = cartaNaMesa;
    }

    public List<Carta> getCartasJogadas() {
        return cartasJogadas;
    }

    public void setCartasJogadas(List<Carta> cartasJogadas) {
        this.cartasJogadas = cartasJogadas;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }
}
