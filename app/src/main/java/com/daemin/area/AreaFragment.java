package com.daemin.area;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.daemin.common.BasicFragment;
import com.daemin.timetable.R;

public class AreaFragment extends BasicFragment {
	private static AreaFragment singleton;
	private View root;
	ImageButton ibfindSchedule, ibwriteSchedule;

	//public static RequestQueue queue;
	public AreaFragment() {
		super(R.layout.fragment_phone, "AreaFragment");
		singleton = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = super.onCreateView(inflater, container, savedInstanceState);
		if (layoutId > 0) {
			/*ibfindSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibfindSchedule);
			ibwriteSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibwriteSchedule);
			ibfindSchedule.setVisibility(View.VISIBLE);
			ibwriteSchedule.setVisibility(View.VISIBLE);
			ibfindSchedule.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "Click btfind Schedule", Toast.LENGTH_LONG);
					//SubMainActivity.getInstance().changeFragment(AreaFragment2.class, "커뮤니티", R.color.orange);
				}
			});

			ibwriteSchedule.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SubMainActivity.getInstance().changeFragment(AreaFragment2.class, "이벤트작성", R.color.maincolor);
					ibfindSchedule.setVisibility(View.GONE);
					ibwriteSchedule.setVisibility(View.GONE);

				}
			});*/
		}
		return root;
	}

	public static AreaFragment getInstance(){
		return singleton;
	}
}
