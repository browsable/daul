package com.daemin.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.daemin.common.BasicFragment;
import com.daemin.event.ChangeFragEvent;
import com.daemin.main.MainActivity;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

public class SettingCalendarFragment extends BasicFragment {
    public SettingCalendarFragment() {
        super(R.layout.fragment_setting_calendar,"SettingCalendarFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        ibMenu = MainActivity.getInstance().getIbMenu();
        ibBack = MainActivity.getInstance().getIbBack();
        btHoliKo = (RelativeLayout) root.findViewById(R.id.btHoliKo);
        btLunar = (RelativeLayout) root.findViewById(R.id.btLunar);
        bt24 = (RelativeLayout) root.findViewById(R.id.bt24);
        btSub= (RelativeLayout) root.findViewById(R.id.btSub);
        btRepeat= (RelativeLayout) root.findViewById(R.id.btRepeat);
        ibMenu.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibMenu.setVisibility(View.VISIBLE);
                ibBack.setVisibility(View.GONE);
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class, "설정"));
            }
        });
        btHoliKo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btLunar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        bt24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return root;
    }
    ImageButton ibMenu, ibBack;
    RelativeLayout btHoliKo,btLunar,bt24,btSub,btRepeat;

}
