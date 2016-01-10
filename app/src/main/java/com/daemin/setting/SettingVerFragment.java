package com.daemin.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daemin.common.BasicFragment;
import com.daemin.dialog.DialDefault;
import com.daemin.enumclass.User;
import com.daemin.event.BackKeyEvent;
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
        EventBus.getDefault().post(new BackKeyEvent("SettingVerFragment",new String[]{"ibBack"},new String[]{"ibMenu"}));
        ibBack = MainActivity.getInstance().getIbBack();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class, "설정"));
                EventBus.getDefault().post(new BackKeyEvent("",new String[]{"ibMenu"},new String[]{"ibBack"}));
            }
        });
        tvLocalVer = (TextView) root.findViewById(R.id.tvLocalVer);
        tvServerVer  = (TextView) root.findViewById(R.id.tvServerVer);
        btUpdate  = (TextView) root.findViewById(R.id.btUpdate);
        tvLocalVer.setText("v"+User.INFO.appVer);
        tvServerVer.setText("v"+User.INFO.appServerVer);
        if (User.INFO.appVer.equals(User.INFO.appServerVer)) {
            equalFlag=true;
            btUpdate.setText(getActivity().getResources().getString(R.string.setting_ver_equal));
        }else{
            equalFlag=false;
            btUpdate.setText(getActivity().getResources().getString(R.string.setting_ver_update));
        }
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!equalFlag) {
                    DialDefault dd = new DialDefault(getActivity(),
                            getActivity().getResources().getString(R.string.update_title),
                            getActivity().getResources().getString(R.string.update_content),
                            0);
                    dd.show();
                }
            }
        });
        return root;
    }
    ImageButton ibBack;
    TextView btUpdate,tvLocalVer,tvServerVer;
    Boolean equalFlag;

}
