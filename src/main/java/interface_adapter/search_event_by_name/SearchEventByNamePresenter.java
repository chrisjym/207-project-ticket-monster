package interface_adapter.search_event_by_name;

import interface_adapter.ViewManagerModel;
// Change to dashboardViewModel
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;
import use_case.search_event_by_name.SearchEventByNameOutputBoundary;

public class SearchEventByNamePresenter implements SearchEventByNameOutputBoundary {
    private final SignupViewModel signupViewModel;
    private final ViewManagerModel viewManagerModel;

    public SearchEventByNamePresenter(ViewManagerModel viewManagerModel, SignupViewModel signupViewModel) {
        this.viewManagerModel = viewManagerModel;
        // Change to dashboardViewModel
        this.signupViewModel = signupViewModel;
    }

    @Override
    public void switchToDashboardView() {
        viewManagerModel.setState(signupViewModel.getViewName());
        // Need to change this to the dashboardViewModel when finished
        viewManagerModel.firePropertyChange();
    }
}
