package unoservidor.rede;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.application.Platform;
import unoservidor.UnoServidor;

public class Comunicador {

    public static final int SOLICITAR_CONEXAO = 1;
    public static final int CONFIRMAR_CONEXAO = 2;
    public static final int CRIAR_PARTIDA = 3;
    public static final int CONFIRMAR_CRIACAO_PARTIDA = 4;
    public static final int ENTRAR_EM_PARTIDA = 5;
    public static final int LISTAR_PARTIDAS = 6;
    public static final int TODOS_JOGADORES_CONECTADOS = 7;
    public static final int CONFIRMAR_ENTRADA_EM_PARTIDA = 8;
    public static final int PARTIDA_CHEIA = 9;
    public static final int DISTRIBUIR_CARTAS = 10;
    public static final int JOGAR_CARTA = 11;
    public static final int COMPRAR_CARTA = 12;
    public static final int PULAR_JOGADA = 13;
    public static final int RESPOSTA_COMPRA = 14;
    public static final int REPORTAR_JOGADA = 15;

    private Socket socket;
    private BufferedReader inputStream;
    private PrintWriter writer;

    public Comunicador(Socket socket) {
        try {
            this.socket = socket;
            this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            UnoServidor.exibirException(ex);
        }
    }

    public void enviarMensagemParaJogador(String mensagemString) {
        try {
            writer.println(mensagemString + "&");
        } catch (Exception ex) {
            Platform.runLater(() -> {
                UnoServidor.exibirException(ex);
            });
        }
    }

    public String receberMensagem() {
        try {
            return inputStream.readLine();
        } catch (IOException ex) {
            Platform.runLater(() -> {
                UnoServidor.exibirException(ex);
            });
        }

        return null;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
