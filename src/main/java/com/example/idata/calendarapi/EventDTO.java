package com.example.idata.calendarapi;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class EventDTO {
    private String summary;
    private String location;
    private String description;
    private EventDateTimeDTO start;
    private EventDateTimeDTO end;
}
