package com.log.cal.autolog;

import android.support.v7.widget.RecyclerView;

/**
 * Created by cal13 on 2/10/2016.
 */
public abstract class Add_Fab_Behavior extends RecyclerView.OnScrollListener
{
    static final float MINIMUM = 10;
    int scrollDist = 0;
    boolean isVisible = true;


    public void onScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        super.onScrolled(recyclerView, dx, dy);

        if (isVisible && scrollDist > MINIMUM) {
            hide();
            scrollDist = 0;
            isVisible = false;
        }
        else if (!isVisible && scrollDist < -MINIMUM) {
            show();
            scrollDist = 0;
            isVisible = true;
        }

        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrollDist += dy;
        }

    }

    public abstract void show();
    public abstract void hide();


}
