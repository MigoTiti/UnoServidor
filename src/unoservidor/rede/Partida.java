package unoservidor.rede;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import unoservidor.estruturas.Baralho;
import unoservidor.estruturas.Carta;
import unoservidor.util.Utilitarios;

public class Partida {

    public static int idIncremental = 0;

    private final Map<Integer, Comunicador> jogadores;
    private Baralho baralho;

    private int nJogadores;
    private int jogadoresConectados;
    private int id;
    private String nome;
    private int jogadorDaVez = 1;

    public Partida(Comunicador primeiroJogador, int nJogadores, String nome) {
        this.nJogadores = nJogadores;
        this.id = idIncremental++;
        this.nome = nome;
        this.jogadores = new HashMap<>();
        this.jogadores.put(++this.jogadoresConectados, primeiroJogador);
        this.baralho = null;
    }

    public void iniciarPartida() {
        multicast(Integer.toString(Comunicador.TODOS_JOGADORES_CONECTADOS));

        distribuirCartas();

        boolean sentidoHorario = true;
        boolean correnteCompra = false;
        int contagemCorrente = 0;

        while (true) {
            String jogada = jogadores.get(jogadorDaVez).receberMensagem();

            StringTokenizer st = new StringTokenizer(jogada, "&");

            int comando = Integer.parseInt(st.nextToken());

            StringBuilder resposta;

            switch (comando) {
                case Comunicador.JOGAR_CARTA:
                    Carta cartaJogada = Utilitarios.decodificarCarta(st.nextToken());

                    this.baralho.jogarCarta(cartaJogada);

                    resposta = new StringBuilder(Integer.toString(Comunicador.REPORTAR_JOGADA));

                    int numero = cartaJogada.getNumero();

                    switch (numero) {
                        case Carta.MAIS_DOIS:
                            contagemCorrente += 2;
                            correnteCompra = true;
                            incrementarVezDoJogador(1, sentidoHorario);
                            resposta.append("&").append(cartaJogada.toString())
                                    .append("&").append(sentidoHorario ? "1" : "0").append("&")
                                    .append(jogadorDaVez).append("&1&").append(contagemCorrente);
                            break;
                        case Carta.MAIS_QUATRO:
                            contagemCorrente += 4;
                            correnteCompra = true;
                            incrementarVezDoJogador(1, sentidoHorario);
                            resposta.append("&").append(cartaJogada.toString())
                                    .append("&").append(sentidoHorario ? "1" : "0").append("&")
                                    .append(jogadorDaVez).append("&1&").append(contagemCorrente);
                            break;
                        case Carta.BLOQUEAR:
                            incrementarVezDoJogador(2, sentidoHorario);
                            resposta.append("&").append(cartaJogada.toString())
                                    .append("&").append(sentidoHorario ? "1" : "0").append("&")
                                    .append(jogadorDaVez).append("&0");
                            break;
                        case Carta.REVERTER:
                            sentidoHorario = !sentidoHorario;
                            incrementarVezDoJogador(1, sentidoHorario);
                            resposta.append("&").append(cartaJogada.toString())
                                    .append("&").append(sentidoHorario ? "1" : "0").append("&")
                                    .append(jogadorDaVez).append("&0");
                            break;
                        default:
                            incrementarVezDoJogador(1, sentidoHorario);
                            resposta.append("&").append(cartaJogada.toString())
                                    .append("&").append(sentidoHorario ? "1" : "0").append("&")
                                    .append(jogadorDaVez).append("&0");
                            break;
                    }

                    multicast(resposta.toString());
                    break;
                case Comunicador.COMPRAR_CARTA:
                    resposta = new StringBuilder(Integer.toString(Comunicador.RESPOSTA_COMPRA));

                    if (correnteCompra) {
                        resposta.append("&").append(contagemCorrente);

                        for (int i = 0; i < contagemCorrente; i++) {
                            resposta.append("&").append(this.baralho.getCartaNoTopo().toString());
                        }

                        correnteCompra = false;
                        contagemCorrente = 0;
                    } else {
                        resposta.append("&1&").append(this.baralho.getCartaNoTopo().toString());
                    }

                    multicast(resposta.toString());
                    break;
                case Comunicador.PULAR_JOGADA:
                    resposta = new StringBuilder(Integer.toString(Comunicador.PULAR_JOGADA));
                    incrementarVezDoJogador(1, sentidoHorario);
                    resposta.append("&").append(jogadorDaVez);

                    multicast(resposta.toString());
                    break;
                case Comunicador.JOGADOR_EM_UNO:
                    multicast(Comunicador.JOGADOR_EM_UNO + "&" + jogadorDaVez);
                    break;
                case Comunicador.JOGADOR_SAIU_UNO:
                    multicast(Comunicador.JOGADOR_SAIU_UNO + "&" + jogadorDaVez);
                    break;
                case Comunicador.JOGADOR_VENCEU:
                    multicast(Comunicador.JOGADOR_VENCEU + "&" + jogadorDaVez);
                    break;
            }
        }
    }

    private void incrementarVezDoJogador(int valor, boolean sentidoHorario) {
        if (sentidoHorario) {
            for (int i = 1; i <= valor; i++) {
                if (jogadorDaVez == nJogadores) {
                    jogadorDaVez = 1;
                } else {
                    jogadorDaVez++;
                }
            }
        } else {
            for (int i = 1; i <= valor; i++) {
                if (jogadorDaVez == 1) {
                    jogadorDaVez = nJogadores;
                } else {
                    jogadorDaVez--;
                }
            }
        }
    }

    private void multicast(String mensagem) {
        jogadores.entrySet().stream().forEach((jogador) -> {
            jogador.getValue().enviarMensagemParaJogador(mensagem);
        });
    }

    private void distribuirCartas() {
        baralho = new Baralho();
        baralho.jogarCarta(baralho.getCartaNoTopo());

        Carta primeiraCarta = baralho.getCartaNaMesa();

        for (int i = 1; i <= jogadoresConectados; i++) {
            StringBuilder mensagem = new StringBuilder(Integer.toString(Comunicador.DISTRIBUIR_CARTAS));
            mensagem.append("&").append(nJogadores)
                    .append("&").append(i)
                    .append("&").append(primeiraCarta.toString());

            for (int j = 0; j < 7; j++) {
                Carta c = baralho.getCartaNoTopo();
                mensagem.append("&").append(c.toString());
            }

            String mensagemPronta = mensagem.toString();
            jogadores.get(i).enviarMensagemParaJogador(mensagemPronta);
        }
    }

    public void adicionarJogador(Comunicador jogador) {
        this.jogadores.put(++this.jogadoresConectados, jogador);
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
