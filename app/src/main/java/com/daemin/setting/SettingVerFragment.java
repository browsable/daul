package com.daemin.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daemin.common.BasicFragment;
import com.daemin.dialog.DialDefault;
import com.daemin.enumclass.User;
import com.daemin.event.ChangeFragEvent;
import com.daemin.main.MainActivity;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

public class SettingVerFragment extends BasicFragment {
    public SettingVerFragment() {
        super(R.layout.fragment_setting_ver,"SettingVerFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        ibMenu = MainActivity.getInstance().getIbMenu();
        ibBack = MainActivity.getInstance().getIbBack();
        ibMenu.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibMenu.setVisibility(View.VISIBLE);
                ibBack.setVisibility(View.GONE);
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class, "설정"));
                //EventBus.getDefault().post(new BackKeyEvent("",new String[]{"ibMenu"},new String[]{"ibBack"}));
            }
        });
        tvLocalVer = (TextView) root.findViewById(R.id.tvLocalVer);
        tvServerVer  = (TextView) root.findViewById(R.id.tvLocalVer);
        tvLocalVer.setText(User.INFO.appVer);
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialDefault dd = new DialDefault(getActivity(),
                        getActivity().getResources().getString(R.string.update_title),
                        getActivity().getResources().getString(R.string.update_content),
                        0);
                dd.show();
            }
        });
        return root;
    }
    ImageButton ibMenu, ibBack;
    TextView btUpdate,tvLocalVer,tvServerVer;

}
