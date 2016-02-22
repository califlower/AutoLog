package com.log.cal.autolog;

import java.util.Date;

/**
 * Created by cal13 on 2/22/2016.
 */
public class Maint_History_Obj implements Comparable<Maint_History_Obj>
{
    Date date;
    int miles;

    public Maint_History_Obj(Date date,int miles)
    {
        this.date=date;
        this.miles=miles;
    }

    @Override
    public int compareTo(Maint_History_Obj another)
    {
        if (another.miles>miles)
        {
            return -1;
        }
        else if (another.miles<miles)
            return 1;
        else
            return 0;
    }
}
