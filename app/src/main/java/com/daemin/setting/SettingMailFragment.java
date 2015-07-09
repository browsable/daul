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
import android.widget.ImageButton;

import com.daemin.common.BasicFragment;
import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;

public class SettingMailFragment extends BasicFragment {
    ImageButton ibMenu, ibBack;
    AutoCompleteTextView actvSelectMail;
    String[] arrayDep={};
    public SettingMailFragment() {
        super(R.layout.fragment_setting_mail, "SettingMailFragment");
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
            SubMainActivity.getInstance().setBackKeyName("SettingMailFragment");
        }
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubMainActivity.getInstance().changeFragment(SettingFragment.class, "설정",R.color.maincolor);
                SubMainActivity.getInstance().setBackKeyName("");
                ibMenu.setVisibility(View.VISIBLE);
                ibBack.setVisibility(View.GONE);

                // 열려있는 키패드 닫기
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(actvSelectMail.getWindowToken(), 0);

            }
        });

        actvSelectMail = makeACTV(root, getActivity(),
                R.layout.dropdown_search,
                R.id.actvSelectMail,
                arrayDep);

        actvSelectMail.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String detaildep;
                detaildep = actvSelectMail.getText().toString();

                // 열려있는 키패드 닫기
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(actvSelectMail.getWindowToken(), 0);

                arrayDep = getActivity().getResources().getStringArray(R.array.array_koreatech_depname);
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
