package interface_adapter.search_event_by_name;

import interface_adapter.ViewModel;
import interface_adapter.signup.SignupState;

public class SearchEventByNameViewModel extends ViewModel<SearchEventByNameState> {

    public SearchEventByNameViewModel() {
        super("search event");
        setState(new SearchEventByNameState());

    }

}
