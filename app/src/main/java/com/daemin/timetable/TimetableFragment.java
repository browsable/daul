package com.daemin.timetable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.daemin.adapter.DayListAdapter;
import com.daemin.adapter.EventListAdapter;
import com.daemin.common.BasicFragment;
import com.daemin.common.Convert;
import com.daemin.data.EventlistData;
import com.daemin.enumclass.Dates;
import com.daemin.event.RemoveEnrollEvent;
import com.daemin.event.SetExpandableEvent;
import com.daemin.repository.MyTimeRepo;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import timedao.MyTime;

public class TimetableFragment extends BasicFragment {

	private PullToRefreshListView listView;
	static LinkedList<MyTime> myTimeList;
    DayListAdapter adapter;
    int year, month,preMonth,lastMonth;
	public TimetableFragment() {
		super(R.layout.fragment_timetable, "TimetableFragment");
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
		if (!TAG.equals("")) { //주,월 제외 - 일 모드일때만 실행
            year=Dates.NOW.year;
            myTimeList = new LinkedList<>();
            preMonth=lastMonth=month=Dates.NOW.month;
			listView = (PullToRefreshListView) root.findViewById(R.id.listView);
			listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
							DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
					// Update the LastUpdatedLabel
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
					// Do work to refresh the list here.
					new GetDataPreTask().execute();
				}
			});
			// Add an end-of-list listener
			listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
				@Override
				public void onLastItemVisible() {
                    new GetDataLastTask().execute();
				}
			});

			ListView actualListView = listView.getRefreshableView();
			// Need to use the Actual ListView when registering for Context Menu
			registerForContextMenu(actualListView);

            myTimeList.addFirst(new MyTime(month, 0,0));
            int firstDayOfWeek = Dates.NOW.getFirstDayOfWeek();
            for(int i=1; i<=31; i++){
                myTimeList.add(new MyTime(month, i, 2*firstDayOfWeek+1));
                ++firstDayOfWeek;
                if(firstDayOfWeek==6) firstDayOfWeek=0;
            }
            //myTimeList.addAll(MyTimeRepo.getMonthTimesForDayList(getActivity(),year,month));
            adapter = new DayListAdapter(getActivity(), myTimeList);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					//Toast.makeText(getActivity(), myTimeList.get(position).getName().toString(), Toast.LENGTH_SHORT).show();
				}
			});
		}

		return root;
	}
	private class GetDataPreTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}
		@Override
		protected void onPostExecute(Void params) {
            if(preMonth==1) {
                --year;
                preMonth=12;
            }
            else --preMonth;
            List<MyTime> list = MyTimeRepo.getMonthTimesForDayList(getActivity(),year,preMonth);
            Collections.reverse(list);
            for(MyTime mt : list)
                myTimeList.addFirst(mt);
            myTimeList.addFirst(new MyTime(preMonth,0,0));
            adapter.notifyDataSetChanged();
			// Call onRefreshComplete when the list has been refreshed.
			listView.onRefreshComplete();
			return;
		}
	}
    private class GetDataLastTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
        @Override
        protected void onPostExecute(Void params) {
            if(lastMonth==12) {
                ++year;
                lastMonth=1;
            }
            else ++lastMonth;
            myTimeList.addLast(new MyTime(lastMonth,0,0));
            for(MyTime mt : MyTimeRepo.getMonthTimesForDayList(getActivity(),year,lastMonth))
                myTimeList.addLast(mt);

            adapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            listView.onRefreshComplete();
            return;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onEventMainThread(RemoveEnrollEvent e) {
        myTimeList.clear();
        preMonth=lastMonth=month=Dates.NOW.month;
        myTimeList.addFirst(new MyTime(month, 0,0));
        myTimeList.addAll(MyTimeRepo.getMonthTimesForDayList(getActivity(), Dates.NOW.year,Dates.NOW.month));
        adapter.notifyDataSetChanged();
        listView.onRefreshComplete();
    }
}
