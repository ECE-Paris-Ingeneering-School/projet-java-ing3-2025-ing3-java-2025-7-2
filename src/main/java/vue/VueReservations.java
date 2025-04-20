package vue;

import modele.Utilisateur;
import modele.dao.ConnexionBDD;
import modele.dao.ReservationDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

//vueResa FX

public class VueReservations extends JFrame {

    private Utilisateur utilisateur;
    private JPanel panel;

    public VueReservations(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        setTitle("Mes Réservations et Facture");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        afficherReservations();

        JScrollPane scroll = new JScrollPane(panel);
        add(scroll, BorderLayout.CENTER);
    }

    private void afficherReservations() {
        panel.removeAll(); // Nettoyer
        Date today = Date.valueOf(LocalDate.now());

        try {
            ReservationDAO dao = new ReservationDAO(ConnexionBDD.getConnexion());
            List<String> reservations = dao.getReservationsDetailsParClientEtDate(utilisateur.getId(), today);

            JLabel titre = new JLabel("Réservations non facturées du " + today + " :");
            titre.setAlignmentX(Component.CENTER_ALIGNMENT);
            titre.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(titre);

            if (reservations.isEmpty()) {
                panel.add(new JLabel("Aucune réservation non facturée aujourd'hui."));
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
                            afficherReservations(); // refresh
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    ligne.add(label);
                    ligne.add(deleteBtn);
                    panel.add(ligne);
                }

                JButton confirmer = new JButton("Confirmer & Générer Facture");
                confirmer.setAlignmentX(Component.CENTER_ALIGNMENT);
                confirmer.addActionListener(e -> {
                    try {
                        int idFacture = dao.creerFacture(utilisateur.getId());
                        JOptionPane.showMessageDialog(this, "Facture générée !");
                        afficherFacture(idFacture);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                panel.add(Box.createVerticalStrut(10));
                panel.add(confirmer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        panel.revalidate();
        panel.repaint();
    }

    private void afficherFacture(int idFacture) {
        panel.removeAll();
        try {
            ReservationDAO dao = new ReservationDAO(ConnexionBDD.getConnexion());
            Map<String, Object> factureInfo = dao.getFactureDetailsAvecReservations(idFacture);

            JLabel titre = new JLabel("🧾 Facture #" + idFacture);
            titre.setFont(new Font("Arial", Font.BOLD, 18));
            titre.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(titre);

            panel.add(Box.createVerticalStrut(10));
            panel.add(new JLabel("Date de facture : " + factureInfo.get("date")));
            panel.add(new JLabel("Nombre de réservations : " + factureInfo.get("nb")));
            panel.add(new JLabel("Prix total : " + factureInfo.get("total") + " €"));
            panel.add(Box.createVerticalStrut(10));

            panel.add(new JLabel("Détail des réservations :"));

            @SuppressWarnings("unchecked")
            List<String> lignes = (List<String>) factureInfo.get("reservations");

            for (String ligne : lignes) {
                panel.add(new JLabel("• " + ligne));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            panel.add(new JLabel("Erreur lors de l'affichage de la facture."));
        }

        panel.revalidate();
        panel.repaint();
    }
}
