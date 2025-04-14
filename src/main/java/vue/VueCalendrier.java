package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import controleur.Calendrier;
import controleur.Reservation;

public class VueCalendrier extends JFrame {
    private Calendrier controller;
    private YearMonth currentYearMonth;
    private JPanel calendarPanel;
    private JLabel monthLabel;
    private JPanel reservationPanel;

    public VueCalendrier() {
        this.controller = new Calendrier(this);
        initializeUI();
        controller.initializeCalendar();
    }

    private void initializeUI() {
        setTitle("Calendrier des Réservations");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel du mois
        JPanel monthPanel = new JPanel();
        monthLabel = new JLabel("", JLabel.CENTER);
        monthPanel.add(monthLabel);

        // Boutons navigation
        JButton prevButton = new JButton("<");
        prevButton.addActionListener(e -> navigateMonth(-1));

        JButton nextButton = new JButton(">");
        nextButton.addActionListener(e -> navigateMonth(1));

        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.add(prevButton, BorderLayout.WEST);
        navPanel.add(monthPanel, BorderLayout.CENTER);
        navPanel.add(nextButton, BorderLayout.EAST);

        // Panel du calendrier
        calendarPanel = new JPanel(new GridLayout(0, 7));

        // Panel de réservation
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

        // En-têtes des jours
        String[] days = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        for (String day : days) {
            calendarPanel.add(new JLabel(day, JLabel.CENTER));
        }

        LocalDate firstOfMonth = yearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1 (Lundi) à 7 (Dimanche)

        // Jours vides au début
        for (int i = 1; i < dayOfWeek; i++) {
            calendarPanel.add(new JPanel());
        }

        // Jours du mois
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
                String selected = (String) attractionCombo.getSelectedItem();
                new Reservation().addReservation(selected, date, 1); // 1 = client simulé
                JOptionPane.showMessageDialog(this, "Réservation ajoutée pour " + selected);
            });

            reservationPanel.add(new JLabel("Attractions:"));
            reservationPanel.add(attractionCombo);
            reservationPanel.add(reserveButton);
        }

        reservationPanel.revalidate();
        reservationPanel.repaint();
    }
}