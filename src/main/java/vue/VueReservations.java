package vue;

import modele.Utilisateur;
import modele.dao.ConnexionBDD;
import modele.dao.ReservationDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class VueReservations extends JFrame {

    private Utilisateur utilisateur;

    public VueReservations(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        setTitle("Récapitulatif de vos Réservations");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        afficherReservations();
    }

    private void afficherReservations() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        try {
            ReservationDAO dao = new ReservationDAO(ConnexionBDD.getConnexion());
            Date today = Date.valueOf(LocalDate.now());
            List<String> reservations = dao.getReservationsDetailsParClientEtDate(utilisateur.getId(), today);

            JLabel titre = new JLabel("Vos réservations effectuées aujourd'hui (" + today + ") :");
            titre.setAlignmentX(Component.CENTER_ALIGNMENT);
            titre.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(titre);
            panel.add(Box.createVerticalStrut(10));

            if (reservations.isEmpty()) {
                JLabel emptyLabel = new JLabel("Aucune réservation trouvée pour aujourd’hui.");
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(emptyLabel);
            } else {
                for (String res : reservations) {
                    JPanel ligne = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel label = new JLabel(res);
                    JButton deleteBtn = new JButton("Supprimer");

                    deleteBtn.addActionListener(e -> {
                        try {
                            int idRes = Integer.parseInt(res.split("#")[1].split(" ")[0]);
                            dao.supprimerReservation(idRes);
                            JOptionPane.showMessageDialog(this, "Réservation supprimée !");
                            this.dispose();
                            new VueReservations(utilisateur).setVisible(true); // Refresh
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    ligne.add(label);
                    ligne.add(deleteBtn);
                    panel.add(ligne);
                }

                panel.add(Box.createVerticalStrut(10));
                JButton confirmer = new JButton("Confirmer & Générer Facture");
                confirmer.setAlignmentX(Component.CENTER_ALIGNMENT);
                confirmer.addActionListener(e -> {
                    try {
                        dao.creerFacture(utilisateur.getId());
                        JOptionPane.showMessageDialog(this, "Facture générée avec succès !");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Erreur lors de la génération de la facture.");
                    }
                });
                panel.add(confirmer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scroll = new JScrollPane(panel);
        add(scroll, BorderLayout.CENTER);
    }
}
