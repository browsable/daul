package com.daemin.main.bottomdialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.daemin.adapter.HorizontalListAdapter;
import com.daemin.common.BasicFragment;
import com.daemin.common.DatabaseHandler;
import com.daemin.common.HorizontalListView;
import com.daemin.common.MyRequest;
import com.daemin.data.SubjectData;
import com.daemin.timetable.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hernia on 2015-07-05.
 */
public class UnivFragment extends BasicFragment {
    AutoCompleteTextView actvSelectUniv;
    private HorizontalListView hlv;
    Button btShowDropDown, btForward;
    View root;
    ArrayList<String> univName;
    public void setUnivName(ArrayList<String> univName) {
        this.univName = univName ;
    }
    public UnivFragment() {
        super(R.layout.bottom_dialog_univ, "UnivFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = super.onCreateView(inflater, container, savedInstanceState);

        if (layoutId > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.dropdown_search, univName);
            actvSelectUniv = (AutoCompleteTextView) root.findViewById(R.id.actvSelectUniv);
            actvSelectUniv.requestFocus();
            actvSelectUniv.setThreshold(1);// will start working from first character
            actvSelectUniv.setAdapter(adapter);// setting the adapter data into the
            actvSelectUniv.setTextColor(Color.DKGRAY);
            actvSelectUniv.setTextSize(16);
            actvSelectUniv.setDropDownVerticalOffset(10);
            btForward = (Button) root.findViewById(R.id.btForward);
            btShowDropDown = (Button) root.findViewById(R.id.btShowDropDown);
            actvSelectUniv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    String detaildep;
                    detaildep = actvSelectUniv.getText().toString();

                    // 열려있는 키패드 닫기
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actvSelectUniv.getWindowToken(), 0);
                    btShowDropDown.setVisibility(View.GONE);
                    btForward.setVisibility(View.VISIBLE);

                    btForward.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            MyRequest.DownloadSqlite();
                            hlv = (HorizontalListView) root.findViewById(R.id.hlv);
                            setupSubjectDatas();
                        }
                    });
                }
            });
        }
        return root;
    }

    private void setupSubjectDatas() {

        DatabaseHandler db = new DatabaseHandler(getActivity());
        List<SubjectData> subjects = db.getAllSubjectDatas();
        HorizontalListAdapter adapter = new HorizontalListAdapter(getActivity(), subjects);
        hlv.setAdapter(adapter);
    }
}