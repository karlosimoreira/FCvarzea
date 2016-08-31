package br.com.karlosimoreira.fcvarzea.domain.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import br.com.karlosimoreira.fcvarzea.inteface.RecyclerViewOnClickListenerHack;

/**
 * Created by Carlos on 31/08/2016.
 */
public class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener{

    private Context mContext;
    private GestureDetector mGestureDetector;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    public RecyclerViewTouchListener(Context c, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh){
        mContext = c;
        mRecyclerViewOnClickListenerHack = rvoclh;

        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);

                View cv = rv.findChildViewUnder(e.getX(), e.getY());

                if(cv != null && mRecyclerViewOnClickListenerHack != null){
                    mRecyclerViewOnClickListenerHack.onLongPressClickListener(cv, rv.getChildPosition(cv) );
                }
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View cv = rv.findChildViewUnder(e.getX(), e.getY());

                if(cv != null && mRecyclerViewOnClickListenerHack != null){
                    mRecyclerViewOnClickListenerHack.onClickListener(cv, rv.getChildPosition(cv) );
                }
                return(true);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {}
}
