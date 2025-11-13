package use_case.search_event_by_name;

import entity.User;
import entity.UserFactory;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupUserDataAccessInterface;

public class SearchEventByNameInteractor implements SearchEventByNameInputBoundary{

    private final SearchEventByNameOutputBoundary userPresenter;

    public SearchEventByNameInteractor(SearchEventByNameOutputBoundary signupOutputBoundary) {
        this.userPresenter = signupOutputBoundary;
    }

    @Override
    public void switchToDashboardView() {
        userPresenter.switchToDashboardView();

    }
}
