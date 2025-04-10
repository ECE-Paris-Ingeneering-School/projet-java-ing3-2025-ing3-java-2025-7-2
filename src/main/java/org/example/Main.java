import Controleur.controleur;
import Modele.Admin;
import Modele.modele;
import javax.swing.*;  import java.awt.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Demande la création de fenêtres décorés
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Créer et configurer un fenêtre d'application
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());

        // Ajouter un "label" au contenu du fenêtre
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);

        // Afficher le fenêtre
        frame.setSize(200,50);
        frame.setVisible(true);

    }
}

