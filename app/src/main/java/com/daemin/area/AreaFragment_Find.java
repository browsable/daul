package com.daemin.area;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.daemin.common.BasicFragment;
import com.daemin.event.ChangeFragEvent;
import com.daemin.timetable.R;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by HOME on 2015-09-21.
 */
public class AreaFragment_Find extends BasicFragment {
    private static AreaFragment_Find singleton;
    private View root;
    Spinner spinner;
    AutoCompleteTextView area_autocomplete;
    ImageButton ibareaSchedule, ibwriteSchedule,ibfindSchedule;
    ArrayList<String> data,spinner_data;
    ArrayAdapter<String> adapter,spinnerAdapter;
    public AreaFragment_Find() {
        super(R.layout.fragment_area_find, "AreaFragment_Find");
        singleton = this;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = super.onCreateView(inflater, container, savedInstanceState);
        if (layoutId > 0) {
            ibfindSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibfindSchedule);
            ibareaSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibareaSchedule);
            ibwriteSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibwriteSchedule);
            ibfindSchedule.setVisibility(View.GONE);
            ibareaSchedule.setVisibility(View.VISIBLE);
            ibwriteSchedule.setVisibility(View.VISIBLE);

            data = new ArrayList();
            for(int i=0;i< AreaFragment.eventList.size();i++){
                data.add(AreaFragment.eventList.get(i).getTitle());
            }
            adapter= new ArrayAdapter<>(getActivity(), R.layout.dropdown_event_list, data);
            area_autocomplete = (AutoCompleteTextView)root.findViewById(R.id.area_autocomplete);
            area_autocomplete.requestFocus();
            area_autocomplete.setThreshold(1);// will start working from first character
            area_autocomplete.setAdapter(adapter);// setting the adapter data into the
            area_autocomplete.setTextColor(Color.DKGRAY);
            area_autocomplete.setTextSize(16);
            area_autocomplete.setDropDownVerticalOffset(10);

            spinner_data = new ArrayList<String>();
            spinner_data.add("1km");
            spinner_data.add("5km");
            spinner_data.add("10km");
            spinnerAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item, spinner_data);
            spinner = (Spinner) root.findViewById(R.id.area_distance_spinner);
            spinner.setPrompt("거리선택");
            spinner.setAdapter(spinnerAdapter);

        }
        ibwriteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeFragEvent(AreaFragment_Write.class, "이벤트작성"));
                ibfindSchedule.setVisibility(View.GONE);
                ibwriteSchedule.setVisibility(View.GONE);
                ibareaSchedule.setVisibility(View.GONE);
            }
        });



        return root;
    }



    public static AreaFragment_Find getInstance(){
        return singleton;
    }
}
