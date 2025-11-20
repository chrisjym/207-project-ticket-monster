package view;

import entity.Event;
import interface_adapter.save_event.SaveEventViewModel;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class SaveEventsView extends JPanel implements PropertyChangeListener {
    private String viewName = "save events";
    private SaveEventViewModel saveEventsViewModel = null;
    private List<Event> savedEvents = new ArrayList<>();
    private JPanel eventsContainer;

    public SaveEventsView(SaveEventViewModel saveEventsViewModel) {
        this.saveEventsViewModel = saveEventsViewModel;
        saveEventsViewModel.addPropertyChangeListener(this);

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(249, 250, 251));

        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);

        eventsContainer = new JPanel();
        eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));
        eventsContainer.setBackground(new Color(249, 250, 251));
        eventsContainer.setBorder(new EmptyBorder(20, 40, 20, 40));

        JScrollPane scrollPane = new JScrollPane(eventsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(249, 250, 251));
        header.setBorder(new EmptyBorder(20, 40, 10, 40));

        JLabel title = new JLabel("My Saved Events");
        title.setFont(new Font("SegoeUI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setOpaque(false);

        header.add(title);
        return header;

    }

    private void refreshEventsList() {
        eventsContainer.removeAll();

        if (savedEvents.isEmpty()) {
            JPanel emptyPanel = new JPanel(new FlowLayout());
            emptyPanel.setBackground(new Color(249, 250, 251));

            JLabel emptyLabel = new JLabel("No saved events yet. Start exploring and save events you're interested in!");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            emptyLabel.setForeground(new Color(107, 114, 128));

            emptyPanel.add(emptyLabel);
            eventsContainer.add(emptyPanel);
        } else {
            for (Event event : savedEvents) {
                JPanel eventCard = createEventCard(event);
                eventsContainer.add(eventCard);
                eventsContainer.add(Box.createVerticalStrut(15));
            }
        }

        eventsContainer.revalidate();
        eventsContainer.repaint();
    }

    private JPanel createEventCard(Event event) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        return card;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("event".equals(evt.getPropertyName())) {
            refreshEventsList();
        } else if ("error".equals(evt.getPropertyName())) {
            System.out.println("Event was null, floundering...");
        }
    }

}
