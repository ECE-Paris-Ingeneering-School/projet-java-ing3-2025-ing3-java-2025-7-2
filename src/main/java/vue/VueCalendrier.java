package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import controleur.Calendrier;
import modele.Utilisateur;
import modele.dao.AttractionDAO;
import modele.dao.ConnexionBDD;
import modele.dao.ReservationDAO;

public class VueCalendrier extends JFrame {
    private Calendrier controller;
    private YearMonth currentYearMonth;
    private JPanel calendarPanel;
    private JLabel monthLabel;
    private JPanel reservationPanel;
    private Utilisateur utilisateurConnecte;

    public VueCalendrier(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        this.controller = new Calendrier(this);
        initializeUI();
        controller.initializeCalendar();
    }

    private void initializeUI() {
        setTitle("Calendrier des Réservations - Connecté : " + utilisateurConnecte.getPrenom());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel monthPanel = new JPanel();
        monthLabel = new JLabel("", JLabel.CENTER);
        monthPanel.add(monthLabel);

        JButton prevButton = new JButton("<");
        prevButton.addActionListener(e -> navigateMonth(-1));

        JButton nextButton = new JButton(">");
        nextButton.addActionListener(e -> navigateMonth(1));

        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.add(prevButton, BorderLayout.WEST);
        navPanel.add(monthPanel, BorderLayout.CENTER);
        navPanel.add(nextButton, BorderLayout.EAST);

        calendarPanel = new JPanel(new GridLayout(0, 7));

        reservationPanel = new JPanel();
        reservationPanel.setBorder(BorderFactory.createTitledBorder("Réservation"));

        add(navPanel, BorderLayout.NORTH);
        add(new JScrollPane(calendarPanel), BorderLayout.CENTER);
        add(reservationPanel, BorderLayout.SOUTH);
    }

    public void displayMonth(YearMonth yearMonth) {
        currentYearMonth = yearMonth;
        monthLabel.setText(yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        calendarPanel.removeAll();

        String[] days = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        for (String day : days) {
            calendarPanel.add(new JLabel(day, JLabel.CENTER));
        }

        LocalDate firstOfMonth = yearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i < dayOfWeek; i++) {
            calendarPanel.add(new JPanel());
        }

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.addActionListener(e -> showDayAttractions(date));
            calendarPanel.add(dayButton);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private void navigateMonth(int months) {
        displayMonth(currentYearMonth.plusMonths(months));
    }

    private void showDayAttractions(LocalDate date) {
        reservationPanel.removeAll();
        List<String> attractions = controller.getAttractionsForDay(date);

        if (attractions.isEmpty()) {
            reservationPanel.add(new JLabel("Aucune attraction disponible"));
        } else {
            JComboBox<String> attractionCombo = new JComboBox<>(attractions.toArray(new String[0]));
            JButton reserveButton = new JButton("Réserver");

            reserveButton.addActionListener(e -> {
                if ("admin".equalsIgnoreCase(utilisateurConnecte.getRole())) {
                    JOptionPane.showMessageDialog(this,
                            "Les administrateurs ne sont pas autorisés à effectuer des réservations.",
                            "Accès refusé",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String selected = (String) attractionCombo.getSelectedItem();
                try {
                    AttractionDAO attractionDAO = new AttractionDAO(ConnexionBDD.getConnexion());
                    int idAttraction = attractionDAO.getAttractionIdByName(selected);
                    ReservationDAO dao = new ReservationDAO(ConnexionBDD.getConnexion());
                    dao.ajouterReservation(utilisateurConnecte.getId(), idAttraction, Date.valueOf(date));
                    JOptionPane.showMessageDialog(this, "Réservation ajoutée pour " + selected);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erreur lors de la réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });

            reservationPanel.add(new JLabel("Attractions:"));
            reservationPanel.add(attractionCombo);
            reservationPanel.add(reserveButton);
        }

        reservationPanel.revalidate();
        reservationPanel.repaint();
    }
}
