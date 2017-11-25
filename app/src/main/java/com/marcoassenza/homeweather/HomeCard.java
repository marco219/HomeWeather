package com.marcoassenza.homeweather;

import java.util.Date;

/**
 * Created by marcoassenza on 12/10/2017.
 */

public class HomeCard {
    int stationId;
    String lastTemperature;
    Date lastTemperatureDate;

    HomeCard(int stationId, String lastTemperature, Date lastTemperatureDate) {
        this.stationId = stationId;
        this.lastTemperature = lastTemperature;
        this.lastTemperatureDate = lastTemperatureDate;
    }
}
