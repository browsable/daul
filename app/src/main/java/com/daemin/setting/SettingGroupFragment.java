package com.daemin.setting;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daemin.common.BasicFragment;
import com.daemin.common.Common;
import com.daemin.common.MyRequest;
import com.daemin.data.GroupListData;
import com.daemin.enumclass.User;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.main.MainActivity;
import com.daemin.timetable.R;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class SettingGroupFragment extends BasicFragment {
    public SettingGroupFragment() {
        super(R.layout.fragment_setting_group, "SettingGroupFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        EventBus.getDefault().post(new BackKeyEvent("SettingGroupFragment", new String[]{"ibBack"}, new String[]{"ibMenu"}));
        ibBack = MainActivity.getInstance().getIbBack();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class, "설정"));
                EventBus.getDefault().post(new BackKeyEvent("", new String[]{"ibMenu"}, new String[]{"ibBack"}));
            }
        });
        univList = new ArrayList<>();
        for (GroupListData.Data d :  User.INFO.groupListData) {
            univList.add(d.getKo() + "/" + d.getTt_version() + "/" + d.getDb_version());
        }
        actvUniv = (AutoCompleteTextView) root.findViewById(R.id.actvUniv);
        btShowUniv = (ToggleButton) root.findViewById(R.id.btShowUniv);
        btEnter = (Button) root.findViewById(R.id.btEnter);
        tvHyperText = (TextView) root.findViewById(R.id.tvHyperText);
        tvHyperText.setMovementMethod(LinkMovementMethod.getInstance());
        univAdapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_univ, univList);
        SettingACTV(actvUniv, univAdapter);
        actvUniv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String[] tmp = actvUniv.getText().toString().split("/");
                groupName = tmp[0];
                ttVersion = tmp[1];
                try {
                    DBVersion = tmp[2];
                } catch (ArrayIndexOutOfBoundsException e) { //등록대기중 인 경우
                    e.printStackTrace();
                    DBVersion = "";
                }
                // 열려있는 키패드 닫기
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(actvUniv.getWindowToken(), 0);
                btShowUniv.setVisibility(View.GONE);
                btEnter.setVisibility(View.VISIBLE);
            }
        });

        btShowUniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Common.isOnline())
                    Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                else {
                    if (univList.size() == 0) MyRequest.getGroupList(getActivity());
                }
                if (btShowUniv.isChecked()) actvUniv.showDropDown();
                else actvUniv.dismissDropDown();
            }
        });

        btEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (Common.isOnline()) {
                    if (User.INFO.getGroupName().equals(groupName)) { //처음 대학을 그대로 선택시
                        if(User.INFO.getGroupDBVer().equals(DBVersion))settingUniv(); //DB버전에 변동이 없는경우
                        else {
                            Toast.makeText(DialSchedule.this, getString(R.string.univ_dbupdate), Toast.LENGTH_SHORT).show();
                            new DownloadFileFromURL().execute(groupName);
                        }
                    }else {
                        if(ttVersion.equals(getResources().getString(R.string.wait1))){
                            Toast.makeText(DialSchedule.this, getString(R.string.wait2), Toast.LENGTH_SHORT).show();
                            btShowUniv.setVisibility(View.VISIBLE);
                            btEnter.setVisibility(View.GONE);
                        }else {
                            new DownloadFileFromURL().execute(groupName);
                        }
                    }
                } else {
                    if (User.INFO.getGroupName().equals(groupName)) {
                        settingUniv();
                    }else {
                        Toast.makeText(DialSchedule.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                }*/

            }
        });

        return root;
    }
    public AutoCompleteTextView SettingACTV(AutoCompleteTextView actv, ArrayAdapter<String> adapter) {
        actv.requestFocus();
        actv.setThreshold(1);// will start working from first character
        actv.setAdapter(adapter);// setting the adapter data into the
        actv.setTextColor(Color.DKGRAY);
        actv.setDropDownVerticalOffset(User.INFO.intervalSize);
        return actv;
    }
    ImageButton ibBack;
    ToggleButton btShowUniv;
    Button btEnter;
    TextView tvHyperText;
    private ArrayAdapter<String> univAdapter;
    private ArrayList<String> univList;
    private AutoCompleteTextView actvUniv;
    private String groupName,ttVersion,DBVersion; //ttVersion: 학기버전
}
