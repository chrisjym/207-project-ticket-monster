package entity;

public class Event {
    private final String id;
    private final String name;
    private final String description;
    private final String address;
    private final Location location;
    private final String category;
    private final String dateTime;

    public Event(String id, String name, String description,
                 String address, Location location,
                 String category, String dateTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.location = location;
        this.category = category;
        this.dateTime = dateTime;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getId() { return id; }
    public String getAddress() { return address; }
    public Location getLocation() { return location; }
    public String getCategory() { return category; }
    public String getDateTime() { return dateTime; }
}
