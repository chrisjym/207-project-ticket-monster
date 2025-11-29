package use_case.display_local_events;

import data_access.TicketmasterEventRepositoryAdapter;
import entity.Event;
import entity.EventCategory;
import entity.EventRepository;
import entity.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DisplayLocalEventsInteractorTest {

    private DisplayLocalEventsInteractor interactor;
    private TestEventRepository testEventRepository;
    private TestOutputBoundary testOutputBoundary;

    @BeforeEach
    void setUp() {
        testEventRepository = new TestEventRepository();
        testOutputBoundary = new TestOutputBoundary();
        interactor = new DisplayLocalEventsInteractor(testEventRepository, testOutputBoundary);
    }

    @Test
    void testExecuteWithValidInput() {
        Location userLocation = new Location(43.6532, -79.3832);
        double radius = 10.0;
        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                userLocation, radius, null
        );

        Event event1 = createTestEvent("1", "Concert", 43.6532, -79.3832, EventCategory.MUSIC);
        Event event2 = createTestEvent("2", "Game", 43.6600, -79.3900, EventCategory.SPORTS);
        testEventRepository.addEvent(event1);
        testEventRepository.addEvent(event2);

        interactor.execute(inputData);

        assertTrue(testOutputBoundary.isSuccessCalled);
        assertFalse(testOutputBoundary.isErrorCalled);
        assertNotNull(testOutputBoundary.outputData);
        assertEquals(2, testOutputBoundary.outputData.getEvents().size());
        assertTrue(testOutputBoundary.outputData.getMessage().contains("Found 2 events"));
    }

    @Test
    void testExecuteWithNullLocation() {
        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                null, 10.0, null
        );

        interactor.execute(inputData);

        assertTrue(testOutputBoundary.isErrorCalled);
        assertFalse(testOutputBoundary.isSuccessCalled);
        assertEquals("Invalid input: location and radius are required",
                testOutputBoundary.errorMessage);
    }

    @Test
    void testExecuteWithNegativeRadius() {
        Location userLocation = new Location(43.6532, -79.3832);
        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                userLocation, -5.0, null
        );

        interactor.execute(inputData);

        assertTrue(testOutputBoundary.isErrorCalled);
        assertFalse(testOutputBoundary.isSuccessCalled);
    }



    @Test
    void testExecuteWithZeroRadius() {
        Location userLocation = new Location(43.6532, -79.3832);
        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                userLocation, 0.0, null
        );

        interactor.execute(inputData);

        assertTrue(testOutputBoundary.isErrorCalled);
    }

    @Test
    void testExecuteWithCategoryFilter() {
        Location userLocation = new Location(43.6532, -79.3832);
        double radius = 10.0;
        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                userLocation, radius, EventCategory.MUSIC
        );

        Event musicEvent = createTestEvent("1", "Concert", 43.6532, -79.3832, EventCategory.MUSIC);
        Event sportsEvent = createTestEvent("2", "Game", 43.6600, -79.3900, EventCategory.SPORTS);
        testEventRepository.addEvent(musicEvent);
        testEventRepository.addEvent(sportsEvent);

        interactor.execute(inputData);

        assertTrue(testOutputBoundary.isSuccessCalled);
        assertEquals(1, testOutputBoundary.outputData.getEvents().size());
        assertEquals(EventCategory.MUSIC,
                testOutputBoundary.outputData.getEvents().get(0).getCategory());
        assertTrue(testOutputBoundary.outputData.getMessage().contains("Music"));
    }

    @Test
    void testExecuteWithNoEventsFound() {
        Location userLocation = new Location(43.6532, -79.3832);
        double radius = 10.0;
        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                userLocation, radius, null
        );

        interactor.execute(inputData);

        assertTrue(testOutputBoundary.isSuccessCalled);
        assertEquals(0, testOutputBoundary.outputData.getEvents().size());
        assertTrue(testOutputBoundary.outputData.getMessage().contains("Found 0 events"));
    }

    @Test
    void testExecuteEventsSortedByDistance() {
        Location userLocation = new Location(43.6532, -79.3832);
        double radius = 50.0;
        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                userLocation, radius, null
        );

        Event nearEvent = createTestEvent("1", "Near Event", 43.6532, -79.3832, EventCategory.MUSIC);
        Event farEvent = createTestEvent("2", "Far Event", 43.7000, -79.4000, EventCategory.MUSIC);

        testEventRepository.addEvent(farEvent);
        testEventRepository.addEvent(nearEvent);

        interactor.execute(inputData);

        assertTrue(testOutputBoundary.isSuccessCalled);
        List<Event> events = testOutputBoundary.outputData.getEvents();
        assertEquals(2, events.size());

        double distance1 = events.get(0).calculateDistanceTo(userLocation);
        double distance2 = events.get(1).calculateDistanceTo(userLocation);
        assertTrue(distance1 <= distance2,
                "Events should be sorted by distance (closest first)");
    }

    @Test
    void testEqualsWithSameEventsDifferentOrder() {
        List<Event> events1 = new ArrayList<>();
        events1.add(createTestEvent("1", "Event 1", 43.6532, -79.3832, EventCategory.MUSIC));
        events1.add(createTestEvent("2", "Event 2", 43.6600, -79.3900, EventCategory.SPORTS));

        List<Event> events2 = new ArrayList<>();
        events2.add(createTestEvent("2", "Event 2", 43.6600, -79.3900, EventCategory.SPORTS));
        events2.add(createTestEvent("1", "Event 1", 43.6532, -79.3832, EventCategory.MUSIC));

        DisplayLocalEventsOutputData outputData1 = new DisplayLocalEventsOutputData(
                events1, "message"
        );
        DisplayLocalEventsOutputData outputData2 = new DisplayLocalEventsOutputData(
                events2, "message"
        );

        assertNotEquals(outputData1, outputData2);
    }

    @Test
    void testExecuteWithException() {
        TestEventRepository errorRepository = new TestEventRepository();
        errorRepository.setShouldThrowException(true);
        DisplayLocalEventsInteractor errorInteractor = new DisplayLocalEventsInteractor(
                errorRepository, testOutputBoundary
        );

        Location userLocation = new Location(43.6532, -79.3832);
        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                userLocation, 10.0, null
        );

        errorInteractor.execute(inputData);

        assertTrue(testOutputBoundary.isErrorCalled);
        assertTrue(testOutputBoundary.errorMessage.contains("Error displaying local events"));
    }

    @Test
    void testSwitchToSaveView() {
        interactor.switchToSaveView();
        assertTrue(testOutputBoundary.isSwitchToSaveViewCalled);
    }

    @Test
    void testEventDistancesCalculated() {
        Location userLocation = new Location(43.6532, -79.3832);
        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                userLocation, 10.0, null
        );

        Event event = createTestEvent("1", "Event", 43.6532, -79.3832, EventCategory.MUSIC);
        testEventRepository.addEvent(event);

        interactor.execute(inputData);

        Map<String, Double> distances = testOutputBoundary.outputData.getEventDistances();
        assertNotNull(distances);
        assertTrue(distances.containsKey("1"));
        assertTrue(distances.get("1") >= 0);
    }

    @Test
    void testEventsOutsideRadiusFiltered() {
        Location userLocation = new Location(43.6532, -79.3832);
        double radius = 5.0;

        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                userLocation, radius, null
        );

        Event nearEvent = createTestEvent("1", "Near", 43.6532, -79.3832, EventCategory.MUSIC);
        Event farEvent = createTestEvent("2", "Far", 44.0000, -80.0000, EventCategory.MUSIC);

        testEventRepository.addEvent(nearEvent);
        testEventRepository.addEvent(farEvent);

        interactor.execute(inputData);

        assertTrue(testOutputBoundary.isSuccessCalled);
        assertEquals(1, testOutputBoundary.outputData.getEvents().size());
        assertEquals("1", testOutputBoundary.outputData.getEvents().get(0).getId());
    }

    @Test
    void testMultipleCategoriesWithFilter() {
        Location userLocation = new Location(43.6532, -79.3832);
        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                userLocation, 10.0, EventCategory.SPORTS
        );

        Event musicEvent = createTestEvent("1", "Concert", 43.6532, -79.3832, EventCategory.MUSIC);
        Event sportsEvent1 = createTestEvent("2", "Game 1", 43.6600, -79.3900, EventCategory.SPORTS);
        Event sportsEvent2 = createTestEvent("3", "Game 2", 43.6550, -79.3850, EventCategory.SPORTS);
        Event artsEvent = createTestEvent("4", "Play", 43.6580, -79.3880, EventCategory.ARTS_THEATRE);

        testEventRepository.addEvent(musicEvent);
        testEventRepository.addEvent(sportsEvent1);
        testEventRepository.addEvent(sportsEvent2);
        testEventRepository.addEvent(artsEvent);

        interactor.execute(inputData);

        assertTrue(testOutputBoundary.isSuccessCalled);
        assertEquals(2, testOutputBoundary.outputData.getEvents().size());
        for (Event event : testOutputBoundary.outputData.getEvents()) {
            assertEquals(EventCategory.SPORTS, event.getCategory());
        }
    }

    @Test
    void testEqualsReturnsTrueForEqualEvents() {
        List<Event> events = createTestEventList();

        DisplayLocalEventsOutputData outputData1 = new DisplayLocalEventsOutputData(
                events, "same message"
        );
        DisplayLocalEventsOutputData outputData2 = new DisplayLocalEventsOutputData(
                events, "same message"
        );

        assertTrue(outputData1.equals(outputData2));
    }

    @Test
    void testExecuteWithTicketmasterAdapter() {
        TestTicketmasterAdapter ticketmasterAdapter = new TestTicketmasterAdapter();
        DisplayLocalEventsInteractor ticketmasterInteractor = new DisplayLocalEventsInteractor(
                ticketmasterAdapter, testOutputBoundary
        );

        Location userLocation = new Location(43.6532, -79.3832);
        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                userLocation, 10.0, null
        );

        Event event = createTestEvent("1", "Event", 43.6532, -79.3832, EventCategory.MUSIC);
        ticketmasterAdapter.addEvent(event);

        ticketmasterInteractor.execute(inputData);

        assertTrue(testOutputBoundary.isSuccessCalled);
        assertTrue(ticketmasterAdapter.findEventsCalled);
        assertEquals(1, testOutputBoundary.outputData.getEvents().size());
    }

    @Test
    void testExecuteMessageWithCategory() {
        Location userLocation = new Location(43.6532, -79.3832);
        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                userLocation, 10.0, EventCategory.MUSIC
        );

        Event event = createTestEvent("1", "Concert", 43.6532, -79.3832, EventCategory.MUSIC);
        testEventRepository.addEvent(event);

        interactor.execute(inputData);

        assertTrue(testOutputBoundary.isSuccessCalled);
        String message = testOutputBoundary.outputData.getMessage();
        assertTrue(message.contains("Music"));
        assertTrue(message.contains("1"));
    }

    @Test
    void testExecuteMessageWithoutCategory() {
        Location userLocation = new Location(43.6532, -79.3832);
        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                userLocation, 10.0, null
        );

        Event event = createTestEvent("1", "Event", 43.6532, -79.3832, EventCategory.MUSIC);
        testEventRepository.addEvent(event);

        interactor.execute(inputData);

        assertTrue(testOutputBoundary.isSuccessCalled);
        String message = testOutputBoundary.outputData.getMessage();
        assertFalse(message.contains("Music"));
        assertTrue(message.contains("1"));
    }

    @Test
    void testCalculateEventDistancesMultipleEvents() {
        Location userLocation = new Location(43.6532, -79.3832);
        DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                userLocation, 10.0, null
        );

        Event event1 = createTestEvent("1", "Event1", 43.6532, -79.3832, EventCategory.MUSIC);
        Event event2 = createTestEvent("2", "Event2", 43.6600, -79.3900, EventCategory.MUSIC);
        testEventRepository.addEvent(event1);
        testEventRepository.addEvent(event2);

        interactor.execute(inputData);

        Map<String, Double> distances = testOutputBoundary.outputData.getEventDistances();
        assertEquals(2, distances.size());
        assertTrue(distances.containsKey("1"));
        assertTrue(distances.containsKey("2"));
        assertTrue(distances.get("1") >= 0);
        assertTrue(distances.get("2") >= 0);
    }

    @Nested
    class InputDataTests {
        @Test
        void testConstructorAndGetters() {
            Location location = new Location(43.6532, -79.3832);
            double radius = 10.0;
            EventCategory category = EventCategory.MUSIC;

            DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                    location, radius, category
            );

            assertEquals(location, inputData.getUserLocation());
            assertEquals(radius, inputData.getRadiusKm());
            assertEquals(category, inputData.getFilterCategory());
        }

        @Test
        void testIsValidWithValidData() {
            Location location = new Location(43.6532, -79.3832);
            DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                    location, 10.0, null
            );

            assertTrue(inputData.isValid());
        }

        @Test
        void testIsValidWithNullLocation() {
            DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                    null, 10.0, null
            );

            assertFalse(inputData.isValid());
        }

        @Test
        void testIsValidWithNegativeRadius() {
            Location location = new Location(43.6532, -79.3832);
            DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                    location, -5.0, null
            );

            assertFalse(inputData.isValid());
        }

        @Test
        void testIsValidWithZeroRadius() {
            Location location = new Location(43.6532, -79.3832);
            DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                    location, 0.0, null
            );

            assertFalse(inputData.isValid());
        }

        @Test
        void testHasCategoryFilterWithCategory() {
            Location location = new Location(43.6532, -79.3832);
            DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                    location, 10.0, EventCategory.MUSIC
            );

            assertTrue(inputData.hasCategoryFilter());
        }

        @Test
        void testHasCategoryFilterWithoutCategory() {
            Location location = new Location(43.6532, -79.3832);
            DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                    location, 10.0, null
            );

            assertFalse(inputData.hasCategoryFilter());
        }

        @Test
        void testToString() {
            Location location = new Location(43.6532, -79.3832);
            DisplayLocalEventsInputData inputData = new DisplayLocalEventsInputData(
                    location, 10.0, EventCategory.MUSIC
            );

            String result = inputData.toString();
            assertNotNull(result);
            assertTrue(result.contains("DisplayLocalEventsInputData"));
        }

        @Test
        void testEqualsAndHashCode() {
            Location location1 = new Location(43.6532, -79.3832);
            Location location2 = new Location(43.6532, -79.3832);

            DisplayLocalEventsInputData inputData1 = new DisplayLocalEventsInputData(
                    location1, 10.0, EventCategory.MUSIC
            );
            DisplayLocalEventsInputData inputData2 = new DisplayLocalEventsInputData(
                    location2, 10.0, EventCategory.MUSIC
            );
            DisplayLocalEventsInputData inputData3 = new DisplayLocalEventsInputData(
                    location1, 15.0, EventCategory.MUSIC
            );

            assertEquals(inputData1, inputData2);
            assertNotEquals(inputData1, inputData3);
            assertEquals(inputData1, inputData1);
            assertNotEquals(inputData1, null);
            assertNotEquals(inputData1, "string");

            assertEquals(inputData1.hashCode(), inputData2.hashCode());
        }

        @Test
        void testEqualsWithDifferentCategory() {
            Location location = new Location(43.6532, -79.3832);

            DisplayLocalEventsInputData inputData1 = new DisplayLocalEventsInputData(
                    location, 10.0, EventCategory.MUSIC
            );
            DisplayLocalEventsInputData inputData2 = new DisplayLocalEventsInputData(
                    location, 10.0, EventCategory.SPORTS
            );
            DisplayLocalEventsInputData inputData3 = new DisplayLocalEventsInputData(
                    location, 10.0, null
            );

            assertNotEquals(inputData1, inputData2);
            assertNotEquals(inputData1, inputData3);
        }

        @Test
        void testEqualsWithBothNullCategories() {
            Location location = new Location(43.6532, -79.3832);

            DisplayLocalEventsInputData inputData1 = new DisplayLocalEventsInputData(
                    location, 10.0, null
            );
            DisplayLocalEventsInputData inputData2 = new DisplayLocalEventsInputData(
                    location, 10.0, null
            );

            assertEquals(inputData1, inputData2);
            assertEquals(inputData1.hashCode(), inputData2.hashCode());
        }

        @Test
        void testEqualsWithOneNullCategory() {
            Location location = new Location(43.6532, -79.3832);

            DisplayLocalEventsInputData inputData1 = new DisplayLocalEventsInputData(
                    location, 10.0, null
            );
            DisplayLocalEventsInputData inputData2 = new DisplayLocalEventsInputData(
                    location, 10.0, EventCategory.MUSIC
            );

            assertNotEquals(inputData1, inputData2);
        }

        @Test
        void testEqualsWithDifferentLocations() {
            Location location1 = new Location(43.6532, -79.3832);
            Location location2 = new Location(43.7000, -79.4000);

            DisplayLocalEventsInputData inputData1 = new DisplayLocalEventsInputData(
                    location1, 10.0, null
            );
            DisplayLocalEventsInputData inputData2 = new DisplayLocalEventsInputData(
                    location2, 10.0, null
            );

            assertNotEquals(inputData1, inputData2);
        }
    }

    @Nested
    class OutputDataTests {
        @Test
        void testConstructorWithTwoParameters() {
            List<Event> events = createTestEventList();
            String message = "Found 2 events";

            DisplayLocalEventsOutputData outputData = new DisplayLocalEventsOutputData(
                    events, message
            );

            assertEquals(events, outputData.getEvents());
            assertEquals(message, outputData.getMessage());
            assertEquals(2, outputData.getTotalEvents());
            assertNotNull(outputData.getEventDistances());
        }

        @Test
        void testConstructorWithThreeParameters() {
            List<Event> events = createTestEventList();
            String message = "Found 2 events";
            Map<String, Double> distances = new HashMap<>();
            distances.put("1", 5.0);
            distances.put("2", 10.0);

            DisplayLocalEventsOutputData outputData = new DisplayLocalEventsOutputData(
                    events, message, distances
            );

            assertEquals(events, outputData.getEvents());
            assertEquals(message, outputData.getMessage());
            assertEquals(2, outputData.getTotalEvents());
            assertEquals(distances, outputData.getEventDistances());
        }

        @Test
        void testConstructorWithNullEvents() {
            DisplayLocalEventsOutputData outputData = new DisplayLocalEventsOutputData(
                    null, "message"
            );

            assertNotNull(outputData.getEvents());
            assertEquals(0, outputData.getEvents().size());
            assertEquals(0, outputData.getTotalEvents());
        }

        @Test
        void testConstructorWithNullMessage() {
            List<Event> events = createTestEventList();

            DisplayLocalEventsOutputData outputData = new DisplayLocalEventsOutputData(
                    events, null
            );

            assertEquals("", outputData.getMessage());
        }

        @Test
        void testConstructorWithNullDistances() {
            List<Event> events = createTestEventList();

            DisplayLocalEventsOutputData outputData = new DisplayLocalEventsOutputData(
                    events, "message", null
            );

            assertNotNull(outputData.getEventDistances());
            assertEquals(0, outputData.getEventDistances().size());
        }

        @Test
        void testConstructorWithAllNullParameters() {
            DisplayLocalEventsOutputData outputData = new DisplayLocalEventsOutputData(
                    null, null, null
            );

            assertNotNull(outputData.getEvents());
            assertEquals(0, outputData.getEvents().size());
            assertEquals("", outputData.getMessage());
            assertNotNull(outputData.getEventDistances());
            assertEquals(0, outputData.getEventDistances().size());
        }

        @Test
        void testGetDistanceForEvent() {
            List<Event> events = createTestEventList();
            Map<String, Double> distances = new HashMap<>();
            distances.put("1", 5.0);
            distances.put("2", 10.0);

            DisplayLocalEventsOutputData outputData = new DisplayLocalEventsOutputData(
                    events, "message", distances
            );

            assertEquals(5.0, outputData.getDistanceForEvent("1"));
            assertEquals(10.0, outputData.getDistanceForEvent("2"));
            assertEquals(-1.0, outputData.getDistanceForEvent("999"));
        }

        @Test
        void testGetTotalEvents() {
            List<Event> events = createTestEventList();
            DisplayLocalEventsOutputData outputData = new DisplayLocalEventsOutputData(
                    events, "message"
            );

            assertEquals(2, outputData.getTotalEvents());
        }

        @Test
        void testHasEvents() {
            List<Event> events = createTestEventList();
            DisplayLocalEventsOutputData outputData1 = new DisplayLocalEventsOutputData(
                    events, "message"
            );
            DisplayLocalEventsOutputData outputData2 = new DisplayLocalEventsOutputData(
                    new ArrayList<>(), "message"
            );

            assertTrue(outputData1.hasEvents());
            assertFalse(outputData2.hasEvents());
        }

        @Test
        void testHasDistanceData() {
            Map<String, Double> distances = new HashMap<>();
            distances.put("1", 5.0);

            DisplayLocalEventsOutputData outputData1 = new DisplayLocalEventsOutputData(
                    createTestEventList(), "message", distances
            );
            DisplayLocalEventsOutputData outputData2 = new DisplayLocalEventsOutputData(
                    createTestEventList(), "message", new HashMap<>()
            );

            assertTrue(outputData1.hasDistanceData());
            assertFalse(outputData2.hasDistanceData());
        }

        @Test
        void testToString() {
            List<Event> events = createTestEventList();
            DisplayLocalEventsOutputData outputData = new DisplayLocalEventsOutputData(
                    events, "Test message"
            );

            String result = outputData.toString();
            assertNotNull(result);
            assertTrue(result.contains("DisplayLocalEventsOutputData"));
            assertTrue(result.contains("2"));
            assertTrue(result.contains("Test message"));
        }

        @Test
        void testToStringWithDistances() {
            Map<String, Double> distances = new HashMap<>();
            distances.put("1", 5.0);

            DisplayLocalEventsOutputData outputData = new DisplayLocalEventsOutputData(
                    createTestEventList(), "Test message", distances
            );

            String result = outputData.toString();
            assertTrue(result.contains("true"));
        }

        @Test
        void testToStringWithoutDistances() {
            DisplayLocalEventsOutputData outputData = new DisplayLocalEventsOutputData(
                    createTestEventList(), "Test message", new HashMap<>()
            );

            String result = outputData.toString();
            assertTrue(result.contains("false"));
        }

        @Test
        void testEqualsAndHashCode() {
            List<Event> events1 = createTestEventList();
            List<Event> events2 = createTestEventList();
            List<Event> events3 = new ArrayList<>();

            DisplayLocalEventsOutputData outputData1 = new DisplayLocalEventsOutputData(
                    events1, "message"
            );
            DisplayLocalEventsOutputData outputData2 = new DisplayLocalEventsOutputData(
                    events2, "message"
            );
            DisplayLocalEventsOutputData outputData3 = new DisplayLocalEventsOutputData(
                    events3, "message"
            );
            DisplayLocalEventsOutputData outputData4 = new DisplayLocalEventsOutputData(
                    events1, "different message"
            );

            assertEquals(outputData1, outputData2);
            assertNotEquals(outputData1, outputData3);
            assertNotEquals(outputData1, outputData4);
            assertEquals(outputData1, outputData1);
            assertNotEquals(outputData1, null);
            assertNotEquals(outputData1, "string");

            assertEquals(outputData1.hashCode(), outputData2.hashCode());
        }

        @Test
        void testEqualsWithDifferentTotalEvents() {
            List<Event> events1 = createTestEventList();
            List<Event> events2 = new ArrayList<>();
            events2.add(createTestEvent("1", "Event", 43.6532, -79.3832, EventCategory.MUSIC));

            DisplayLocalEventsOutputData outputData1 = new DisplayLocalEventsOutputData(
                    events1, "message"
            );
            DisplayLocalEventsOutputData outputData2 = new DisplayLocalEventsOutputData(
                    events2, "message"
            );

            assertNotEquals(outputData1, outputData2);
        }

        @Test
        void testEqualsWithDifferentEvents() {
            List<Event> events1 = createTestEventList();
            List<Event> events2 = new ArrayList<>();
            events2.add(createTestEvent("3", "Different Event", 43.6532, -79.3832, EventCategory.FILM));

            DisplayLocalEventsOutputData outputData1 = new DisplayLocalEventsOutputData(
                    events1, "message"
            );
            DisplayLocalEventsOutputData outputData2 = new DisplayLocalEventsOutputData(
                    events2, "message"
            );

            assertNotEquals(outputData1, outputData2);
        }

        @Test
        void testHashCodeConsistency() {
            List<Event> events = createTestEventList();
            DisplayLocalEventsOutputData outputData = new DisplayLocalEventsOutputData(
                    events, "message"
            );

            int hash1 = outputData.hashCode();
            int hash2 = outputData.hashCode();

            assertEquals(hash1, hash2);
        }
    }

    private Event createTestEvent(String id, String name, double lat, double lon,
                                  EventCategory category) {
        return new Event(
                id,
                name,
                "Description for " + name,
                "123 Test St",
                category,
                new Location(lat, lon),
                LocalDateTime.now().plusDays(1),
                "https://example.com/image.jpg"
        );
    }

    private List<Event> createTestEventList() {
        List<Event> events = new ArrayList<>();
        events.add(new Event(
                "1",
                "Event 1",
                "Description",
                "Address 1",
                EventCategory.MUSIC,
                new Location(43.6532, -79.3832),
                LocalDateTime.now().plusDays(1),
                "image1.jpg"
        ));
        events.add(new Event(
                "2",
                "Event 2",
                "Description",
                "Address 2",
                EventCategory.SPORTS,
                new Location(43.6600, -79.3900),
                LocalDateTime.now().plusDays(2),
                "image2.jpg"
        ));
        return events;
    }

    private static class TestEventRepository implements EventRepository {
        private List<Event> events = new ArrayList<>();
        private boolean shouldThrowException = false;

        public void addEvent(Event event) {
            events.add(event);
        }

        public void setShouldThrowException(boolean value) {
            this.shouldThrowException = value;
        }

        @Override
        public List<Event> findAllEvents() {
            if (shouldThrowException) {
                throw new RuntimeException("Test exception");
            }
            return new ArrayList<>(events);
        }

        @Override
        public Optional<Event> findById(String id) {
            return events.stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst();
        }

        @Override
        public List<Event> findByName(String query) {
            return events.stream()
                    .filter(e -> e.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
        }
    }

    private static class TestTicketmasterAdapter extends TicketmasterEventRepositoryAdapter {
        private List<Event> events = new ArrayList<>();
        boolean findEventsCalled = false;

        public TestTicketmasterAdapter() {
            super(null, new Location(43.6532, -79.3832), 10.0);
        }

        public void addEvent(Event event) {
            events.add(event);
        }

        @Override
        public List<Event> findEvents(Location center, double radiusKm) {
            findEventsCalled = true;
            return new ArrayList<>(events);
        }

        @Override
        public List<Event> findAllEvents() {
            return new ArrayList<>(events);
        }

        @Override
        public Optional<Event> findById(String id) {
            return events.stream()
                    .filter(e -> e.getId().equals(id))
                    .findFirst();
        }

        @Override
        public List<Event> findByName(String query) {
            return new ArrayList<>();
        }
    }

    private static class TestOutputBoundary implements DisplayLocalEventsOutputBoundary {
        boolean isSuccessCalled = false;
        boolean isErrorCalled = false;
        boolean isSwitchToSaveViewCalled = false;
        DisplayLocalEventsOutputData outputData;
        String errorMessage;

        @Override
        public void presentSuccess(DisplayLocalEventsOutputData outputData) {
            this.isSuccessCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void presentError(String errorMessage) {
            this.isErrorCalled = true;
            this.errorMessage = errorMessage;
        }

        @Override
        public void switchToSaveView() {
            this.isSwitchToSaveViewCalled = true;
        }
    }
}
