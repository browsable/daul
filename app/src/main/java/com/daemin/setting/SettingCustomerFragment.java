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
import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;

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
        if (layoutId > 0) {
            ibMenu = SubMainActivity.getInstance().getIbMenu();
            ibBack = SubMainActivity.getInstance().getIbBack();
            ibMenu.setVisibility(View.GONE);
            ibBack.setVisibility(View.VISIBLE);
            SubMainActivity.getInstance().setBackKeyName("SettingCustomerFragment");

            btSettingLockActivity = (LinearLayout)root.findViewById(R.id.btSettingLockActivity);
            btSettingWithdrawlActivity = (LinearLayout)root.findViewById(R.id.btSettingWithdrawlActivity);
        }
        ibBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SubMainActivity.getInstance().changeFragment(SettingFragment.class, "설정",R.color.maincolor);
                        SubMainActivity.getInstance().setBackKeyName("");
                ibMenu.setVisibility(View.VISIBLE);
                ibBack.setVisibility(View.GONE);
            }
        });

        btSettingLockActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_container, new SettingUnivFragment()).commit();*/
                SubMainActivity.getInstance().changeFragment(SettingLockFragment.class, "암호잠금", R.color.maincolor);
            }
        });

        btSettingWithdrawlActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_container, new SettingUnivFragment()).commit();*/
                SubMainActivity.getInstance().changeFragment(SettingWithdrawlFragment.class,"TimeDAO 탈퇴",R.color.maincolor);
            }
        });

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
