package use_case.search;

public class SearchInputData {
    private final String query;
    private final String searchType;

    public SearchInputData(String query, String searchType) {
        this.query = query;
        this.searchType = searchType; // This is for implementation into search by location vs event
    }

    public String getQuery() { return query; }
    public String getSearchType() { return searchType; }
}
