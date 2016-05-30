package com.daemin.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.daemin.common.BasicFragment;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.timetable.R;
import com.daemin.main.MainActivity;

import org.greenrobot.eventbus.EventBus;

public class SettingCalendarFragment extends BasicFragment implements View.OnClickListener{
    public SettingCalendarFragment() {
        super(R.layout.fragment_setting_calendar,"SettingCalendarFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        EventBus.getDefault().post(new BackKeyEvent("SettingCalendarFragment",new String[]{"ibBack"},new String[]{"ibMenu"}));
        //ibBack = MainActivity.getInstance().getIbBack();
        ibBack = MainActivity.getInstance().getIbBack();
        btHoliKo = (RelativeLayout) root.findViewById(R.id.btHoliKo);
        btLunar = (RelativeLayout) root.findViewById(R.id.btLunar);
        bt24 = (RelativeLayout) root.findViewById(R.id.bt24);
        btSub = (RelativeLayout) root.findViewById(R.id.btSub);
        btRepeat = (RelativeLayout) root.findViewById(R.id.btRepeat);
        switch1 = (ToggleButton) root.findViewById(R.id.switch1);
        switch2 = (ToggleButton) root.findViewById(R.id.switch2);
        switch3 = (ToggleButton) root.findViewById(R.id.switch3);
        switch4 = (ToggleButton) root.findViewById(R.id.switch4);
        switch5 = (ToggleButton) root.findViewById(R.id.switch5);
        btHoliKo.setOnClickListener(this);
        btLunar.setOnClickListener(this);
        bt24.setOnClickListener(this);
        btSub.setOnClickListener(this);
        btRepeat.setOnClickListener(this);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class, "설정"));
                EventBus.getDefault().post(new BackKeyEvent("",new String[]{"ibMenu"},new String[]{"ibBack"}));
            }
        });
        return root;
    }
    ImageButton ibBack;
    RelativeLayout btHoliKo,btLunar,bt24,btSub,btRepeat;
    ToggleButton switch1,switch2,switch3,switch4,switch5;
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btHoliKo:
                if(switch1.isChecked()){
                    switch1.setBackgroundResource(R.drawable.ic_switch_on);
                    switch1.setChecked(false);
                }
                else {
                    switch1.setBackgroundResource(R.drawable.ic_switch_off);
                    switch1.setChecked(true);
                }
                break;
            case R.id.btLunar:
                if(switch2.isChecked()){
                    switch2.setBackgroundResource(R.drawable.ic_switch_off);
                    switch2.setChecked(false);
                }
                else {
                    switch2.setBackgroundResource(R.drawable.ic_switch_on);
                    switch2.setChecked(true);
                }
                break;
            case R.id.bt24:
                if(switch3.isChecked()){
                    switch3.setBackgroundResource(R.drawable.ic_switch_off);
                    switch3.setChecked(false);
                }
                else {
                    switch3.setBackgroundResource(R.drawable.ic_switch_on);
                    switch3.setChecked(true);
                }
                break;
            case R.id.btSub:
                if(switch4.isChecked()){
                    switch4.setBackgroundResource(R.drawable.ic_switch_off);
                    switch4.setChecked(false);
                }
                else {
                    switch4.setBackgroundResource(R.drawable.ic_switch_on);
                    switch4.setChecked(true);
                }
                break;
            case R.id.btRepeat:
                if(switch5.isChecked()){
                    switch5.setBackgroundResource(R.drawable.ic_switch_off);
                    switch5.setChecked(false);
                }
                else {
                    switch5.setBackgroundResource(R.drawable.ic_switch_on);
                    switch5.setChecked(true);
                }
                break;

        }
    }
}
