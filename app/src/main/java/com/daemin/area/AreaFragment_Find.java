package com.daemin.area;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.daemin.common.BasicFragment;
import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;

/**
 * Created by HOME on 2015-09-21.
 */
public class AreaFragment_Find extends BasicFragment {
    private static AreaFragment_Find singleton;
    private View root;

    ImageButton ibareaSchedule, ibwriteSchedule,ibfindSchedule;

    public AreaFragment_Find() {
        super(R.layout.fragment_area_find, "AreaFragment_Find");
        singleton = this;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        if (layoutId > 0) {
            ibfindSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibfindSchedule);
            ibareaSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibareaSchedule);
            ibwriteSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibwriteSchedule);
            ibfindSchedule.setVisibility(View.GONE);
            ibareaSchedule.setVisibility(View.VISIBLE);
            ibwriteSchedule.setVisibility(View.VISIBLE);

        }
        ibwriteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubMainActivity.getInstance().changeFragment(AreaFragment_Write.class, "이벤트작성", R.color.maincolor);
                ibfindSchedule.setVisibility(View.GONE);
                ibwriteSchedule.setVisibility(View.GONE);
                ibareaSchedule.setVisibility(View.GONE);
            }
        });



        return root;
    }



    public static AreaFragment_Find getInstance(){
        return singleton;
    }
}
