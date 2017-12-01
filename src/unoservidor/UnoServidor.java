package unoservidor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UnoServidor extends JApplet {
    
    private static final int JFXPANEL_WIDTH_INT = 500;
    private static final int JFXPANEL_HEIGHT_INT = 500;
    private static JFXPanel fxContainer;
    
    private static TextArea logger;

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
        iniciarServidor();
        Platform.runLater(() -> {
            createScene();
        });
    }
    
    private void createScene() {
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
        
    }
}
