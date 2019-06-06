package com.example.cricketfarmapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reading implements Comparable<Reading> {
    //Date is a string because the RealTime Database cannot contain Date objects and we create Reading from the RealTime Database
    private String readingDate;
    private double temperature;
    private double eggZoneHumidity;
    private double adultZoneHumidity;
    private String systemKey;
    private double spongeHumidity;

    public Reading(){}

    public Reading(String date,double temp, double eggZoneHum, double adultZoneHum,String key, double spongeHum)  {
        temperature = temp;
        eggZoneHumidity = eggZoneHum;
        adultZoneHumidity = adultZoneHum;
        readingDate = date;
        systemKey = key;
        spongeHumidity = spongeHum;
    }

    public double getSpongeHumidity() {return spongeHumidity;}

    public String getSystemKey() {return systemKey;}

    public String getReadingDate() {
        return readingDate;
    }


    public double getEggZoneHumidity() {
        return eggZoneHumidity;
    }


    public double getAdultZoneHumidity() {
        return adultZoneHumidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public boolean verifyConditions() {
        return verifyTemperature() && verifyAdultZoneHumidity() && verifyEggZoneHumidity() && verifySpongeHumidity();
    }

    public boolean verifyTemperature() {
        return temperature<32.0 && temperature>26.0;
    }

    public boolean verifyEggZoneHumidity() {
        return eggZoneHumidity > 80;
    }

    public boolean verifyAdultZoneHumidity() {
        return adultZoneHumidity > 45 && adultZoneHumidity < 55;
    }

    public boolean verifySpongeHumidity() {
        return spongeHumidity>50 && spongeHumidity<60;
    }

    @Override
    public int compareTo(Reading reading) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date current = format.parse(this.getReadingDate());
            Date comparingTo = format.parse(reading.getReadingDate());
            return current.compareTo(comparingTo);
        }
        catch (ParseException e){
            return 0;
        }

    }
}
