package unoservidor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.StringTokenizer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.util.Pair;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import unoservidor.rede.Comunicador;
import unoservidor.rede.Partida;

public class UnoServidor extends JApplet {

    private static final int JFXPANEL_WIDTH_INT = 500;
    private static final int JFXPANEL_HEIGHT_INT = 500;
    private static JFXPanel fxContainer;

    private static TextArea logger;

    public static final int PORTA = 12345;
    public static final HashMap<Integer, Partida> PARTIDAS = new HashMap<>();
    private static DatagramSocket socket;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            }

            JFrame frame = new JFrame("Uno - Servidor");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JApplet applet = new UnoServidor();
            applet.init();

            frame.setContentPane(applet.getContentPane());

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            applet.start();
        });
    }

    @Override
    public void init() {
        fxContainer = new JFXPanel();
        fxContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        add(fxContainer, BorderLayout.CENTER);
        Platform.runLater(() -> {
            createScene();
            appendMensagem("Servidor sendo inicializado...");
        });
        new Thread(() -> iniciarServidor()).start();
    }

    private static void createScene() {
        logger = new TextArea();
        logger.setEditable(false);

        StackPane root = new StackPane();
        root.setPadding(new Insets(30));
        root.getChildren().add(logger);
        fxContainer.setScene(new Scene(root));
    }

    private static void appendMensagem(String mensagem) {
        Platform.runLater(() -> {
            logger.appendText(mensagem + "\n");
        });
    }

    private static void iniciarServidor() {
        try {
            socket = new DatagramSocket(PORTA);

            appendMensagem("Servidor iniciado");

            while (true) {
                byte[] mensagemAReceber = new byte[500];
                DatagramPacket pacoteAReceber = new DatagramPacket(mensagemAReceber, mensagemAReceber.length);
                socket.receive(pacoteAReceber);
                String mensagemRecebida = new String(pacoteAReceber.getData());

                StringTokenizer st = new StringTokenizer(mensagemRecebida, "&");
                int comando = Integer.parseInt(st.nextToken());

                if (comando == Comunicador.SOLICITAR_CONEXAO) {
                    new Thread(() -> {
                        try {
                            handleConexao(pacoteAReceber);
                        } catch (SocketException ex) {
                            exibirException(ex);
                        }
                    }).start();
                }
            }
        } catch (SocketException ex) {
            exibirException(ex);
        } catch (IOException ex) {
            exibirException(ex);
        }
    }

    private static void handleConexao(DatagramPacket pacote) throws SocketException {
        Comunicador comunicador = new Comunicador(new DatagramSocket());

        appendMensagem("Usuario conectado, IP: " + pacote.getAddress().getHostAddress() + ", porta: " + pacote.getPort());

        InetAddress ip = pacote.getAddress();
        int porta = pacote.getPort();

        comunicador.enviarMensagemParaJogador(Integer.toString(Comunicador.CONFIRMAR_CONEXAO), ip, porta);

        while (true) {
            String mensagem = comunicador.receberMensagem();
            StringTokenizer st = new StringTokenizer(mensagem, "&");

            int comando = Integer.parseInt(st.nextToken());

            switch (comando) {
                case Comunicador.CRIAR_PARTIDA:
                    String nome = st.nextToken();
                    int nJogadores = Integer.parseInt(st.nextToken());

                    Partida p = new Partida(new Pair<>(ip, porta), comunicador, nJogadores, nome);

                    PARTIDAS.put(p.getId(), p);
                    appendMensagem("Partida '" + nome + "' criada, capacidade: " + nJogadores + " jogadores");

                    comunicador.enviarMensagemParaJogador(Integer.toString(Comunicador.CONFIRMAR_CRIACAO_PARTIDA), ip, porta);
                    return;
                case Comunicador.LISTAR_PARTIDAS:
                    StringBuilder listagem = new StringBuilder(Comunicador.LISTAR_PARTIDAS + "&");

                    PARTIDAS.entrySet().stream().map((entry) -> entry.getValue()).forEach((partida) -> {
                        listagem.append(partida.getId()).append("&").
                                append(partida.getNome()).append("&").
                                append(partida.getnJogadores()).append("&").
                                append(partida.getJogadoresConectados()).append("&");
                    });

                    listagem.deleteCharAt(listagem.length() - 1);

                    comunicador.enviarMensagemParaJogador(listagem.toString(), ip, porta);
                    break;
                case Comunicador.ENTRAR_EM_PARTIDA:
                    int id = Integer.parseInt(st.nextToken());

                    Partida partidaAEntrar = PARTIDAS.get(id);

                    if (partidaAEntrar.getJogadoresConectados() != partidaAEntrar.getnJogadores()) {
                        partidaAEntrar.adicionarJogador(ip, porta);

                        comunicador.enviarMensagemParaJogador(Integer.toString(Comunicador.CONFIRMAR_ENTRADA_EM_PARTIDA), ip, porta);
                        
                        if (partidaAEntrar.getJogadoresConectados() == partidaAEntrar.getnJogadores())
                            new Thread(() -> partidaAEntrar.iniciarPartida()).start();
                        
                        return;
                    } else {
                        comunicador.enviarMensagemParaJogador(Integer.toString(Comunicador.PARTIDA_CHEIA), ip, porta);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public static void exibirException(Exception ex) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Exception");
            alert.setContentText(ex.getMessage());

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
        });
    }
}
