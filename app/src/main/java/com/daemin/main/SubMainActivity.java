package com.daemin.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.daemin.adapter.BottomNormalListAdapter;
import com.daemin.adapter.HorizontalListAdapter;
import com.daemin.area.AreaFragment;
import com.daemin.common.AsyncCallback;
import com.daemin.common.AsyncExecutor;
import com.daemin.common.BackPressCloseHandler;
import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.common.CurrentTime;
import com.daemin.common.DatabaseHandler;
import com.daemin.common.DialAddTimePicker;
import com.daemin.common.DialMonthPicker;
import com.daemin.common.DialWeekPicker;
import com.daemin.common.HorizontalListView;
import com.daemin.common.MyRequest;
import com.daemin.common.MyVolley;
import com.daemin.common.RoundedCornerNetworkImageView;
import com.daemin.community.CommunityFragment;
import com.daemin.data.BottomNormalData;
import com.daemin.data.SubjectData;
import com.daemin.enumclass.DayOfMonthPos;
import com.daemin.enumclass.DayOfMonthPosState;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.MyPreferences;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.enumclass.User;
import com.daemin.friend.FriendFragment;
import com.daemin.map.MapActivity;
import com.daemin.setting.SettingFragment;
import com.daemin.timetable.InitMonthThread;
import com.daemin.timetable.InitSurfaceView;
import com.daemin.timetable.InitWeekThread;
import com.daemin.timetable.R;
import com.daemin.timetable.TimetableFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SubMainActivity extends FragmentActivity {
	private static final String SAMPLE_IMAGE_URL = "http://hernia.cafe24.com/android/test2.png";
	InitSurfaceView InitSurfaceView;
	DrawerLayout mDrawerLayout;
	RoundedCornerNetworkImageView ivProfile;
	LinearLayout mLeftDrawer, llDialog,llColor, llNormal, llUniv, llIncludeUniv, llIncludeDep,llTitle;
	ImageButton ibMenu, ibBack, ibfindSchedule, ibwriteSchedule, ibareaSchedule;
	TextView tvTitle,tvTitleYear;
	EditText etPlace;
	Button btPlus,btNormal, btUniv, btColor, btShowUniv,btShowDep,btShowGrade,btEnter, btWriteArticle;
	FrameLayout flSurface, frame_container;
	RelativeLayout rlBar,rlArea;
	Fragment mContent = null;
	Boolean surfaceFlag = false, colorFlag = false;
	BackPressCloseHandler backPressCloseHandler;
	String BackKeyName="",colorName,korName,engName,barText;
	HorizontalListAdapter adapter;
	AutoCompleteTextView actvSelectUniv,actvSelectDep, actvSelectGrade;
	Boolean clickFlag1=false;
	Boolean clickFlag2=false;
	Boolean clickFlag3=false;
	GradientDrawable gd;
	Boolean adapterFlag=false;
	static int indexForTitle=0;
	int viewMode;
	private HorizontalListView hlv;
	DatabaseHandler db;
	static SubMainActivity singleton;
	public static SubMainActivity getInstance() {
		return singleton;
	}
	public ImageButton getIbBack() {return ibBack;}
	public ImageButton getIbMenu() {return ibMenu;}
	public String getBarText() {
		return barText;
	}
	public void setBackKeyName(String backKeyName) {
		BackKeyName = backKeyName;
	}
	Spinner spinner;
	ArrayAdapter<CharSequence> spinnerAdapter;
	TextSwitcher switcher;
	//bottom drawer
	private SlidingUpPanelLayout mLayout;
	ArrayList<BottomNormalData> normalList;
	ArrayAdapter normalAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		singleton = this;
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		setContentView(R.layout.activity_main);
		initUI();
		setTitle();
		colorButtonSetting();
		makeNormalList();
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if(viewMode == 2){
			barText = CurrentTime.getTitleYearMonth(this);
			btUniv.setVisibility(View.INVISIBLE);
			switcher.setText(barText);
			tvTitleYear.setVisibility(View.GONE);
		}
		//Log.i("phone", User.USER.getPhoneNum());

		backPressCloseHandler = new BackPressCloseHandler(this);

	}
	public void setTitle(){
		tvTitleYear.setText(CurrentTime.getYear() + getString(R.string.year));
		switcher.setFactory(new ViewSwitcher.ViewFactory() {
			@Override
			public View makeView() {
				TextView myText = new TextView(SubMainActivity.this);
				myText.setGravity(Gravity.BOTTOM);
				myText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				myText.setTypeface(Typeface.DEFAULT_BOLD);
				myText.setTextColor(Color.WHITE);
				return myText;
			}
		});
		Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
		Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
		switcher.setInAnimation(in);
		switcher.setOutAnimation(out);
		switcher.setText(CurrentTime.getTitleMonthWeek(this));
	}
	public void makeNormalList(){
		normalList = new ArrayList<>();
		HorizontalListView lvTime = (HorizontalListView) findViewById(R.id.lvTime);
		normalAdapter = new BottomNormalListAdapter(this, normalList);
		lvTime.setAdapter(normalAdapter);
		lvTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (viewMode) {
					case 0:
						String xth = ((TextView) view.findViewById(R.id.tvXth)).getText().toString();
						String startHour = ((TextView) view.findViewById(R.id.tvStartHour)).getText().toString();
						String startMin = ((TextView) view.findViewById(R.id.tvStartMin)).getText().toString();
						String endHour = ((TextView) view.findViewById(R.id.tvEndHour)).getText().toString();
						String endMin = ((TextView) view.findViewById(R.id.tvEndMin)).getText().toString();
						DialWeekPicker dwp = new DialWeekPicker(SubMainActivity.this, position, xth, startHour, startMin, endHour, endMin);
						dwp.show();
						break;
					case 2:
						DialMonthPicker dmp = new DialMonthPicker(SubMainActivity.this);
						dmp.show();
						break;
				}
			}
		});
		lvTime.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
										   int pos, long id) {
				switch (viewMode) {
					case 0:
						String startHour = ((TextView) view.findViewById(R.id.tvStartHour)).getText().toString();
						String endHour = ((TextView) view.findViewById(R.id.tvEndHour)).getText().toString();
						String endMin = ((TextView) view.findViewById(R.id.tvEndMin)).getText().toString();
						String xth = ((TextView) view.findViewById(R.id.tvXth)).getText().toString();
						removeWeek(Integer.parseInt(xth), Integer.parseInt(startHour), Integer.parseInt(endHour), Integer.parseInt(endMin));
						break;
					case 2:
						String tvYMD = ((TextView) view.findViewById(R.id.tvYMD)).getText().toString();
						String xth2 = ((TextView) view.findViewById(R.id.tvXth)).getText().toString();
						removeMonth(Integer.parseInt(xth2), Integer.parseInt(tvYMD.split("/")[1]));
						break;
				}
				normalList.remove(pos);
				normalAdapter.notifyDataSetChanged();
				return true;
			}
		});
	}
	//3:9:00:10:00 , 3:9:00:11:00 3:9:00:11:30
	public void addWeek(int xth, int startHour, int startMin, int endHour, int endMin){
		if(endMin!=0) ++endHour;
		else endMin=60;

		TimePos[] tp = new TimePos[endHour - startHour];
		int j = 0;
		for (int i = startHour; i < endHour; i++) {
			tp[j] = TimePos.valueOf(Convert.getxyMerge(xth, Convert.HourOfDayToYth(i)));
			if (tp[j].getPosState() == PosState.NO_PAINT) {
				if(i==startHour && startMin!=0) tp[j].setMin(startMin, 60);
				if(i==endHour-1) tp[j].setMin(0, endMin);
				tp[j].setPosState(PosState.ADJUST);
				Common.getTempTimePos().add(tp[j].name());
				Log.d("hi",tp[j].name());
			}
			++j;
		}
	}
	public void removeWeek(int xth, int startHour, int endHour, int endMin){
		if(startHour!=endHour) {
			if(endMin!=0)++endHour;
			TimePos[] tp = new TimePos[endHour - startHour];
			int j = 0;
			for (int i = startHour; i < endHour; i++) {
				tp[j] = TimePos.valueOf(Convert.getxyMerge(xth, Convert.HourOfDayToYth(i)));
				if (tp[j].getPosState() != PosState.NO_PAINT) {
					tp[j].setMin(0, 60);
					tp[j].setPosState(PosState.NO_PAINT);
				}
				++j;
			}
		}else{
			TimePos tp = TimePos.valueOf(Convert.getxyMerge(xth, Convert.HourOfDayToYth(startHour)));
			tp.setMin(0, 60);
			tp.setPosState(PosState.NO_PAINT);
		}
	}
	public void removeMonth(int xth, int day){
		InitMonthThread im = (InitMonthThread)InitSurfaceView.getInitThread();
		int yth = (im.getDayOfWeekOfLastMonth()+day)/7+1;
		DayOfMonthPos DOMP = DayOfMonthPos.valueOf(Convert.getxyMergeForMonth(xth, yth));
		if (DOMP.getPosState() != DayOfMonthPosState.NO_PAINT) {
			DOMP.setPosState(DayOfMonthPosState.NO_PAINT);
		}
	}
	public void updateWeekList(){
		normalList.clear();
		int startYth=0,startMin=0,endYth=0,endMin=0,tmpXth=0;
		int tmpStartYth=0, tmpStartMin=0, tmpEndYth=0, tmpEndMin=0;
		String YMD="";
		for (TimePos ETP : TimePos.values()) {
			if(ETP.getPosState()==PosState.PAINT||ETP.getPosState()==PosState.ADJUST){
				if(tmpXth!=ETP.getXth()){
					tmpXth = ETP.getXth();
					YMD = InitSurfaceView.getInitThread().getMonthAndDay(tmpXth);
					tmpStartYth=tmpStartMin=tmpEndYth=tmpEndMin=0;
				}
				if(tmpEndYth!=ETP.getYth()) {
					tmpStartYth = startYth = ETP.getYth();
					tmpStartMin = startMin = ETP.getStartMin();
					tmpEndMin = endMin = ETP.getEndMin();
					if(endMin!=0) tmpEndYth = endYth = startYth;
					else tmpEndYth = endYth = startYth + 2;
					normalList.add(new BottomNormalData(YMD,
							Convert.YthToHourOfDay(startYth),
							Convert.IntToString(startMin),
							Convert.YthToHourOfDay(endYth),
							Convert.IntToString(endMin),
							tmpXth
					));
				}else if(tmpEndYth== ETP.getYth()&&tmpEndMin==ETP.getStartMin()){ //
					normalList.remove(normalList.size()-1);
					startYth = tmpStartYth;
					startMin = tmpStartMin;
					tmpEndMin = endMin = ETP.getEndMin();
					if(endMin!=0) tmpEndYth = endYth = ETP.getYth();
					else tmpEndYth = endYth = ETP.getYth() + 2;
					normalList.add(new BottomNormalData(YMD,
							Convert.YthToHourOfDay(startYth),
							Convert.IntToString(startMin),
							Convert.YthToHourOfDay(endYth),
							Convert.IntToString(endMin),
							tmpXth
					));
				}
			}
		}
		normalAdapter.notifyDataSetChanged();

	}
	public void updateMonthList(){
		normalList.clear();
		int tmpXth=0,tmpYth=0;
		String YMD="";
		for (DayOfMonthPos DOMP : DayOfMonthPos.values()) {
			if (DOMP.getPosState() == DayOfMonthPosState.PAINT) {
				tmpXth = DOMP.getXth();
				tmpYth = DOMP.getYth();
				YMD = CurrentTime.getTitleMonth()+"/"+InitSurfaceView.getInitThread().getMonthAndDay(tmpXth - 1, 7 * (tmpYth - 1));
				normalList.add(new BottomNormalData(YMD,"8","00","9","00",tmpXth));
			}
		}
		normalAdapter.notifyDataSetChanged();
	}
	public void clearView(){
		normalList.clear();
		normalAdapter.notifyDataSetChanged();
		Common.stateFilter(Common.getTempTimePos(), viewMode);
	}
	public void updateListByAdd(String YMD, String startHour, String startMin, String endHour, String endMin, int xth) {
		normalList.add(new BottomNormalData(YMD, startHour, startMin, endHour, endMin, xth));
		normalAdapter.notifyDataSetChanged();
	}
	public void updateListByDial(String startHour, String startMin, String endHour, String endMin, int xth,int position) {
		normalList.remove(position);
		normalList.add(position, new BottomNormalData(InitSurfaceView.getInitThread().getMonthAndDay(xth), startHour, startMin, endHour, endMin, xth));
		normalAdapter.notifyDataSetChanged();
	}
	public void colorButtonSetting(){
		gd = (GradientDrawable) btColor.getBackground().mutate();
		String[] dialogColorBtn = getResources().getStringArray(R.array.dialogColorBtn);
		for (int i = 0; i < dialogColorBtn.length; i++) {
			int resID = getResources().getIdentifier(dialogColorBtn[i], "id", getPackageName());
			final int resColor = getResources().getIdentifier(dialogColorBtn[i], "color", getPackageName());
			ImageButton B = (ImageButton) findViewById(resID);
			B.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					llColor.setVisibility(View.INVISIBLE);
					colorFlag = false;
					colorName = getResources().getString(resColor);
					gd.setColor(getResources().getColor(resColor));
					gd.invalidateSelf();
				}
			});
		}
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mContent != null)
			getSupportFragmentManager().putFragment(outState, "mContent",
					mContent);
	}

	public void changeFragment(Class cl, String title, int barcolor) {
		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();

		String fragmentTag = cl.getSimpleName();
		Fragment newFragment = fm.findFragmentByTag(fragmentTag);

		if (newFragment != null) {
			if (newFragment == mContent) {
				// toggle();
				if (mDrawerLayout.isDrawerOpen(mLeftDrawer))
					mDrawerLayout.closeDrawer(mLeftDrawer);
				onChangedFragment(title, barcolor);

				return;
			}
		}
		if (mContent != null)
			ft.detach(mContent);

		if (newFragment == null)
			newFragment = fm.findFragmentByTag(fragmentTag);

		if (newFragment == null) {
			try {
				newFragment = (Fragment) cl.newInstance();

			} catch (Exception e) {
				newFragment = null;

			}

			if (newFragment != null) {
				if (fragmentTag != "TimetableFragment")
					ft.add(R.id.frame_container, newFragment, fragmentTag);
			}
		} else
			ft.attach(newFragment);

		if (newFragment != null) {

			if (mDrawerLayout.isDrawerOpen(mLeftDrawer))
				mDrawerLayout.closeDrawer(mLeftDrawer);
			mContent = newFragment;
			ft.commit();
			onChangedFragment(title, barcolor);
		}
	}
	private void onChangedFragment(String title, int barcolor) {
		if(!title.equals("")) tvTitle.setText(title);
		switch (barcolor) {
		case R.color.maincolor:
			rlBar.setBackgroundResource(R.color.maincolor);
			break;
			case R.color.orange:
			rlBar.setBackgroundResource(R.color.orange);
			break;
		}
	}
	// 드로어 메뉴 버튼 클릭 리스너
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public void mOnClick(View v) {

		// "글쓰기" 버튼은 메뉴를 펼칠 때를 제외하고 언제나 숨김
		if(v.getId() != R.id.ibMenu )
			btWriteArticle.setVisibility(View.GONE);

		switch (v.getId()) {
			case R.id.ibMenu:
				boolean drawerOpen = mDrawerLayout.isDrawerOpen(mLeftDrawer);
				if (drawerOpen) {
					mDrawerLayout.closeDrawer(mLeftDrawer);
				}
				else {
					mDrawerLayout.openDrawer(mLeftDrawer);
					if(mLayout.getPanelState()==SlidingUpPanelLayout.PanelState.EXPANDED)
						mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
				}
				break;
			case R.id.btTimetable:
				normalList.clear();
				normalAdapter.notifyDataSetChanged();
				rlArea.setVisibility(View.GONE);
				llTitle.setVisibility(View.VISIBLE);
				tvTitle.setVisibility(View.GONE);
				btPlus.setVisibility(View.VISIBLE);
				mLayout.setVisibility(View.VISIBLE);
				mLayout.setTouchEnabled(true);
				changeFragment(TimetableFragment.class, "", R.color.maincolor);
				flSurface.setVisibility(View.VISIBLE);
				frame_container.setVisibility(View.GONE);
				if (surfaceFlag) {
					InitSurfaceView.surfaceCreated(InitSurfaceView.getHolder());
					surfaceFlag = false;
				}
				BackKeyName = "";
				break;
			case R.id.btFriend:
				normalList.clear();
				normalAdapter.notifyDataSetChanged();
				rlArea.setVisibility(View.GONE);
				llTitle.setVisibility(View.GONE);
				tvTitle.setVisibility(View.VISIBLE);
				btPlus.setVisibility(View.GONE);
				mLayout.setTouchEnabled(false);
				flSurface.setVisibility(View.GONE);
				frame_container.setVisibility(View.VISIBLE);
				changeFragment(FriendFragment.class, "친구시간표", R.color.orange);
				InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
				surfaceFlag = true;
				BackKeyName = "";
				break;
			case R.id.btArea:case R.id.ibareaSchedule:
				ibfindSchedule.setVisibility(View.VISIBLE);
				ibareaSchedule.setVisibility(View.GONE);
				normalList.clear();
				normalAdapter.notifyDataSetChanged();
				rlArea.setVisibility(View.VISIBLE);
				llTitle.setVisibility(View.GONE);
				tvTitle.setVisibility(View.VISIBLE);
				btPlus.setVisibility(View.GONE);
				mLayout.setTouchEnabled(false);
				flSurface.setVisibility(View.GONE);
				frame_container.setVisibility(View.VISIBLE);
				changeFragment(AreaFragment.class, "주변시간표", R.color.maincolor);
				InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
				surfaceFlag = true;
				BackKeyName = "";
				break;
			case R.id.btCommunity:
				normalList.clear();
				normalAdapter.notifyDataSetChanged();
				rlArea.setVisibility(View.GONE);
				llTitle.setVisibility(View.GONE);
				tvTitle.setVisibility(View.VISIBLE);
				btPlus.setVisibility(View.GONE);
				mLayout.setTouchEnabled(false);
				InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
				flSurface.setVisibility(View.GONE);
				frame_container.setVisibility(View.VISIBLE);
				changeFragment(CommunityFragment.class, "커뮤니티", R.color.orange);
				surfaceFlag = true;
				BackKeyName = "";
				break;
			case R.id.btSetting:
				normalList.clear();
				normalAdapter.notifyDataSetChanged();
				rlArea.setVisibility(View.GONE);
				llTitle.setVisibility(View.GONE);
				tvTitle.setVisibility(View.VISIBLE);
				btPlus.setVisibility(View.GONE);
				mLayout.setTouchEnabled(false);
				flSurface.setVisibility(View.GONE);
				frame_container.setVisibility(View.VISIBLE);
				changeFragment(SettingFragment.class, "설정", R.color.maincolor);
				InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
				surfaceFlag = true;
				BackKeyName = "";
				break;
			case R.id.btNormal:
				rlArea.setVisibility(View.GONE);
				DrawMode.CURRENT.setMode(0);
				normalList.clear();
				normalAdapter.notifyDataSetChanged();
				DrawMode.CURRENT.setMode(0);
				Common.stateFilter(Common.getTempTimePos(),viewMode);
				llNormal.setVisibility(View.VISIBLE);
				llUniv.setVisibility(View.GONE);
				btNormal.setTextColor(getResources().getColor(
						R.color.white));
				btUniv.setTextColor(getResources().getColor(
						R.color.gray));
				break;
			case R.id.btUniv:
				normalList.clear();
				normalAdapter.notifyDataSetChanged();

				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
				DrawMode.CURRENT.setMode(1);
				Common.stateFilter(Common.getTempTimePos(), viewMode);
				llNormal.setVisibility(View.GONE);
				llUniv.setVisibility(View.VISIBLE);
				btNormal.setTextColor(getResources().getColor(
						R.color.gray));
				btUniv.setTextColor(getResources().getColor(
						R.color.white));
				ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.dropdown_search, MyRequest.getGroupListFomServer());
				actvSelectUniv = (AutoCompleteTextView) findViewById(R.id.actvSelectUniv);
				actvSelectUniv.requestFocus();
				actvSelectUniv.setThreshold(1);// will start working from first character
				actvSelectUniv.setAdapter(adapter);// setting the adapter data into the
				actvSelectUniv.setTextColor(Color.DKGRAY);
				actvSelectUniv.setTextSize(16);
				actvSelectUniv.setDropDownVerticalOffset(10);
				btEnter = (Button) findViewById(R.id.btEnter);
				btShowUniv = (Button) findViewById(R.id.btShowUniv);
				btShowUniv.setVisibility(View.GONE);
				btEnter.setVisibility(View.VISIBLE);
				/*actvSelectUniv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View v,
											int position, long id) {
						korName = actvSelectUniv.getText().toString();
						User.USER.setKorUnivName(korName);
						engName = GroupListFromServerRepository
								.getEngByKor(SubMainActivity.this, korName);
						User.USER.setEngUnivName(engName);


						// 열려있는 키패드 닫기
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(actvSelectUniv.getWindowToken(), 0);
						btShowUniv.setVisibility(View.GONE);
						btEnter.setVisibility(View.VISIBLE);

						btEnter.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (Common.isOnline()) {
									if (User.USER.isSubjectDownloadState()) {
										Toast.makeText(SubMainActivity.this, "이미 다운로드된 상태이므로 과목셋팅만", Toast.LENGTH_SHORT).show();
										setupSubjectDatas();
									} else {
										Toast.makeText(SubMainActivity.this, "첫 과목 다운로드", Toast.LENGTH_SHORT).show();
										DownloadSqlite("koreatech");
									}
								} else {
									if (User.USER.isSubjectDownloadState()) {
										Toast.makeText(SubMainActivity.this, "네트워크는 비연결, 오프라인으로 조회", Toast.LENGTH_SHORT).show();
										setupSubjectDatas();
									} else {
										Toast.makeText(SubMainActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
									}
								}
							}
						});
					}
				});*/
				btEnter.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (Common.isOnline()) {
							if (User.USER.isSubjectDownloadState()) {
								Toast.makeText(SubMainActivity.this, "이미 다운로드된 상태이므로 과목셋팅만", Toast.LENGTH_SHORT).show();
								setupSubjectDatas();
							} else {
								Toast.makeText(SubMainActivity.this, "첫 과목 다운로드", Toast.LENGTH_SHORT).show();
								DownloadSqlite("koreatech");
							}
						} else {
							if (User.USER.isSubjectDownloadState()) {
								Toast.makeText(SubMainActivity.this, "네트워크는 비연결, 오프라인으로 조회", Toast.LENGTH_SHORT).show();
								setupSubjectDatas();
							} else {
								Toast.makeText(SubMainActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
				btShowUniv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (clickFlag1) {
							actvSelectUniv.dismissDropDown();
							btShowUniv.setBackgroundResource(R.drawable.ic_action_expand);
							clickFlag1 = false;

						} else {
							if(!User.USER.isGroupListDownloadState()) {
								MyRequest.getGroupList();
							}
							actvSelectUniv.showDropDown();
							btShowUniv.setBackgroundResource(R.drawable.ic_action_collapse);
							clickFlag1 = true;

						}
					}
				});

				actvSelectUniv.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
					@Override
					public void onDismiss() {
						btShowUniv.setBackgroundResource(R.drawable.ic_action_expand);
					}
				});
				break;
			case R.id.btColor:
				if (!colorFlag) {
					llColor.setVisibility(View.VISIBLE);
					colorFlag = true;
				} else {
					llColor.setVisibility(View.INVISIBLE);
					colorFlag = false;
				}
				break;
			case R.id.btPlus:
				/*switch(viewMode) {
					case "week":
						break;
					case "month":
						break;
				}*/
					mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
					if(DrawMode.CURRENT.getMode()==3) DrawMode.CURRENT.setMode(1);
				break;
			case R.id.btCancel:
				mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
				switch (DrawMode.CURRENT.getMode()) {
					case 0:
						DrawMode.CURRENT.setMode(0);
						break;
					case 1:
						Common.stateFilter(Common.getTempTimePos(),viewMode);
						DrawMode.CURRENT.setMode(3);
						adapterFlag=false;
						break;
				}
				break;
			case R.id.btBack:
				Common.stateFilter(Common.getTempTimePos(), viewMode);
				normalList.clear();
				normalAdapter.notifyDataSetChanged();
				switch(viewMode) {
					case 0:
						InitWeekThread iw = (InitWeekThread) InitSurfaceView.getInitThread();
						--indexForTitle;
						if (indexForTitle < 0) {
							tvTitleYear.setText(CurrentTime.backTitleYear(-indexForTitle) + getString(R.string.year));
							barText = CurrentTime.backTitleMonthWeek(this, -indexForTitle);
							switcher.setText(barText);
							iw.setCurrentTime(CurrentTime.getBackDateOfWeek(-indexForTitle));
						} else {
							tvTitleYear.setText(CurrentTime.preTitleYear(indexForTitle) + getString(R.string.year));
							barText = CurrentTime.preTitleMonthWeek(this, indexForTitle);
							switcher.setText(barText);
							iw.setCurrentTime(CurrentTime.getPreDateOfWeek(indexForTitle));
						}
						break;
					case 2:
						InitMonthThread im = (InitMonthThread) InitSurfaceView.getInitThread();
						--indexForTitle;
						if (indexForTitle < 0) {
							barText = CurrentTime.backTitleYearMonth(this, -indexForTitle);
							switcher.setText(barText);
							im.setCurrentTime(CurrentTime.getBackDayOfLastMonth(-indexForTitle),
									CurrentTime.getBackDayOfWeekOfLastMonth(-indexForTitle),
									CurrentTime.getBackDayNumOfMonth(-indexForTitle));
						} else {
							barText = CurrentTime.preTitleYearMonth(this, indexForTitle);
							switcher.setText(barText);
							im.setCurrentTime(CurrentTime.getPreDayOfLastMonth(indexForTitle),
									CurrentTime.getPreDayOfWeekOfLastMonth(indexForTitle),
									CurrentTime.getPreDayNumOfMonth(indexForTitle));
						}
						break;
				}
				break;
			case R.id.btForward:
				Common.stateFilter(Common.getTempTimePos(), viewMode);
				normalList.clear();
				normalAdapter.notifyDataSetChanged();
				switch(viewMode) {
					case 0:
						InitWeekThread iw = (InitWeekThread) InitSurfaceView.getInitThread();
						++indexForTitle;
						if (indexForTitle < 0) {
							tvTitleYear.setText(CurrentTime.backTitleYear(-indexForTitle) + getString(R.string.year));
							barText = CurrentTime.backTitleMonthWeek(this, -indexForTitle);
							switcher.setText(barText);
							iw.setCurrentTime(CurrentTime.getBackDateOfWeek(-indexForTitle));
						} else {
							tvTitleYear.setText(CurrentTime.preTitleYear(indexForTitle) + getString(R.string.year));
							barText = CurrentTime.preTitleMonthWeek(this, indexForTitle);
							switcher.setText(barText);
							iw.setCurrentTime(CurrentTime.getPreDateOfWeek(indexForTitle));
						}
						break;
					case 2:
						InitMonthThread im = (InitMonthThread) InitSurfaceView.getInitThread();
						++indexForTitle;
						if (indexForTitle < 0) {
							barText = CurrentTime.backTitleYearMonth(this, -indexForTitle);
							switcher.setText(barText);
							im.setCurrentTime(CurrentTime.getBackDayOfLastMonth(-indexForTitle),
									CurrentTime.getBackDayOfWeekOfLastMonth(-indexForTitle),
									CurrentTime.getBackDayNumOfMonth(-indexForTitle));
						} else {
							barText = CurrentTime.preTitleYearMonth(this, indexForTitle);
							switcher.setText(barText);
							im.setCurrentTime(CurrentTime.getPreDayOfLastMonth(indexForTitle),
									CurrentTime.getPreDayOfWeekOfLastMonth(indexForTitle),
									CurrentTime.getPreDayNumOfMonth(indexForTitle));
						}
						break;
				}
				break;
			case R.id.btNew:
				DialAddTimePicker datp = null;
				switch(viewMode) {
					case 0:
						InitWeekThread iw = (InitWeekThread) InitSurfaceView.getInitThread();
						datp = new DialAddTimePicker(SubMainActivity.this, iw.getAllMonthAndDay());
						break;
					case 2:
						InitMonthThread im = (InitMonthThread) InitSurfaceView.getInitThread();
						datp = new DialAddTimePicker(SubMainActivity.this, im.getMonthData(),im.getDayOfWeekOfLastMonth());
						break;
				}
				datp.show();

				break;
			case R.id.btPlace:
				Intent i = new Intent(SubMainActivity.this, MapActivity.class);
				startActivityForResult(i, 0);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				break;
			case R.id.btShare:
				InitWeekThread iw = (InitWeekThread) InitSurfaceView.getInitThread();
				datp = new DialAddTimePicker(SubMainActivity.this, iw.getAllMonthAndDay());
				datp.show();
				break;
			case R.id.btAlarm:
				InitWeekThread iws = (InitWeekThread) InitSurfaceView.getInitThread();
				datp = new DialAddTimePicker(SubMainActivity.this, iws.getAllMonthAndDay());
				datp.show();
				break;
			case R.id.btRepeat:
				InitWeekThread iwss = (InitWeekThread) InitSurfaceView.getInitThread();
				datp = new DialAddTimePicker(SubMainActivity.this, iwss.getAllMonthAndDay());
				datp.show();
				break;
		}

	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK) {
			switch (requestCode) {
				case 0: //MapActivity
					String place = data.getStringExtra("place");
					etPlace.setText(place);
					break;
				default:
					break;
			}
		}//else if(resultCode==RESULT_CANCELED
	}
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private void setupSubjectDatas() {
		llIncludeDep.setVisibility(View.VISIBLE);
		Common.setLlIncludeDepIn(true);
		db = new DatabaseHandler(this);
		List<SubjectData> subjects = db.getAllSubjectDatas();
		HorizontalListAdapter adapter = new HorizontalListAdapter(this, subjects);
		hlv.setAdapter(adapter);
		hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String[] temps = null;
				Common.stateFilter(Common.getTempTimePos(), viewMode);
				for (String timePos : getTimeList(((TextView) view.findViewById(R.id.time)).getText()
						.toString())) {
					temps = timePos.split(":");
					addWeek(Integer.parseInt(temps[0])
							,Integer.parseInt(temps[1])
							,Integer.parseInt(temps[2])
							,Integer.parseInt(temps[3])
							,Integer.parseInt(temps[4]));
				}
			}
		});
		btShowDep = (Button) findViewById(R.id.btShowDep);
		btShowGrade = (Button) findViewById(R.id.btShowGrade);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
				R.layout.dropdown_search, MyRequest.getGroupListFomServer());
		actvSelectDep = (AutoCompleteTextView) findViewById(R.id.actvSelectDep);
		actvSelectDep.requestFocus();
		actvSelectDep.setThreshold(1);// will start working from first character
		actvSelectDep.setAdapter(adapter2);// setting the adapter data into the
		actvSelectDep.setTextColor(Color.DKGRAY);
		actvSelectDep.setDropDownVerticalOffset(10);
		actvSelectDep.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
									int position, long id) {

			}
		});
		btShowDep.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (clickFlag2) {
					actvSelectDep.dismissDropDown();
					btShowDep.setBackgroundResource(R.drawable.ic_action_expand);
					clickFlag2 = false;
					if (!User.USER.isGroupListDownloadState()) {
						MyRequest.getGroupList();
					}
				} else {
					actvSelectDep.showDropDown();
					btShowDep.setBackgroundResource(R.drawable.ic_action_collapse);
					clickFlag2 = true;
				}
			}
		});
		actvSelectDep.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
			@Override
			public void onDismiss() {
				btShowDep.setBackgroundResource(R.drawable.ic_action_expand);
			}
		});
		ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this,
				R.layout.dropdown_search, MyRequest.getGroupListFomServer());
		actvSelectGrade = (AutoCompleteTextView) findViewById(R.id.actvSelectGrade);
		actvSelectGrade.requestFocus();
		actvSelectGrade.setThreshold(1);// will start working from first character
		actvSelectGrade.setAdapter(adapter3);// setting the adapter data into the
		actvSelectGrade.setTextColor(Color.DKGRAY);
		actvSelectGrade.setDropDownVerticalOffset(10);
		actvSelectGrade.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
									int position, long id) {

			}
		});
		btShowGrade.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (clickFlag3) {
					actvSelectGrade.dismissDropDown();
					btShowGrade.setBackgroundResource(R.drawable.ic_action_expand);
					clickFlag3 = false;
					if (!User.USER.isGroupListDownloadState()) {
						MyRequest.getGroupList();
					}
				} else {
					actvSelectGrade.showDropDown();
					btShowGrade.setBackgroundResource(R.drawable.ic_action_collapse);
					clickFlag3 = true;
				}
			}
		});
		actvSelectGrade.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
			@Override
			public void onDismiss() {
				btShowGrade.setBackgroundResource(R.drawable.ic_action_expand);
			}
		});
	}
	private String[] getTimeList(String time){
		String[] timeList=null; //요일 인덱스와 이벤트에 따른 시간(시작~끝)으로 자름, 예)3:9:00:10:00/3:14:00:15:00을 분리
		try {
			if(!time.equals(null))
				timeList = time.split("/");
			else
				return timeList;
		}catch(NullPointerException e){
			Toast.makeText(SubMainActivity.this, getString(R.string.e_learning), Toast.LENGTH_SHORT).show();
		}
		return timeList;
	}
	public void DownloadSqlite(final String univName) {
		// 비동기로 실행될 코드
		Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				int count;
				try {
					URL url = new URL("http://hernia.cafe24.com/android/db/"+univName+"/subject.sqlite");
					URLConnection conection = url.openConnection();
					conection.connect();

					// input stream to read file - with 8k buffer
					createFolder();
					InputStream input = new BufferedInputStream(url.openStream(), 8192);
					OutputStream output = new FileOutputStream("/sdcard/.TimeDAO/subject.sqlite");

					///data/data/com.daemin.timetable/databases
					byte data[] = new byte[2048];


					while ((count = input.read(data)) != -1) {
						// writing data to file
						output.write(data, 0, count);
					}

					// flushing output
					output.flush();

					// closing streams
					output.close();
					input.close();

				} catch (Exception e) {
					Log.e("Error: ", e.getMessage());
				}

				return null;
			}

		};

		new AsyncExecutor<Void>()
				.setCallable(callable)
				.setCallback(callback)
				.execute();
	}

	// 비동기로 실행된 결과를 받아 처리하는 코드
	private AsyncCallback<Void> callback = new AsyncCallback<Void>() {
		@Override
		public void onResult(Void result) {
			User.USER.setSubjectDownloadState(true);
			setupSubjectDatas();
		}

		@Override
		public void exceptionOccured(Exception e) {
		}

		@Override
		public void cancelled() {
		}
	};
	public static void createFolder(){
		try{
			//check sdcard mount state
			String str = Environment.getExternalStorageState();
			if ( str.equals(Environment.MEDIA_MOUNTED)) {
				String mTargetDirPath = Environment.getExternalStorageDirectory().getAbsolutePath()
						+      "/.TimeDAO/";

				File file = new File(mTargetDirPath);
				if(!file.exists()){
					file.mkdirs();
				}
			}else{
			}
		}catch(Exception e){
		}
	}
	public void onBackPressed() {
		//super.onBackPressed();
		if (mLayout != null &&
				(mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
			mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
		} else {
			backPressCloseHandler.onBackPressed(BackKeyName);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//appcontroller에서 앱 실행시 초기에 불러오게될 정보를 저장함
		SharedPreferences.Editor editor = MyPreferences.USERINFO.getEditor();
		editor.putBoolean("GroupListDownloadState", User.USER.isGroupListDownloadState());
		editor.putBoolean("SubjectDownloadState", User.USER.isSubjectDownloadState());
		editor.putString("EngUnivName", User.USER.getEngUnivName());
		editor.putInt("viewMode", viewMode);
		editor.commit();
		Common.setLlIncludeDepIn(false);
		Common.stateFilter(Common.getTempTimePos(), viewMode);
		CurrentTime.setTitleMonth(CurrentTime.getNow().getMonthOfYear());
		indexForTitle = 0;
		adapterFlag = false;
	}

	private void initUI(){
		if (User.USER.isSubjectDownloadState()) db = new DatabaseHandler(this);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		viewMode = MyPreferences.USERINFO.getPref().getInt("viewMode",0);
		DrawMode.CURRENT.setMode(0);
		ibMenu = (ImageButton) findViewById(R.id.ibMenu);
		ibBack = (ImageButton) findViewById(R.id.ibBack);
		mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		llDialog = (LinearLayout) findViewById(R.id.llDialog);
		llColor = (LinearLayout) findViewById(R.id.llColor);
		llNormal = (LinearLayout) findViewById(R.id.llNormal);
		llUniv = (LinearLayout) findViewById(R.id.llUniv);
		llIncludeUniv = (LinearLayout) findViewById(R.id.llIncludeUniv);
		llIncludeDep = (LinearLayout) findViewById(R.id.llIncludeDep);
		llTitle = (LinearLayout) findViewById(R.id.llTitle);
		rlArea = (RelativeLayout) findViewById(R.id.rlArea);
		etPlace = (EditText) findViewById(R.id.etPlace);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitleYear = (TextView) findViewById(R.id.tvTitleYear);
		btPlus = (Button) findViewById(R.id.btPlus);
		btNormal = (Button) findViewById(R.id.btNormal);
		btUniv = (Button) findViewById(R.id.btUniv);
		btColor = (Button) findViewById(R.id.btColor);
		btWriteArticle = (Button) findViewById(R.id.btWriteArticle);
		ibfindSchedule = (ImageButton)findViewById(R.id.ibfindSchedule);
		ibwriteSchedule = (ImageButton)findViewById(R.id.ibwriteSchedule);
		ibareaSchedule = (ImageButton)findViewById(R.id.ibareaSchedule);
		hlv = (HorizontalListView) findViewById(R.id.hlv);
		mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		mLayout.setTouchEnabled(true);
		ivProfile = (RoundedCornerNetworkImageView) findViewById(R.id.ivProfile);
		ivProfile.setImageUrl(SAMPLE_IMAGE_URL,MyVolley.getImageLoader());
		rlBar = (RelativeLayout) findViewById(R.id.rlBar);
		switcher = (TextSwitcher) findViewById(R.id.switcher);
		ViewGroup.LayoutParams params = llDialog.getLayoutParams();
		DisplayMetrics dm = getResources().getDisplayMetrics();
		params.height = dm.heightPixels/3 - 10;
		llDialog.setLayoutParams(params);
		flSurface = (FrameLayout) findViewById(R.id.flSurface);
		frame_container = (FrameLayout) findViewById(R.id.frame_container);
		params = flSurface.getLayoutParams();
		params.height = dm.heightPixels * 7 / 8;
		flSurface.setLayoutParams(params);
		InitSurfaceView = new InitSurfaceView(this,viewMode);
		flSurface.addView(InitSurfaceView);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		colorName = Common.MAIN_COLOR;
		spinner = (Spinner) findViewById(R.id.spinner);

		// Create an ArrayAdapter using the string array and a default spinner
		spinnerAdapter = ArrayAdapter
				.createFromResource(this, R.array.wdm,
						R.layout.simple_spinner_item);

		// Specify the layout to use when the list of choices appears
		spinnerAdapter
				.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(spinnerAdapter);
		spinner.setSelection(viewMode);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				normalList.clear();
				normalAdapter.notifyDataSetChanged();
				llTitle.setVisibility(View.VISIBLE);
				frame_container.setVisibility(View.GONE);
				InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
				switch(position){
					case 0: //주
						indexForTitle = 0;
						barText = CurrentTime.getTitleMonthWeek(SubMainActivity.this);
						switcher.setText("");
						switcher.setText(barText);
						tvTitleYear.setVisibility(View.VISIBLE);
						btUniv.setVisibility(View.VISIBLE);
						Common.stateFilter(Common.getTempTimePos(), viewMode);
						btUniv.setTextColor(getResources().getColor(
								R.color.gray));
						viewMode = 0;
						InitSurfaceView.setMode(viewMode);
						InitSurfaceView.surfaceCreated(InitSurfaceView.getHolder());
						break;
					case 1: // 일
						break;
					case 2: // 월
						indexForTitle = 0;
						barText = CurrentTime.getTitleYearMonth(SubMainActivity.this);
						switcher.setText(barText);
						tvTitleYear.setVisibility(View.GONE);
						btUniv.setVisibility(View.INVISIBLE);
						DrawMode.CURRENT.setMode(0);
						Common.stateFilter(Common.getTempTimePos(), viewMode);
						llNormal.setVisibility(View.VISIBLE);
						llUniv.setVisibility(View.GONE);
						btNormal.setTextColor(getResources().getColor(
								R.color.white));
						viewMode = 2;
						InitSurfaceView.setMode(viewMode);
						InitSurfaceView.surfaceCreated(InitSurfaceView.getHolder());
						break;
				}
				surfaceFlag = false;
				BackKeyName = "";
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});

		mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				btPlus.setVisibility(View.GONE);
			}
			@Override
			public void onPanelExpanded(View panel) {
			}
			@Override
			public void onPanelCollapsed(View panel) {
				btPlus.setVisibility(View.VISIBLE);
			}
			@Override
			public void onPanelAnchored(View panel) {
			}
			@Override
			public void onPanelHidden(View panel) {

			}
		});
	}

}
