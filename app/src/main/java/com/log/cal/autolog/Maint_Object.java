package com.log.cal.autolog;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/*Maintenance Object Class */
public class Maint_Object
{
    String name;
    BigDecimal cost;
    int interval;
    List<Date> dates_done;
    List<Integer>miles_done;

    public Maint_Object(String name,BigDecimal cost, int interval,List<Date> dates_done, List<Integer> miles_done)
    {
        this.name=name;
        this.cost=cost;
        this.interval=interval;
        this.dates_done=dates_done;
        this.miles_done=miles_done;
    }
}

