package com.daemin.area;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daemin.common.BasicFragment;
import com.daemin.community.WritePostFragment;
import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;

public class AreaFragment extends BasicFragment {
	private static AreaFragment singleton;
	ImageButton btfindSchedule;
	ImageButton btwriteSchedule;
	private View root;
	//public static RequestQueue queue;
	public AreaFragment() {
		super(R.layout.fragment_area1, "AreaFragment");
		singleton = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = super.onCreateView(inflater, container, savedInstanceState);
		if (layoutId > 0) {
			btfindSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.btfindSchedule);
			btwriteSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.btwriteSchedule);
			btfindSchedule.setVisibility(View.VISIBLE);
			btwriteSchedule.setVisibility(View.VISIBLE);

			btwriteSchedule.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(),"Click btwriteSchedule",Toast.LENGTH_LONG);
					//SubMainActivity.getInstance().changeFragment(AreaFragment2.class, "커뮤니티", R.color.orange);
				}
			});

			btwriteSchedule.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SubMainActivity.getInstance().changeFragment(AreaFragment2.class, "이벤트작성", R.color.maincolor);
				}
			});
		}
		return root;
	}
	@Override
	public void onDestroy(){
		btfindSchedule.setVisibility(View.GONE);
		btwriteSchedule.setVisibility(View.GONE);
		super.onDestroyView();
	}

	public static AreaFragment getInstance(){
		return singleton;
	}
}
