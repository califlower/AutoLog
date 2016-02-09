package com.log.cal.autolog;


class Bike_Object
{

    final String bike_make;
    final String bike_model;
    final int bike_year;
    final int bike_mileage;

// --Commented out by Inspection START (2/9/2016 1:24 AM):
//    public Bike_Object()
//    {
//        this.bike_make="No Make Specified";
//        this.bike_model="No Model Specified";
//        this.bike_year=0;
//
//    }
// --Commented out by Inspection STOP (2/9/2016 1:24 AM)
    public Bike_Object(String bike_make,String bike_model, int bike_year, int bike_mileage)
    {
        this.bike_make=bike_make;
        this.bike_model=bike_model;
        this.bike_year=bike_year;
        this.bike_mileage=bike_mileage;
    }

    public String toString()
    {
        return (bike_make+bike_model+bike_year);
    }
}
