package com.log.cal.autolog;


import java.util.List;

@SuppressWarnings("CanBeFinal")
class Vehicle_Object
{

    String bike_make;
    String bike_model;
    int bike_year;
    int bike_mileage;
    String maint_type;
    String path;
    List<Maint_Object> maint_list;

// --Commented out by Inspection START (2/9/2016 1:24 AM):
//    public Vehicle_Object()
//    {
//        this.bike_make="No Make Specified";
//        this.bike_model="No Model Specified";
//        this.bike_year=0;
//
//    }
// --Commented out by Inspection STOP (2/9/2016 1:24 AM)
    public Vehicle_Object(String bike_make, String bike_model, int bike_year, int bike_mileage, String maint_type, String path, List<Maint_Object> maint_list )
    {
        this.bike_make=bike_make;
        this.bike_model=bike_model;
        this.bike_year=bike_year;
        this.bike_mileage=bike_mileage;
        this.maint_type=maint_type;
        this.path=path;
        this.maint_list=maint_list;

    }

    public String toString()
    {
        return (bike_make+bike_model+bike_year);
    }
}
