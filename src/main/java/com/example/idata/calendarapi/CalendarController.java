package com.example.idata.calendarapi;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;
    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody EventDTO eventDTO) throws Exception {
        Event eventDetails = convertToEvent(eventDTO);
        Event createdEvent = calendarService.createEvent(eventDetails);
        return ResponseEntity.ok(createdEvent);
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEvents() {
        try {
            List<Event> events = calendarService.getEvents();
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            System.err.println("Failed to retrieve events: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Event convertToEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.setSummary(eventDTO.getSummary());
        event.setLocation(eventDTO.getLocation());
        event.setDescription(eventDTO.getDescription());

        if (eventDTO.getStart() != null && eventDTO.getEnd() != null) {
            try {
                DateTime startDateTime = new DateTime(eventDTO.getStart().getDateTime());
                DateTime endDateTime = new DateTime(eventDTO.getEnd().getDateTime());

                EventDateTime startEventDateTime = new EventDateTime()
                        .setDateTime(startDateTime)
                        .setTimeZone(eventDTO.getStart().getTimeZone());
                EventDateTime endEventDateTime = new EventDateTime()
                        .setDateTime(endDateTime)
                        .setTimeZone(eventDTO.getEnd().getTimeZone());

                event.setStart(startEventDateTime);
                event.setEnd(endEventDateTime);
            } catch (Exception e) {
                System.err.println("Error parsing DateTime: " + e.getMessage());
                // Consider throwing an exception or handling it such that the caller can react appropriately
                throw new IllegalArgumentException("Invalid date/time format provided.", e);
            }
        } else {
            throw new IllegalArgumentException("Both start and end date/times must be provided.");
        }
        return event;
    }

}
