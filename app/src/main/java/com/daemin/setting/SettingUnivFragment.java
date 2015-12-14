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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import com.daemin.common.BasicFragment;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.main.MainActivity;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

/**
 * Created by hernia on 2015-06-20.
 */
public class SettingUnivFragment extends BasicFragment {
    ImageButton ibMenu, ibBack;
    AutoCompleteTextView actvSelectUniv, actvSelectDep;
    Button btShowDropDown1, btShowDropDown2;
    Boolean clickFlag1=false, clickFlag2=false;
    String[] arrayDep={};
    public SettingUnivFragment() {
        super(R.layout.fragment_setting_univ, "SettingUnivFragment");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        ibMenu = MainActivity.getInstance().getIbMenu();
        ibBack = MainActivity.getInstance().getIbBack();
        if (layoutId > 0) {
            ibMenu.setVisibility(View.GONE);
            ibBack.setVisibility(View.VISIBLE);
            EventBus.getDefault().post(new BackKeyEvent("SettingUnivFragment"));
            btShowDropDown1 = (Button) root.findViewById(R.id.btShowDropDown1);
            btShowDropDown2 = (Button) root.findViewById(R.id.btShowDropDown2);
        }
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class, "설정"));
                EventBus.getDefault().post(new BackKeyEvent(""));
                ibMenu.setVisibility(View.VISIBLE);
                ibBack.setVisibility(View.GONE);

                // 열려있는 키패드 닫기
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(actvSelectUniv.getWindowToken(), 0);

            }
        });

        actvSelectUniv = makeACTV(root, getActivity(),
                R.layout.dropdown_univ,
                R.id.actvSelectUniv,
                arrayDep);
        actvSelectDep = makeACTV(root, getActivity(),
                R.layout.dropdown_univ,
                R.id.actvSelectDep,
                arrayDep);


        actvSelectUniv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String detaildep;
                detaildep = actvSelectUniv.getText().toString();

                // 열려있는 키패드 닫기
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(actvSelectUniv.getWindowToken(), 0);

                //arrayDep = getActivity().getResources().getStringArray(R.array.array_koreatech_depname);
                //actvSelectDep.set
            }
        });

        actvSelectDep.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String detaildep;
                detaildep = actvSelectDep.getText().toString();
                // 열려있는 키패드 닫기
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(actvSelectDep.getWindowToken(), 0);
            }
        });

        btShowDropDown1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickFlag1) {
                    actvSelectUniv.dismissDropDown();
                    btShowDropDown1.setBackgroundResource(R.drawable.ic_expand);
                    clickFlag1 = false;
                } else {
                    actvSelectUniv.showDropDown();
                    btShowDropDown1.setBackgroundResource(R.drawable.ic_collapse);
                    clickFlag1 = true;
                }
            }
        });

        actvSelectUniv.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                btShowDropDown1.setBackgroundResource(R.drawable.ic_expand);
            }
        });


        btShowDropDown2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickFlag2) {
                    actvSelectDep.dismissDropDown();
                    btShowDropDown2.setBackgroundResource(R.drawable.ic_expand);
                    clickFlag2 = false;
                } else {
                    actvSelectDep.showDropDown();
                    btShowDropDown2.setBackgroundResource(R.drawable.ic_collapse);
                    clickFlag2 = true;
                }
            }
        });

        actvSelectDep.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                btShowDropDown2.setBackgroundResource(R.drawable.ic_expand);
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
