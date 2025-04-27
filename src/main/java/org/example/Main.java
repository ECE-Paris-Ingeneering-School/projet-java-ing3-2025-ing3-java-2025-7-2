import controleur.controleur;
import modele.Admin;
import modele.modele;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Classe principale Main pour lancer une simple fenêtre Swing affichant "Hello World".
 */
public class Main {

    /**
     * Point d'entrée de l'application.
     *
     * @param args Arguments de la ligne de commande (non utilisés ici)
     */
    public static void main(String[] args) {
       
        JFrame.setDefaultLookAndFeelDecorated(true);

        
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());

        
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);

       
        frame.setSize(200, 50);
        frame.setVisible(true);
    }
}
