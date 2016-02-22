package com.log.cal.autolog;

import java.util.List;


/*Maintenance Object Class */
@SuppressWarnings("CanBeFinal")
class Maint_Object implements Comparable<Maint_Object>
{
    String name;
    Double cost;
    int interval;
    List<Maint_History_Obj> history;


    public Maint_Object(String name,Double cost, int interval,List<Maint_History_Obj> history)
    {
        this.name=name;
        this.cost=cost;
        this.interval=interval;
        this.history=history;
    }

    public int compareTo(Maint_Object m)
    {
        if ((interval+history.get(0).miles)<(m.interval+m.history.get(0).miles))
        {
            return -1;

        }
        else
            return 1;
    }
}

