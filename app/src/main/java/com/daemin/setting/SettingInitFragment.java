package com.daemin.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.daemin.common.BasicFragment;
import com.daemin.dialog.DialDefault;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.main.MainActivity;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

public class SettingInitFragment extends BasicFragment {
    public SettingInitFragment() {
        super(R.layout.fragment_setting_init,"SettingInitFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        EventBus.getDefault().post(new BackKeyEvent("SettingInitFragment",new String[]{"ibBack"},new String[]{"ibMenu"}));
        ibBack = MainActivity.getInstance().getIbBack();
        btInitTable = (Button)root.findViewById(R.id.btInitTable);
        btInitSub = (Button)root.findViewById(R.id.btInitSub);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class, "설정"));
                EventBus.getDefault().post(new BackKeyEvent("",new String[]{"ibMenu"},new String[]{"ibBack"}));
            }
        });
        btInitTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialDefault dd = new DialDefault(getActivity(),
                        getActivity().getResources().getString(R.string.setting_init_table_title),
                        getActivity().getResources().getString(R.string.setting_init_q),
                        1);
                dd.show();

            }
        });
        btInitSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialDefault dd = new DialDefault(getActivity(),
                        getActivity().getResources().getString(R.string.setting_init_sub_title),
                        getActivity().getResources().getString(R.string.setting_init_q),
                        2);
                dd.show();
            }
        });
        return root;
    }
    ImageButton ibBack;
    Button btInitTable,btInitSub;

}
