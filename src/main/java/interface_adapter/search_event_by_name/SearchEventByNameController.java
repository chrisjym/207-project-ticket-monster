package interface_adapter.search_event_by_name;

import use_case.search_event_by_name.SearchEventByNameInputBoundary;
import use_case.signup.SignupInputBoundary;

public class SearchEventByNameController {

    private final SearchEventByNameInputBoundary userEventSearchUseCaseInteractor;

    public SearchEventByNameController(SearchEventByNameInputBoundary userEventSearchUseCaseInteractor) {
        this.userEventSearchUseCaseInteractor = userEventSearchUseCaseInteractor;
    }

    /**
     * Executes the "switch to LoginView" Use Case.
     */
    public void switchToDashboardView() {
        userEventSearchUseCaseInteractor.switchToDashboardView();
    }
}
