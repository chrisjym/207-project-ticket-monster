package use_case.calendarFlow.viewDemoAndTest;

import data_access.CalendarFlowDataAccessObject;
import entity.Event;
import entity.Location;
import interface_adapter.calendarFlow.CalendarFlowState;
import interface_adapter.calendarFlow.CalendarFlowViewModel;
import view.EventListByDateView;

import javax.swing.*;
        import java.time.LocalDate;
import java.util.List;

/**
 * Simple manual test/demo:
 * - makes up a date, location, radius
 * - calls CalendarFlowDataAccessObject.getEventsByDate(...)
 * - shows the result in EventListByDateView
 */
public class CalendarFlowDataAccessObjectTest {

    public static void main(String[] args) {
        // 1. Make up test inputs (change these to whatever you want)
        LocalDate selectedDate = LocalDate.of(2025, 11, 30);
        Location userLocation = new Location(
                "Downtown Toronto",
                43.6532,   // latitude
                -79.3832   // longitude
        );
        double radiusKm = 10.0;

        // 2. Call the real data access object
        CalendarFlowDataAccessObject dao = new CalendarFlowDataAccessObject();
        List<Event> events = dao.getEventsByDate(selectedDate, userLocation, radiusKm);

        // Just to see something in console as well
        System.out.println("Selected date: " + selectedDate);
        System.out.println("Location: " + userLocation);
        System.out.println("Radius (km): " + radiusKm);
        System.out.println("Number of events returned: " + events.size());

        // 3. Show the result in EventListByDateView
        SwingUtilities.invokeLater(() -> {
            // ViewModel and View
            CalendarFlowViewModel viewModel = new CalendarFlowViewModel();
            EventListByDateView view = new EventListByDateView(viewModel);

            // Build state from the DAO result
            CalendarFlowState state = new CalendarFlowState();
            state.setDate(selectedDate);
            state.setEventList(events);

            // Push state into the ViewModel (this will trigger propertyChange on the view)
            viewModel.setState(state);
            viewModel.firePropertyChange();

            // 4. Wrap the view in a frame and show it
            JFrame frame = new JFrame("Events for " + selectedDate);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
