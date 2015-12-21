package com.daemin.setting;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.daemin.common.BasicFragment;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.main.MainActivity;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

public class SettingCustomerFragment extends BasicFragment {
    private LinearLayout btSettingLockActivity;
    private LinearLayout btSettingWithdrawlActivity;

    ImageButton ibMenu, ibBack;
    String[] arrayDep={};
    public SettingCustomerFragment() {
        super(R.layout.fragment_setting_customer, "SettingCustomerFragment");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
       /* ibMenu = MainActivity.getInstance().getIbMenu();
        ibBack = MainActivity.getInstance().getIbBack();
        if (layoutId > 0) {
            ibMenu.setVisibility(View.GONE);
            ibBack.setVisibility(View.VISIBLE);
            btSettingLockActivity = (LinearLayout)root.findViewById(R.id.btSettingLockActivity);
            btSettingWithdrawlActivity = (LinearLayout)root.findViewById(R.id.btSettingWithdrawlActivity);
        }
        ibBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class, "설정"));
                        EventBus.getDefault().post(new BackKeyEvent(""));
                ibMenu.setVisibility(View.VISIBLE);
                ibBack.setVisibility(View.GONE);
            }
        });

        btSettingLockActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeFragEvent(SettingLockFragment.class, "암호잠금"));
            }
        });

        btSettingWithdrawlActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeFragEvent(SettingWithdrawlFragment.class, "TimeDAO 탈퇴"));

            }
        });
*/
        return root;
    }

    @SuppressLint("NewApi")
    public AutoCompleteTextView makeACTV(View root, Context context, int adapterres, int actvres, String[] objects){

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, adapterres,objects);
        final AutoCompleteTextView actvSelect = (AutoCompleteTextView) root
                .findViewById(actvres);
        actvSelect.requestFocus();
        actvSelect.setThreshold(1);// will start working from first character
        actvSelect.setAdapter(adapter);// setting the adapter data into the
        actvSelect.setTextColor(Color.DKGRAY);
        actvSelect.setTextSize(16);
        actvSelect.setDropDownVerticalOffset(10);

        return actvSelect;
    }
}
