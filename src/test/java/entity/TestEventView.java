package entity;

import interface_adapter.search_event_by_name.SearchEventByNameViewModel;
import view.SearchEventByNameView;

import javax.swing.*;

public class TestEventView {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Event View Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the view with an empty view model
        SearchEventByNameViewModel viewModel = new SearchEventByNameViewModel();
        SearchEventByNameView eventView = new SearchEventByNameView(viewModel);

        frame.add(eventView);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}