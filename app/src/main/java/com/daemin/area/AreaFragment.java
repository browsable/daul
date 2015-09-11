package com.daemin.area;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.daemin.adapter.EventListAdapter;
import com.daemin.common.BasicFragment;
import com.daemin.community.amazon.AmazonActivity;
import com.daemin.data.EventlistData;
import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;

import java.util.ArrayList;

public class AreaFragment extends BasicFragment {
	private static AreaFragment singleton;
	private View root;
	private ListView listView;
	ArrayList<EventlistData> eventList;
	EventListAdapter event_adapter;
	ImageButton ibfindSchedule, ibwriteSchedule;

	//public static RequestQueue queue;
	public AreaFragment() {
		super(R.layout.fragment_area1, "AreaFragment");
		singleton = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = super.onCreateView(inflater, container, savedInstanceState);
		listView = (ListView)root.findViewById(R.id.event_list);
		eventList = new ArrayList<>();
		eventList.add(new EventlistData("소울관 정기공연","2015.09.09","posnopi13@gmail.com"));
		eventList.add(new EventlistData("커피방울","2015.09.11","caff@gmail.com"));
		eventList.add(new EventlistData("dazzle","2015.09.13","dazzle@gmail.com"));
		eventList.add(new EventlistData("창업동아리 다울 회의","2015.09.14","daul@gmail.com"));
		event_adapter = new EventListAdapter(getActivity().getApplicationContext(), eventList);
		listView.setAdapter(event_adapter);

		if (layoutId > 0) {
			ibfindSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibfindSchedule);
			ibwriteSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibwriteSchedule);
			ibfindSchedule.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});

			ibwriteSchedule.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SubMainActivity.getInstance().changeFragment(AreaFragment2.class, "이벤트작성", R.color.maincolor);
					ibfindSchedule.setVisibility(View.GONE);
					ibwriteSchedule.setVisibility(View.GONE);

				}
			});
		}
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getActivity(),eventList.get(position).getUser().toString(),Toast.LENGTH_SHORT).show();
			}
		});
		return root;
	}
//	public void onClick_amazon(){
//		Intent intent = new Intent(SubMainActivity.getInstance(), AmazonActivity.class);
//		startActivity(intent);
//	}
	public static AreaFragment getInstance(){
		return singleton;
	}
}
