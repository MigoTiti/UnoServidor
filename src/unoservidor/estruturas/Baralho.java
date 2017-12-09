package unoservidor.estruturas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baralho {

    private Carta cartaNaMesa;
    private List<Carta> cartasJogadas;
    private List<Carta> cartas;

    public static final int MODO_NORMAL = 1;
    public static final int MODO_MAIS_DOIS = 2;
    public static final int MODO_BLOQUEIO = 3;
    public static final int MODO_REVERSO = 4;
    public static final int MODO_MAIS_QUATRO = 5;
    public static final int MODO_CORINGA = 6;
    
    public Baralho() {
        this.cartas = new ArrayList<>();

        int modo = MODO_NORMAL;

        switch (modo) {
            case 1:
                inicializacaoNormal();
                break;
            case 2:
                inicializarMaisDois();
                break;
            case 3:
                inicializarBloqueio();
                break;
            case 4:
                inicializarReverso();
                break;
            case 5:
                inicializarMaisQuatro();
                break;
            case 6:
                inicializarCoringa();
                break;
        }

        this.cartasJogadas = new ArrayList<>();
    }

    private void inicializarBloqueio() {
        for (int i = 0; i < (108 / 4); i++) {
            this.cartas.add(new Carta(Carta.COR_VERMELHA, Carta.BLOQUEAR));
            this.cartas.add(new Carta(Carta.COR_AMARELA, Carta.BLOQUEAR));
            this.cartas.add(new Carta(Carta.COR_AZUL, Carta.BLOQUEAR));
            this.cartas.add(new Carta(Carta.COR_VERDE, Carta.BLOQUEAR));
        }
    }
    
    private void inicializarReverso() {
        for (int i = 0; i < (108 / 4); i++) {
            this.cartas.add(new Carta(Carta.COR_VERMELHA, Carta.REVERTER));
            this.cartas.add(new Carta(Carta.COR_AMARELA, Carta.REVERTER));
            this.cartas.add(new Carta(Carta.COR_AZUL, Carta.REVERTER));
            this.cartas.add(new Carta(Carta.COR_VERDE, Carta.REVERTER));
        }
    }
    
    private void inicializarCoringa() {
        for (int i = 0; i < 108; i++) {
            this.cartas.add(new Carta(Carta.COR_PRETA, Carta.CORINGA));
        }
    }
    
    private void inicializarMaisQuatro() {
        this.cartas.add(new Carta(Carta.COR_PRETA, Carta.MAIS_QUATRO));
    }
    
    private void inicializarMaisDois() {
        for (int i = 0; i < (108 / 4); i++) {
            this.cartas.add(new Carta(Carta.COR_VERMELHA, Carta.MAIS_DOIS));
            this.cartas.add(new Carta(Carta.COR_AMARELA, Carta.MAIS_DOIS));
            this.cartas.add(new Carta(Carta.COR_AZUL, Carta.MAIS_DOIS));
            this.cartas.add(new Carta(Carta.COR_VERDE, Carta.MAIS_DOIS));
        }
    }

    private void inicializacaoNormal() {
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
    }

    public void jogarCarta(Carta c) {
        cartasJogadas.add(cartaNaMesa);
        cartaNaMesa = c;
    }

    public Carta getCartaNoTopo() {
        if (cartas.isEmpty()) {
            cartas = new ArrayList<>(cartasJogadas);
            Collections.shuffle(cartas);
        }

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
