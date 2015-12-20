package com.daemin.setting;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.daemin.common.BasicFragment;
import com.daemin.dialog.DialDefault;
import com.daemin.enumclass.User;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.main.MainActivity;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

public class SettingInitFragment extends BasicFragment {
    public SettingInitFragment() {
        super(R.layout.fragment_setting_init,"SettingIdFragment");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        ibMenu = MainActivity.getInstance().getIbMenu();
        ibBack = MainActivity.getInstance().getIbBack();
        btInitTable = (Button)root.findViewById(R.id.btInitTable);
        btInitSub = (Button)root.findViewById(R.id.btInitSub);
        if (layoutId > 0) {
            ibMenu.setVisibility(View.GONE);
            ibBack.setVisibility(View.VISIBLE);
            User.INFO.getEditor().putString("creditSum", "0").commit();
            EventBus.getDefault().post(new BackKeyEvent("SettingInitFragment"));
        }
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibMenu.setVisibility(View.VISIBLE);
                ibBack.setVisibility(View.GONE);
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class, "설정"));
                EventBus.getDefault().post(new BackKeyEvent(""));
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
    ImageButton ibMenu, ibBack;
    Button btInitTable,btInitSub;

}
