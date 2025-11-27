package use_case.calendarFlow;

/**
 * InputBoundary for the Calendar Flow Use Case
 * Defines the interface for executing the calendar flow use case
 */
public interface CalendarFlowInputBoundary {

    void execute(CalendarFlowInputData inputData);

    /**
     * Switch to the Dashboard View.
     */
    void switchToDashboardView();
}
