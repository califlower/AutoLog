package com.log.cal.autolog;

import android.support.v7.widget.RecyclerView;

/******
 * Makes the FAB dissapear and appear when the scrollview moves and stuff
 */
abstract class Add_Fab_Behavior extends RecyclerView.OnScrollListener
{
    private static final float MINIMUM = 20;
    private int scrollDist = 0;
    private boolean isVisible = true;



    public void onScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        super.onScrolled(recyclerView, dx, dy);

        if (isVisible && scrollDist > MINIMUM) {
            hide();
            scrollDist = 0;
            isVisible = false;
        }
        else if (!isVisible && scrollDist < -MINIMUM)  {
            show();
            scrollDist = 0;
            isVisible = true;
        }

        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrollDist += dy;
        }

    }


    public void  onScrollStateChanged (RecyclerView recyclerView, int newState)
    {
        super.onScrollStateChanged(recyclerView,newState);

        int itemCount = recyclerView.getLayoutManager().getItemCount();
        if (itemCount<=1)
        {
            show();
            scrollDist = 0;
            isVisible = true;
        }

    }
    public abstract void show();
    public abstract void hide();


}
