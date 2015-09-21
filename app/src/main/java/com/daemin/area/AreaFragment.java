package com.daemin.area;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.daemin.adapter.EventListAdapter;
import com.daemin.common.BasicFragment;
import com.daemin.data.EventlistData;
import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.LinkedList;

public class AreaFragment extends BasicFragment {
	private static AreaFragment singleton;
	private View root;
	private PullToRefreshListView listView;
	static LinkedList<EventlistData> eventList;
	EventListAdapter event_adapter;
	ImageButton ibfindSchedule, ibwriteSchedule;

	//public static RequestQueue queue;
	public AreaFragment() {
		super(R.layout.fragment_area_list, "AreaFragment");
		singleton = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = super.onCreateView(inflater, container, savedInstanceState);
		if (layoutId > 0) {
			ibfindSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibfindSchedule);
			ibwriteSchedule = (ImageButton) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.ibwriteSchedule);
			ibfindSchedule.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});

		listView = (PullToRefreshListView)root.findViewById(R.id.event_list);

		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// Do work to refresh the list here.
				new GetDataTask().execute();
			}
		});

		// Add an end-of-list listener
		listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
			}
		});

		ListView actualListView = listView.getRefreshableView();
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);

		eventList = new LinkedList<>();
		eventList.addFirst(new EventlistData("소울관 정기공연", "2015.09.09", "posnopi13@gmail.com"));
		eventList.add(new EventlistData("커피방울", "2015.09.11", "caff@gmail.com"));
		eventList.add(new EventlistData("dazzle","2015.09.13","dazzle@gmail.com"));
		eventList.add(new EventlistData("창업동아리 다울 회의","2015.09.14","daul@gmail.com"));
		event_adapter = new EventListAdapter(getActivity().getApplicationContext(), eventList);
		listView.setAdapter(event_adapter);
		ibwriteSchedule.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SubMainActivity.getInstance().changeFragment(AreaFragment_Write.class, "이벤트작성", R.color.maincolor);
				ibfindSchedule.setVisibility(View.GONE);
				ibwriteSchedule.setVisibility(View.GONE);

			}
		});
		ibfindSchedule.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SubMainActivity.getInstance().changeFragment(AreaFragment_Find.class, "주변이벤트찾검색", R.color.maincolor);


			}
		});
		}
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getActivity(), eventList.get(position).getUser().toString(), Toast.LENGTH_SHORT).show();
			}
		});
		return root;
	}
	private class GetDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}
		@Override
		protected void onPostExecute(Void params) {
			eventList.addFirst(new EventlistData("Refresh 추가데이터 확인용", "2015.09.14", "daul@gmail.com"));
			eventList.addFirst(new EventlistData("Refresh 추가데이터 확인용", "2015.09.14", "daul@gmail.com"));
			eventList.addFirst(new EventlistData("Refresh 추가데이터 확인용", "2015.09.14", "daul@gmail.com"));
			event_adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			listView.onRefreshComplete();
			return;
		}
	}
//	public void onClick_amazon(){
//		Intent intent = new Intent(SubMainActivity.getInstance(), AmazonActivity.class);
//		startActivity(intent);
//	}
	public static AreaFragment getInstance(){
		return singleton;
	}
}
