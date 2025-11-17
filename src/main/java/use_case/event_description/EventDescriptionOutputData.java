package use_case.event_description;

public class EventDescriptionOutputData {

    private final String id;
    private final String name;
    private final String description;
    private final String address;
    private final String category;
    private final String dateTime;
    // currently missing private final double distanceKm => need to calculate 

    public EventDescriptionOutputData(String id,
                                      String name,
                                      String description,
                                      String address,
                                      String category,
                                      String dateTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.category = category;
        this.dateTime = dateTime;
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public String getAddress() { return address; }

    public String getCategory() { return category; }

    public String getDateTime() { return dateTime; }
}
