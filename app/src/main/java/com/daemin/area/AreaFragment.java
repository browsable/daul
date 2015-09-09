package com.daemin.area;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.daemin.common.BasicFragment;
import com.daemin.community.amazon.AmazonActivity;
import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;

public class AreaFragment extends BasicFragment {
	private static AreaFragment singleton;
	private View root;
	Button bt;
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
		Button bt = (Button)root.findViewById(R.id.amazon);
		if (layoutId > 0) {
			ibfindSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibfindSchedule);
			ibwriteSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibwriteSchedule);
			ibfindSchedule.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//SubMainActivity.getInstance().changeFragment(AreaFragment2.class, "커뮤니티", R.color.orange);
				}
			});
			bt.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					Intent intent = new Intent(SubMainActivity.getInstance(), AmazonActivity.class);
					startActivity(intent);
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
