package com.daemin.area;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daemin.common.BasicFragment;
import com.daemin.community.WritePostFragment;
import com.daemin.main.SubMainActivity;
import com.daemin.setting.SettingFragment;
import com.daemin.timetable.R;

public class AreaFragment2 extends BasicFragment {
    private static AreaFragment2 singleton;
    private View root;
    ImageButton ibBack, ibMenu;
    //public static RequestQueue queue;
    public AreaFragment2() {
        super(R.layout.fragment_area2, "AreaFragment");
        singleton = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        if (layoutId > 0) {
           ibMenu = SubMainActivity.getInstance().getIbMenu();
           ibBack = SubMainActivity.getInstance().getIbBack();
           ibMenu.setVisibility(View.GONE);
           ibBack.setVisibility(View.VISIBLE);
           SubMainActivity.getInstance().setBackKeyName("AreaFragment");

        }



        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubMainActivity.getInstance().changeFragment(AreaFragment.class, "주변시간표", R.color.maincolor);
                SubMainActivity.getInstance().setBackKeyName("");
                ibMenu.setVisibility(View.VISIBLE);
                ibBack.setVisibility(View.GONE);
            }
        });
        return root;
    }

    public static AreaFragment2 getInstance(){
        return singleton;
    }
}
