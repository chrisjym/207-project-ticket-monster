package use_case.displaylocalevents;
import use_case.displaylocalevents.DisplayLocalEventsOutputData;

public interface DisplayLocalEventsOutputBoundary {
    void presentSuccess(DisplayLocalEventsOutputData outputData);

    void presentError(String errorMessage);
}
