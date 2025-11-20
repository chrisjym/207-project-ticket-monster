package interface_adapter.save_event;

import use_case.save_event.SaveEventOutputBoundary;
import use_case.save_event.SaveEventOutputData;

public class SaveEventPresenter implements SaveEventOutputBoundary {

    private final SaveEventViewModel saveEventViewModel;

    public SaveEventPresenter(SaveEventViewModel saveEventViewModel) {
        this.saveEventViewModel = saveEventViewModel;
    }

    @Override
    public void prepareSuccessView(SaveEventOutputData outputData) {
        SaveEventState currentState = saveEventViewModel.getState();
        currentState.setEvent(outputData.getEvent());
        saveEventViewModel.setState(currentState);
        saveEventViewModel.firePropertyChange("event");
    }

    @Override
    public void prepareFailureView(String errorMessage) {
        SaveEventState currentState = saveEventViewModel.getState();
        currentState.setEvent(null);
        saveEventViewModel.setState(currentState);
        saveEventViewModel.firePropertyChange("error");
    }
}
