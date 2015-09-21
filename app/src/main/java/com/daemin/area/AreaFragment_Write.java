package com.daemin.area;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.daemin.common.BasicFragment;
import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;

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
        if (layoutId > 0) {
            ibfindSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibfindSchedule);
            ibwriteSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibwriteSchedule);
           ibMenu = SubMainActivity.getInstance().getIbMenu();
           ibBack = SubMainActivity.getInstance().getIbBack();
           ibMenu.setVisibility(View.GONE);
           ibBack.setVisibility(View.VISIBLE);
           SubMainActivity.getInstance().setBackKeyName("AreaFragment");

        }



        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibMenu.setVisibility(View.VISIBLE);
                ibBack.setVisibility(View.GONE);
                ibfindSchedule.setVisibility(View.VISIBLE);
                ibwriteSchedule.setVisibility(View.VISIBLE);
                SubMainActivity.getInstance().changeFragment(AreaFragment.class, "주변시간표", R.color.maincolor);
                SubMainActivity.getInstance().setBackKeyName("");

            }
        });
        return root;
    }

    public static AreaFragment_Write getInstance(){
        return singleton;
    }
}
