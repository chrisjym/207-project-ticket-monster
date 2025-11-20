package interface_adapter.save_event;

import entity.Event;
import use_case.save_event.SaveEventInputBoundary;
import use_case.save_event.SaveEventInputData;

public class SaveEventController {
    private final SaveEventInputBoundary saveEventUseCase;

    public SaveEventController(SaveEventInputBoundary saveEventUseCase) {
        this.saveEventUseCase = saveEventUseCase;
    }

    public void saveEvent(Event event) {
        SaveEventInputData inputData = new SaveEventInputData(event);
        saveEventUseCase.execute(inputData);
    }
}
