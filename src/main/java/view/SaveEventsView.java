package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import entity.Event;
import entity.EventCategory;
import interface_adapter.save_event.SaveEventController;
import interface_adapter.save_event.SaveEventViewModel;
import use_case.save_event.SaveEventInteractor;

/**
 * View for displaying saved events.
 */
public class SaveEventsView extends JPanel implements PropertyChangeListener {

    // Used to Generative AI to make all magic numbers into constants with compliance to checkStyle
    private static final int DEFAULT_FONT_SIZE_SMALL = 11;
    private static final int DEFAULT_FONT_SIZE_MEDIUM = 12;
    private static final int DEFAULT_FONT_SIZE_NORMAL = 13;
    private static final int DEFAULT_FONT_SIZE_LARGE = 14;
    private static final int DEFAULT_FONT_SIZE_XLARGE = 16;
    private static final int DEFAULT_FONT_SIZE_XXLARGE = 18;
    private static final int DEFAULT_FONT_SIZE_HEADER = 24;
    private static final int DEFAULT_FONT_SIZE_ICON = 36;

    private static final int MARGIN_SMALL = 4;
    private static final int MARGIN_MEDIUM = 5;
    private static final int MARGIN_NORMAL = 6;
    private static final int MARGIN_LARGE = 8;
    private static final int MARGIN_XLARGE = 10;
    private static final int MARGIN_XXLARGE = 12;
    private static final int MARGIN_HEADER = 13;
    private static final int MARGIN_CONTAINER = 15;
    private static final int MARGIN_PANEL = 20;
    private static final int MARGIN_ACTION = 35;
    private static final int MARGIN_LABEL = 40;

    private static final int WIDTH_SMALL = 100;
    private static final int HEIGHT_SMALL = 100;
    private static final int HEIGHT_CARD = 140;
    private static final int BORDER_RADIUS = 12;
    private static final int ICON_OFFSET = 4;

    private static final Color BACKGROUND_COLOR = new Color(249, 250, 251);
    private static final Color HEADER_COLOR = new Color(13, 133, 251);
    private static final Color HEADER_BORDER_COLOR = new Color(10, 103, 198);
    private static final Color CARD_BORDER_COLOR = new Color(229, 231, 235);
    private static final Color TEXT_PRIMARY_COLOR = new Color(17, 24, 39);
    private static final Color TEXT_SECONDARY_COLOR = new Color(107, 114, 128);
    private static final Color REMOVE_BUTTON_COLOR = new Color(239, 68, 68);
    private static final Color MUSIC_COLOR = new Color(147, 51, 234);
    private static final Color SPORTS_COLOR = new Color(59, 130, 246);
    private static final Color ARTS_THEATRE_COLOR = new Color(236, 72, 153);
    private static final Color MISCELLANEOUS_COLOR = new Color(107, 114, 128);

    private static final String FONT_NAME = "Segoe UI";
    private static final String EMPTY_MESSAGE =
            "No saved events yet. Start exploring and save events you're interested in!";
    private static final String REMOVE_CONFIRM_MESSAGE = "Remove \"%s\" from saved events?";
    private static final String REMOVE_CONFIRM_TITLE = "Confirm Removal";
    private static final String BACK_BUTTON_TEXT = "Back to Events";
    private static final String TITLE_TEXT = "My Saved Events";
    private static final String DATE_PREFIX = "Date: ";
    private static final String VENUE_PREFIX = "Venue: ";
    private static final String REMOVE_BUTTON_TEXT = "Remove";
    private static final String ICON_TEXT = "Event";

    private final String viewName = "save events";
    private SaveEventViewModel saveEventsViewModel;
    private final List<Event> savedEvents = new ArrayList<>();
    private JPanel eventsContainer;
    private SaveEventController saveEventController;
    private SaveEventInteractor saveEventInteractor;

    /**
     * Constructor for SaveEventsView.
     *
     * @param saveEventsViewModelParam the view model for save events
     */
    public SaveEventsView(final SaveEventViewModel saveEventsViewModelParam) {
        this.saveEventsViewModel = saveEventsViewModelParam;
        saveEventsViewModelParam.addPropertyChangeListener(this);

        initializeUi();
    }

    private void initializeUi() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        final JPanel header = createHeader();
        add(header, BorderLayout.NORTH);

        eventsContainer = new JPanel();
        eventsContainer.setLayout(new BoxLayout(eventsContainer, BoxLayout.Y_AXIS));
        eventsContainer.setBackground(BACKGROUND_COLOR);
        eventsContainer.setBorder(new EmptyBorder(MARGIN_PANEL, MARGIN_LABEL,
                MARGIN_PANEL, MARGIN_LABEL));

        final JScrollPane scrollPane = new JScrollPane(eventsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(DEFAULT_FONT_SIZE_XLARGE);
        add(scrollPane);
    }

    private JPanel createHeader() {
        final JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, HEADER_BORDER_COLOR),
                new EmptyBorder(MARGIN_PANEL, MARGIN_LABEL, MARGIN_PANEL, MARGIN_LABEL)
        ));

        final JLabel titleLabel = new JLabel(TITLE_TEXT);
        titleLabel.setFont(new Font(FONT_NAME, Font.BOLD, DEFAULT_FONT_SIZE_HEADER));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);

        final JButton backButton = new JButton(BACK_BUTTON_TEXT);
        backButton.setFont(new Font(FONT_NAME, Font.PLAIN, MARGIN_HEADER));
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent actionEvent) {
                saveEventController.switchToDashboardView();
            }
        });

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);
        return headerPanel;
    }

    /**
     * Refreshes the events list display.
     */
    public void refreshEventsList() {
        eventsContainer.removeAll();

        final List<Event> currentSavedEvents = saveEventInteractor.getSavedEvents();

        if (currentSavedEvents.isEmpty()) {
            showEmptyState();
        }
        else {
            showEventsList(currentSavedEvents);
        }

        eventsContainer.revalidate();
        eventsContainer.repaint();
    }

    private void showEmptyState() {
        final JPanel emptyPanel = new JPanel(new FlowLayout());
        emptyPanel.setBackground(BACKGROUND_COLOR);

        final JLabel emptyLabel = new JLabel(EMPTY_MESSAGE);
        emptyLabel.setFont(new Font(FONT_NAME, Font.PLAIN, DEFAULT_FONT_SIZE_XLARGE));
        emptyLabel.setForeground(TEXT_SECONDARY_COLOR);

        emptyPanel.add(emptyLabel);
        eventsContainer.add(emptyPanel);
    }

    private void showEventsList(final List<Event> events) {
        for (Event event : events) {
            final JPanel eventCard = createEventCard(event);
            eventsContainer.add(eventCard);
            eventsContainer.add(Box.createVerticalStrut(MARGIN_CONTAINER));
        }
    }

    private JPanel createEventCard(final Event event) {
        final JPanel card = new JPanel(new BorderLayout(MARGIN_PANEL, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(CARD_BORDER_COLOR, 1, true),
                new EmptyBorder(MARGIN_PANEL, MARGIN_PANEL, MARGIN_PANEL, MARGIN_PANEL)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT_CARD));

        final JPanel imagePanel = createImagePanel(event);
        final JPanel detailsPanel = createDetailsPanel(event);
        final JPanel actionsPanel = createActionsPanel(event);

        card.add(imagePanel, BorderLayout.WEST);
        card.add(detailsPanel, BorderLayout.CENTER);
        card.add(actionsPanel, BorderLayout.EAST);

        return card;
    }

    private JPanel createImagePanel(final Event event) {
        final JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics graphics) {
                super.paintComponent(graphics);
                paintIconBackground(graphics, event);
            }
        };
        panel.setPreferredSize(new Dimension(WIDTH_SMALL, HEIGHT_SMALL));
        panel.setOpaque(false);
        return panel;
    }

    private void paintIconBackground(final Graphics graphics, final Event event) {
        final Graphics2D graphics2d = (Graphics2D) graphics;
        graphics2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final Color categoryColor = getCategoryColor(event.getCategory());
        final GradientPaint gradient = new GradientPaint(
                0, 0, categoryColor.darker(),
                getWidth(), getHeight(), categoryColor
        );
        graphics2d.setPaint(gradient);
        graphics2d.fillRoundRect(0, 0, getWidth(), getHeight(),
                BORDER_RADIUS, BORDER_RADIUS);

        graphics2d.setColor(Color.WHITE);
        graphics2d.setFont(new Font(FONT_NAME, Font.BOLD, DEFAULT_FONT_SIZE_ICON));
        final FontMetrics fontMetrics = graphics2d.getFontMetrics();
        final int xPosition = (getWidth() - fontMetrics.stringWidth(ICON_TEXT)) / 2;
        final int yPosition = (getHeight() + fontMetrics.getAscent()) / 2 - ICON_OFFSET;
        graphics2d.drawString(ICON_TEXT, xPosition, yPosition);
    }

    private JPanel createDetailsPanel(final Event event) {
        final JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);

        final Color categoryColor = getCategoryColor(event.getCategory());
        final JLabel categoryLabel = createCategoryLabel(event, categoryColor);
        final JLabel nameLabel = createNameLabel(event);
        final JLabel dateLabel = createDateLabel(event);
        final JLabel locationLabel = createLocationLabel(event);

        detailsPanel.add(categoryLabel);
        detailsPanel.add(Box.createVerticalStrut(MARGIN_LARGE));
        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createVerticalStrut(MARGIN_NORMAL));
        detailsPanel.add(dateLabel);
        detailsPanel.add(Box.createVerticalStrut(MARGIN_SMALL));
        detailsPanel.add(locationLabel);

        return detailsPanel;
    }

    private JLabel createCategoryLabel(final Event event, final Color categoryColor) {
        final JLabel label = new JLabel(event.getCategory().toString());
        label.setFont(new Font(FONT_NAME, Font.BOLD, DEFAULT_FONT_SIZE_SMALL));
        label.setForeground(categoryColor);
        label.setOpaque(true);
        label.setBackground(new Color(categoryColor.getRed(),
                categoryColor.getGreen(), categoryColor.getBlue(), MARGIN_LABEL));
        label.setBorder(new EmptyBorder(MARGIN_SMALL, MARGIN_XLARGE,
                MARGIN_SMALL, MARGIN_XLARGE));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createNameLabel(final Event event) {
        final JLabel label = new JLabel(event.getName());
        label.setFont(new Font(FONT_NAME, Font.BOLD, DEFAULT_FONT_SIZE_XXLARGE));
        label.setForeground(TEXT_PRIMARY_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createDateLabel(final Event event) {
        final JLabel label = new JLabel(DATE_PREFIX + event.getStartTime().toString());
        label.setFont(new Font(FONT_NAME, Font.PLAIN, DEFAULT_FONT_SIZE_LARGE));
        label.setForeground(TEXT_SECONDARY_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createLocationLabel(final Event event) {
        final JLabel label = new JLabel(VENUE_PREFIX + event.getLocation().getAddress());
        label.setFont(new Font(FONT_NAME, Font.PLAIN, DEFAULT_FONT_SIZE_LARGE));
        label.setForeground(TEXT_SECONDARY_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createActionsPanel(final Event event) {
        final JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setOpaque(false);

        final JButton removeButton = createActionButton(REMOVE_BUTTON_TEXT, REMOVE_BUTTON_COLOR);
        removeButton.setBorder(new CompoundBorder(
                new LineBorder(CARD_BORDER_COLOR, 1, true),
                new EmptyBorder(MARGIN_MEDIUM, MARGIN_PANEL, MARGIN_MEDIUM, MARGIN_PANEL)
        ));

        setupRemoveButtonAction(removeButton, event);

        actionsPanel.add(Box.createVerticalStrut(MARGIN_ACTION));
        actionsPanel.add(removeButton);

        return actionsPanel;
    }

    private void setupRemoveButtonAction(final JButton removeButton, final Event event) {
        removeButton.addActionListener(actionEvent -> handleRemoveEvent(event));
    }

    private void handleRemoveEvent(final Event event) {
        final int result = JOptionPane.showConfirmDialog(this,
                String.format(REMOVE_CONFIRM_MESSAGE, event.getName()),
                REMOVE_CONFIRM_TITLE,
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            saveEventInteractor.removeEvent(event);
            refreshEventsList();
        }
    }

    private JButton createActionButton(final String text, final Color color) {
        final JButton button = new JButton(text);
        button.setFont(new Font(FONT_NAME, Font.BOLD, DEFAULT_FONT_SIZE_MEDIUM));
        button.setForeground(color);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(MARGIN_LARGE,
                DEFAULT_FONT_SIZE_XLARGE, MARGIN_LARGE, DEFAULT_FONT_SIZE_XLARGE));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        setupButtonHoverEffects(button, color);

        return button;
    }

    private void setupButtonHoverEffects(final JButton button, final Color color) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent mouseEvent) {
                button.setBackground(color.darker());
            }

            @Override
            public void mouseExited(final MouseEvent mouseEvent) {
                button.setBackground(Color.WHITE);
            }
        });
    }

    private Color getCategoryColor(final EventCategory category) {
        final Color selectedColor;

        if (category == EventCategory.MUSIC) {
            selectedColor = MUSIC_COLOR;
        }
        else if (category == EventCategory.SPORTS) {
            selectedColor = SPORTS_COLOR;
        }
        else if (category == EventCategory.ARTS_THEATRE) {
            selectedColor = ARTS_THEATRE_COLOR;
        }
        else {
            selectedColor = MISCELLANEOUS_COLOR;
        }

        return selectedColor;
    }

    /**
     * Adds a saved event to the view.
     *
     * @param eventToAdd the event to add
     */
    public void addSavedEvent(final Event eventToAdd) {
        if (!savedEvents.contains(eventToAdd)) {
            savedEvents.add(eventToAdd);
            refreshEventsList();
        }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent propertyChangeEvent) {
        if ("event".equals(propertyChangeEvent.getPropertyName())) {
            refreshEventsList();
        }
        else if ("error".equals(propertyChangeEvent.getPropertyName())) {
            System.out.println("Event was null, floundering...");
        }
    }

    /**
     * Sets the save event interactor.
     *
     * @param newInteractor the interactor to set
     */
    public void setSaveEventInteractor(final SaveEventInteractor newInteractor) {
        this.saveEventInteractor = newInteractor;
    }

    /**
     * Sets the save event controller.
     *
     * @param newController the controller to set
     */
    public void setSaveEventController(final SaveEventController newController) {
        this.saveEventController = newController;
    }

    /**
     * Gets the view name.
     *
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }
}
