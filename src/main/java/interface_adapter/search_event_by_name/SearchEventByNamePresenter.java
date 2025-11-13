package interface_adapter.search_event_by_name;

import interface_adapter.ViewManagerModel;
import view.ViewManager;
import use_case.search_event_by_name.SearchEventByNameOutputBoundary;

public class SearchEventByNamePresenter implements SearchEventByNameOutputBoundary {

    private final ViewManagerModel viewManagerModel;

    public SearchEventByNamePresenter(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void switchToDashboardView() {
        viewManagerModel.setState(dashboardViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
