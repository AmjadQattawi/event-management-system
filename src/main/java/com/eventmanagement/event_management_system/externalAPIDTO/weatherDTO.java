package com.eventmanagement.event_management_system.externalAPIDTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class weatherDTO {

    private Double latitude;
    private Double longitude;
    private CurrentWeather current_weather;

}
