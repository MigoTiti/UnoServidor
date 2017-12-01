package unoservidor.rede;

import java.net.DatagramSocket;
import unoservidor.estruturas.Baralho;

public class Partida {

    public static int idIncremental = 0;

    private DatagramSocket[] jogadores;
    private Baralho baralho;

    private int nJogadores;
    private int jogadoresConectados;
    private int id;
    private String nome;
    private int jogadorDaVez = 1;

    public Partida(DatagramSocket primeiroJogador, int nJogadores, String nome) {
        this.nJogadores = nJogadores;
        this.id = idIncremental++;
        this.nome = nome;
        this.jogadores = new DatagramSocket[nJogadores];
        this.jogadores[jogadoresConectados++] = primeiroJogador;
        this.baralho = new Baralho();
    }

    private void iniciarPartida() {

    }

    private void incrementarVezDoJogador(int valor, boolean sentidoHorario) {
        if (sentidoHorario) {
            for (int i = 1; i <= valor; i++) {
                if (jogadorDaVez == nJogadores)
                    jogadorDaVez = 1; 
                else
                    jogadorDaVez++;
            }
        } else {
            for (int i = 1; i <= valor; i++) {
                if (jogadorDaVez == 1)
                    jogadorDaVez = nJogadores;
                else
                    jogadorDaVez--;
            }
        }
    }

    private void multicast(String mensagem) {

    }

    private void distribuirCartas() {

    }

    public void adicionarJogador(DatagramSocket jogador) {
        this.jogadores[jogadoresConectados++] = jogador;
    }

    public DatagramSocket[] getJogadores() {
        return jogadores;
    }

    public void setJogadores(DatagramSocket[] jogadores) {
        this.jogadores = jogadores;
    }

    public int getnJogadores() {
        return nJogadores;
    }

    public void setnJogadores(int nJogadores) {
        this.nJogadores = nJogadores;
    }

    public int getJogadoresConectados() {
        return jogadoresConectados;
    }

    public void setJogadoresConectados(int jogadoresConectados) {
        this.jogadoresConectados = jogadoresConectados;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getJogadorDaVez() {
        return jogadorDaVez;
    }

    public void setJogadorDaVez(int jogadorDaVez) {
        this.jogadorDaVez = jogadorDaVez;
    }

    public Baralho getBaralho() {
        return baralho;
    }

    public void setBaralho(Baralho baralho) {
        this.baralho = baralho;
    }
}
