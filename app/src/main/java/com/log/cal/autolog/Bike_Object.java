package com.log.cal.autolog;

/**
 * Created by cal13 on 2/8/2016.
 */
public class Bike_Object
{

    String bike_make;
    String bike_model;
    int bike_year;

    public Bike_Object()
    {
        this.bike_make="No Make Specified";
        this.bike_model="No Model Specified";
        this.bike_year=0;

    }
    public Bike_Object(String bike_make,String bike_model, int bike_year)
    {
        this.bike_make=bike_make;
        this.bike_model=bike_model;
        this.bike_year=bike_year;
    }

    public String toString()
    {
        return (bike_make+bike_model+bike_year);
    }
}
