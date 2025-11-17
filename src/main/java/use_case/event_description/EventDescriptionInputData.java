package use_case.event_description;

public class EventDescriptionInputData {
    private final String eventId;

    public EventDescriptionInputData(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }
}
