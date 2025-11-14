package view;

import javax.swing.*;

import interface_adapter.search_event_by_name.SearchEventByNameController;
import interface_adapter.search_event_by_name.SearchEventByNameViewModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class SearchEventByNameView extends JPanel {

    private final String viewName = "event search";
    private final SearchEventByNameViewModel searchEventByNameViewModel;
    private SearchEventByNameController eventController = null;


    /**
     * Constructor for the EventView, takes an event and returns a view of its details
     * @param searchEventByNameViewModel
     */
    public SearchEventByNameView(SearchEventByNameViewModel searchEventByNameViewModel) {
        this.searchEventByNameViewModel = searchEventByNameViewModel;
        this.setLayout(new BorderLayout());

//        if (searchEventByNameViewModel.getEvent() == null) {
//            JPanel emptyPanel = new JPanel();
//            emptyPanel.setForeground(Color.WHITE);
//
//            JLabel emptyLabel = new JLabel("No Event Found");
//            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//            emptyLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
//            emptyPanel.add(emptyLabel);
//            add(emptyPanel);
//        }
//        else {
        JPanel detailsPanel = createDetailsPanel(searchEventByNameViewModel);
        add(detailsPanel, BorderLayout.CENTER);

        JPanel imagePanel = createImagePanel(searchEventByNameViewModel);
        add(imagePanel, BorderLayout.EAST);
//        }
    }

    /**
     * Create the left-panel of the event, this contains all the details of the event.
     * @param searchEventByNameViewModel
     * @return
     */
    private JPanel createDetailsPanel(SearchEventByNameViewModel searchEventByNameViewModel) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setForeground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setPreferredSize(new Dimension(400, 700));

        // Back
        JButton backButton = new JButton("← Back to Events");
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(backButton);
        mainPanel.add(Box.createVerticalStrut(24));


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventController.switchToDashboardView();

            }
        });

        // Category
        // Replace with: searchEventByNameViewModel.getEvent().getCategory().getDisplayName()
        JLabel categoryLabel = new JLabel("Sport");
        categoryLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        categoryLabel.setForeground(new Color(59, 130, 246));
        categoryLabel.setOpaque(true);
        categoryLabel.setBackground(new Color(219, 234, 254));
        categoryLabel.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(categoryLabel);
        mainPanel.add(Box.createVerticalStrut(12));


        // Name
        // Replace with: searchEventByNameViewModel.getEvent().getName()
        JLabel nameLabel = new JLabel("<html>Toronto Raptors <br/> vs. Lakers</html>");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(nameLabel);
        mainPanel.add(Box.createVerticalStrut(12));


        // Date
        // Replace with: searchEventByNameViewModel.getEvent().getDate()
        mainPanel.add(createRow("Date:", "November 18, 2025" ));
        mainPanel.add(Box.createVerticalStrut(12));

        // Venue
        // Replace with: searchEventByNameViewModel.getEvent().getLocation()
        mainPanel.add(createRow("Venue:", "Diddy's House" ));
        mainPanel.add(Box.createVerticalStrut(12));

        JLabel descTitle = new JLabel("About this event");
        descTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        descTitle.setForeground(new Color(20, 20, 20));
        descTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(descTitle);
        mainPanel.add(Box.createVerticalStrut(12));

        //Replace with: searchEventByNameViewModel.getEvent().getDescription()
        JTextArea descArea = new JTextArea("Test Description");
        descArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descArea.setForeground(new Color(80, 80, 80));
        descArea.setBackground(Color.WHITE);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBorder(null);
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(descArea);
        mainPanel.add(Box.createVerticalStrut(30));

        JButton saveButton = new JButton("Save Event");
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveButton.setFocusPainted(false);
        saveButton.setBackground(Color.WHITE);
        saveButton.setForeground(new Color(59, 130, 246));
        saveButton.setBorder(BorderFactory.createLineBorder(new Color(59, 130, 246), 2));
        saveButton.setPreferredSize(new Dimension(160, 45));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT); // Change to LEFT
        mainPanel.add(saveButton);


        // Im not sure what this does, and will have to do more research (just copied it from another source)
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane);

        return wrapper;
    }

    /**
     * Create the right-panel of the event, containing an image of the event.
     * @param searchEventByNameViewModel
     * @return
     */
    private JPanel createImagePanel(SearchEventByNameViewModel searchEventByNameViewModel) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(500, 700));
        mainPanel.setBackground(new Color(30, 30, 30));
        // Get image from TicketMaster API (Event Entity)

        try {
            // Replace with: searchEventByNameViewModel.getEvent().getImageURL()
            URL imageUrl = new URL("https://images.unsplash.com/photo-1504450758481-7338eba7524a?w=500&h=700&fit=crop");
            ImageIcon icon = new ImageIcon(imageUrl);
            Image image = icon.getImage().getScaledInstance(500, 700, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(image));
            mainPanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            // Fallback if image doesn't load
            JLabel placeholderLabel = new JLabel("No Image :(");
            mainPanel.add(placeholderLabel, BorderLayout.CENTER);
        }
        return mainPanel;
    }

    private JPanel createRow(String category, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(400, 30));

        JLabel title = new JLabel(category);
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setForeground(new Color(50, 50, 50));

        JLabel description = new JLabel(text);
        description.setFont(new Font("SansSerif", Font.PLAIN, 14));
        description.setForeground(new Color(50, 50, 50));

        row.add(title);
        row.add(description);

        return row;
    }

    public String getViewName() {
        return viewName;
    }

    public void setEventController(SearchEventByNameController controller) {
        this.eventController = controller;
    }

}
