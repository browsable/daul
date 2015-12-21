package com.daemin.area;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.daemin.common.BasicFragment;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

public class AreaFragment_Write extends BasicFragment {
    private static AreaFragment_Write singleton;
    private View root;
    ImageButton ibBack, ibMenu;
    ImageButton ibfindSchedule, ibwriteSchedule;
    //public static RequestQueue queue;
    public AreaFragment_Write() {
        super(R.layout.fragment_area_write, "AreaFragment");
        singleton = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
       /* if (layoutId > 0) {
            //EventBus.getDefault().post(new ViewGoneEvent(new String[]{"ibMenu"}));
            //EventBus.getDefault().post(new ViewVisibleEvent(new String[]{"ibBack"}));
            EventBus.getDefault().post(new BackKeyEvent("AreaFragment"));

        }



        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibMenu.setVisibility(View.VISIBLE);
                ibBack.setVisibility(View.GONE);
                ibfindSchedule.setVisibility(View.VISIBLE);
                ibwriteSchedule.setVisibility(View.VISIBLE);
                EventBus.getDefault().post(new ChangeFragEvent(AreaFragment.class, "주변시간표"));
                EventBus.getDefault().post(new BackKeyEvent(""));

            }
        });*/
        return root;
    }

    public static AreaFragment_Write getInstance(){
        return singleton;
    }
}
