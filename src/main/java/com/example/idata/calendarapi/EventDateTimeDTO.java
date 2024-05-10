package com.example.idata.calendarapi;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventDateTimeDTO {
    private String dateTime;
    private String timeZone;

    public EventDateTimeDTO(String dateTime, String timeZone) {
        this.dateTime = dateTime;
        this.timeZone = timeZone;
    }
}
