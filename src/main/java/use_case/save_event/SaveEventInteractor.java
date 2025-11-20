package use_case.save_event;

import entity.Event;

import java.util.ArrayList;
import java.util.List;

public class SaveEventInteractor implements SaveEventInputBoundary {

    private SaveEventOutputBoundary eventPresenter;
    private final List<Event> savedEvents = new ArrayList<>();

    public SaveEventInteractor(SaveEventOutputBoundary outputBoundary) {
        this.eventPresenter = outputBoundary;
    }

    @Override
    public void execute(SaveEventInputData inputData) {
        if (inputData.getEvent() == null) {
            eventPresenter.prepareFailureView("No Event Found");
        } else {
            Event event = inputData.getEvent();
            if (savedEvents.contains(event)) {
                eventPresenter.prepareFailureView("Event already saved");
                return;
            }
            savedEvents.add(event);
            SaveEventOutputData outputData = new SaveEventOutputData(event);
            eventPresenter.prepareSuccessView(outputData);
        }
    }

    public List<Event> getSavedEvents() {
        return new ArrayList<>(savedEvents); // Return a copy to maintain encapsulation
    }
}
