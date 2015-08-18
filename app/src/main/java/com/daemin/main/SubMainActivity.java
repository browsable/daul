package com.daemin.main;

import android.annotation.TargetApi;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.daemin.adapter.HorizontalListAdapter;
import com.daemin.area.AreaFragment;
import com.daemin.common.AsyncCallback;
import com.daemin.common.AsyncExecutor;
import com.daemin.common.BackPressCloseHandler;
import com.daemin.common.Common;
import com.daemin.common.CurrentTime;
import com.daemin.common.DatabaseHandler;
import com.daemin.common.HorizontalListView;
import com.daemin.common.MyRequest;
import com.daemin.community.CommunityFragment2;
import com.daemin.community.WriteArticleFragment;
import com.daemin.data.SubjectData;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.MyPreferences;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.enumclass.User;
import com.daemin.friend.FriendFragment;
import com.daemin.repository.GroupListFromServerRepository;
import com.daemin.setting.SettingFragment;
import com.daemin.timetable.InitSurfaceView;
import com.daemin.timetable.InitThread;
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

	InitSurfaceView InitSurfaceView;
	DrawerLayout mDrawerLayout;
	LinearLayout mLeftDrawer, llDialog,llColor, llNormal, llUniv, llIncludeUniv, llIncludeDep, llRecommend,llTitle;
	ImageButton ibMenu, ibBack, ibCalendar;
	TextView tvTitle,tvTitleYear,tvRecommendDummy;
	Button btPlus,btNormal, btUniv, btRecommend, btColor, btShowUniv,btShowDep,btShowGrade,btEnter, btWriteArticle;
	FrameLayout flSurface, frame_container;
	RelativeLayout rlBar;
	Fragment mContent = null;
	Boolean surfaceFlag = false, colorFlag = false;
	BackPressCloseHandler backPressCloseHandler;
	String BackKeyName="",colorName,korName,engName,viewMode;
	HorizontalListAdapter adapter;
	AutoCompleteTextView actvSelectUniv,actvSelectDep, actvSelectGrade;
	Boolean clickFlag1=false;
	Boolean clickFlag2=false;
	Boolean clickFlag3=false;
	GradientDrawable gd;
	Boolean adapterFlag=false;
	static int indexForTitle=0;
	private HorizontalListView hlv, hlvRecommend;
	DatabaseHandler db;
	static SubMainActivity singleton;
	public static SubMainActivity getInstance() {
		return singleton;
	}
	public ImageButton getIbBack() {return ibBack;}
	public ImageButton getIbMenu() {return ibMenu;}
	public void setBackKeyName(String backKeyName) {
		BackKeyName = backKeyName;
	}
	TextSwitcher switcher;
	//bottom drawer
	private SlidingUpPanelLayout mLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		singleton = this;
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		setContentView(R.layout.activity_main);
		if (User.USER.isSubjectDownloadState()) db = new DatabaseHandler(this);
		viewMode = MyPreferences.USERINFO.getPref().getString("viewMode","week");
		DrawMode.CURRENT.setMode(0);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		ibMenu = (ImageButton) findViewById(R.id.ibMenu);
		ibBack = (ImageButton) findViewById(R.id.ibBack);
		ibCalendar = (ImageButton) findViewById(R.id.ibCalendar);
		mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		llDialog = (LinearLayout) findViewById(R.id.llDialog);
		llColor = (LinearLayout) findViewById(R.id.llColor);
		llNormal = (LinearLayout) findViewById(R.id.llNormal);
		llUniv = (LinearLayout) findViewById(R.id.llUniv);
		llRecommend = (LinearLayout) findViewById(R.id.llRecommend);
		llIncludeUniv = (LinearLayout) findViewById(R.id.llIncludeUniv);
		llIncludeDep = (LinearLayout) findViewById(R.id.llIncludeDep);
		llTitle = (LinearLayout) findViewById(R.id.llTitle);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitleYear = (TextView) findViewById(R.id.tvTitleYear);
		tvRecommendDummy = (TextView) findViewById(R.id.tvRecommendDummy);
		btPlus = (Button) findViewById(R.id.btPlus);
		btNormal = (Button) findViewById(R.id.btNormal);
		btUniv = (Button) findViewById(R.id.btUniv);
		btRecommend = (Button) findViewById(R.id.btRecommend);
		btColor = (Button) findViewById(R.id.btColor);
		btWriteArticle = (Button) findViewById(R.id.btWriteArticle);
		hlv = (HorizontalListView) findViewById(R.id.hlv);
		hlvRecommend = (HorizontalListView) findViewById(R.id.hlvRecommend);
		mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		mLayout.setTouchEnabled(true);
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
		/*ArrayList<DialogNormalData> normalList = new ArrayList<>();
		normalList.add( new DialogNormalData(startYear,startMonthOfYear,startDayOfMonth,
				endYear,endMonthOfYear,endDayOfMonth,startHour,startMinute,endHour,endMinute,AMPM));

		ListView lvTime = (ListView) findViewById(R.id.lvTime);
		ArrayAdapter adapter = new DialogNormalListAdapter(this, normalList);
		lvTime.setAdapter(adapter);
		Common.setListViewHeightBasedOnChildren(lvTime);*/
		colorButtonSetting();

		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
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
		//Log.i("phone", User.USER.getPhoneNum());
		backPressCloseHandler = new BackPressCloseHandler(this);
	}

	/*public static ArrayList<DialogNormalData> makeNormalList(){

		new DialogNormalData(startYear,startMonthOfYear,startDayOfMonth,
				endYear,endMonthOfYear,endDayOfMonth,startHour,startMinute,endHour,endMinute,AMPM)
		if(tempTimePos!=null) {
			for(String t : tempTimePos){
				TimePos.valueOf(t).setPosState(PosState.NO_PAINT);
			}
		}
		return;
	}*/
	private void colorButtonSetting(){
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
		InitThread it = InitSurfaceView.getInitThread();

		btWriteArticle.setVisibility(View.GONE);

		switch (v.getId()) {
			case R.id.ibMenu:
				mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
				boolean drawerOpen = mDrawerLayout.isDrawerOpen(mLeftDrawer);
				if (drawerOpen) {
					mDrawerLayout.closeDrawer(mLeftDrawer);
				}
				else {
					mDrawerLayout.openDrawer(mLeftDrawer);
				}

				break;
			case R.id.ibCalendar:
				llTitle.setVisibility(View.VISIBLE);
				frame_container.setVisibility(View.GONE);
				InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
				switch(viewMode){
					case "week":
						btUniv.setVisibility(View.INVISIBLE);
						btRecommend.setVisibility(View.INVISIBLE);
						DrawMode.CURRENT.setMode(0);
						Common.stateFilter(Common.getTempTimePos(), viewMode);
						llNormal.setVisibility(View.VISIBLE);
						llUniv.setVisibility(View.GONE);
						llRecommend.setVisibility(View.GONE);
						btNormal.setTextColor(getResources().getColor(
								R.color.white));
						viewMode = "month";
						InitSurfaceView.setMode(viewMode);
						InitSurfaceView.surfaceCreated(InitSurfaceView.getHolder());
						break;
					case "month":
						btUniv.setVisibility(View.VISIBLE);
						btRecommend.setVisibility(View.VISIBLE);
						Common.stateFilter(Common.getTempTimePos(), viewMode);
						btUniv.setTextColor(getResources().getColor(
								R.color.gray));
						btRecommend.setTextColor(getResources().getColor(
								R.color.gray));
						viewMode = "week";
						InitSurfaceView.setMode(viewMode);
						InitSurfaceView.surfaceCreated(InitSurfaceView.getHolder());
						break;
				}
				surfaceFlag = false;
				BackKeyName = "";
				break;
			case R.id.btTimetable:
				llTitle.setVisibility(View.VISIBLE);
				tvTitle.setVisibility(View.GONE);
				btPlus.setVisibility(View.VISIBLE);
				ibCalendar.setVisibility(View.VISIBLE);
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
				llTitle.setVisibility(View.GONE);
				tvTitle.setVisibility(View.VISIBLE);
				btPlus.setVisibility(View.GONE);
				ibCalendar.setVisibility(View.GONE);
				mLayout.setTouchEnabled(false);
				flSurface.setVisibility(View.GONE);
				frame_container.setVisibility(View.VISIBLE);
				changeFragment(FriendFragment.class, "친구시간표", R.color.orange);
				InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
				surfaceFlag = true;
				BackKeyName = "";
				break;
			case R.id.btArea:
				llTitle.setVisibility(View.GONE);
				tvTitle.setVisibility(View.VISIBLE);
				btPlus.setVisibility(View.GONE);
				ibCalendar.setVisibility(View.GONE);
				mLayout.setTouchEnabled(false);
				flSurface.setVisibility(View.GONE);
				frame_container.setVisibility(View.VISIBLE);
				changeFragment(AreaFragment.class, "주변시간표", R.color.maincolor);
				InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
				surfaceFlag = true;
				BackKeyName = "";
				break;
			case R.id.btCommunity:
				llTitle.setVisibility(View.GONE);
				tvTitle.setVisibility(View.VISIBLE);
				btPlus.setVisibility(View.GONE);
				ibCalendar.setVisibility(View.GONE);
				mLayout.setTouchEnabled(false);
				flSurface.setVisibility(View.GONE);
				frame_container.setVisibility(View.VISIBLE);
				changeFragment(CommunityFragment2.class, "커뮤니티", R.color.orange);
				InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
				surfaceFlag = true;
				BackKeyName = "";
				btWriteArticle.setVisibility(View.VISIBLE);
				btWriteArticle.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						changeFragment(WriteArticleFragment.class, "커뮤니티", R.color.orange);
					}
				});
				break;
			case R.id.btSetting:
				llTitle.setVisibility(View.GONE);
				tvTitle.setVisibility(View.VISIBLE);
				btPlus.setVisibility(View.GONE);
				ibCalendar.setVisibility(View.GONE);
				mLayout.setTouchEnabled(false);
				flSurface.setVisibility(View.GONE);
				frame_container.setVisibility(View.VISIBLE);
				changeFragment(SettingFragment.class, "설정", R.color.maincolor);
				InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
				surfaceFlag = true;
				BackKeyName = "";
				break;
			case R.id.btNormal:
				DrawMode.CURRENT.setMode(0);
				Common.stateFilter(Common.getTempTimePos(),viewMode);
				llNormal.setVisibility(View.VISIBLE);
				llUniv.setVisibility(View.GONE);
				llRecommend.setVisibility(View.GONE);
				btNormal.setTextColor(getResources().getColor(
						R.color.white));
				btUniv.setTextColor(getResources().getColor(
						R.color.gray));
				btRecommend.setTextColor(getResources().getColor(
						R.color.gray));
				break;
			case R.id.btUniv:
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
				DrawMode.CURRENT.setMode(1);
				Common.stateFilter(Common.getTempTimePos(),viewMode);
				llNormal.setVisibility(View.GONE);
				llUniv.setVisibility(View.VISIBLE);
				llRecommend.setVisibility(View.GONE);
				btNormal.setTextColor(getResources().getColor(
						R.color.gray));
				btUniv.setTextColor(getResources().getColor(
						R.color.white));
				btRecommend.setTextColor(getResources().getColor(
						R.color.gray));
				ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
						R.layout.dropdown_search, MyRequest.getGroupListFomServer());
				actvSelectUniv = (AutoCompleteTextView) findViewById(R.id.actvSelectUniv);
				actvSelectUniv.requestFocus();
				actvSelectUniv.setThreshold(1);// will start working from first character
				actvSelectUniv.setAdapter(adapter);// setting the adapter data into the
				actvSelectUniv.setTextColor(Color.DKGRAY);
				actvSelectUniv.setTextSize(16);
				actvSelectUniv.setDropDownVerticalOffset(10);
				btEnter = (Button) findViewById(R.id.btEnter);
				btShowUniv = (Button) findViewById(R.id.btShowUniv);
				actvSelectUniv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
										DownloadSqlite(engName);
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
			case R.id.btRecommend:
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
				DrawMode.CURRENT.setMode(2);
				hlvRecommend.setVisibility(View.GONE);
				tvRecommendDummy.setVisibility(View.VISIBLE);
				tvRecommendDummy.setText(getResources().getString(R.string.select_time));
				Common.stateFilter(Common.getTempTimePos(),viewMode);
				llNormal.setVisibility(View.GONE);
				llUniv.setVisibility(View.GONE);
				llRecommend.setVisibility(View.VISIBLE);
				btNormal.setTextColor(getResources().getColor(
						R.color.gray));
				btUniv.setTextColor(getResources().getColor(
						R.color.gray));
				btRecommend.setTextColor(getResources().getColor(
						R.color.white));
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
			case R.id.btAddTime:
				break;
			case R.id.btPlus:
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
					case 2:
						DrawMode.CURRENT.setMode(2);
						break;
				}
				break;
			case R.id.btBack:
					--indexForTitle;
					if(indexForTitle<0) {
						tvTitleYear.setText(CurrentTime.backTitleYear(-indexForTitle) + getString(R.string.year));
						switcher.setText(CurrentTime.backTitleMonthWeek(this, -indexForTitle));
						it.setCurrentTime(CurrentTime.getBackDateOfWeek(-indexForTitle));
					}else{
						tvTitleYear.setText(CurrentTime.preTitleYear(indexForTitle) + getString(R.string.year));
						switcher.setText(CurrentTime.preTitleMonthWeek(this, indexForTitle));
						it.setCurrentTime(CurrentTime.getPreDateOfWeek(indexForTitle));
					}
				break;
			case R.id.btForward:
					++indexForTitle;
					if(indexForTitle<0) {
						tvTitleYear.setText(CurrentTime.backTitleYear(-indexForTitle) + getString(R.string.year));
						switcher.setText(CurrentTime.backTitleMonthWeek(this, -indexForTitle));
						it.setCurrentTime(CurrentTime.getBackDateOfWeek(-indexForTitle));
					}else{
						tvTitleYear.setText(CurrentTime.preTitleYear(indexForTitle) + getString(R.string.year));
						switcher.setText(CurrentTime.preTitleMonthWeek(this, indexForTitle));
						it.setCurrentTime(CurrentTime.getPreDateOfWeek(indexForTitle));
					}
				break;
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

				ArrayList<String> tempTimePos = new ArrayList<>();
				Common.stateFilter(Common.getTempTimePos(),viewMode);
				for (String timePos : getTimeList(((TextView) view.findViewById(R.id.time)).getText()
						.toString())) {
					tempTimePos.add(timePos);
					TimePos.valueOf(timePos).setPosState(PosState.TEMPORARY);
				}
				Common.setTempTimePos(tempTimePos);
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

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public void setupRecommendDatas(String time) {
		if(User.USER.isSubjectDownloadState()) {
			//if(db.equals(null)) db = new DatabaseHandler(context);
			List<SubjectData> recommends = db.getRecommendSubjectDatas(time);
			if(recommends.size()==0){
				hlvRecommend.setVisibility(View.GONE);
				tvRecommendDummy.setVisibility(View.VISIBLE);
				tvRecommendDummy.setText(getResources().getString(R.string.nothing_schedule));
			}else{
				hlvRecommend.setVisibility(View.VISIBLE);
				tvRecommendDummy.setVisibility(View.GONE);
			}
			if(!adapterFlag) {
				adapter = new HorizontalListAdapter(this, recommends);
				hlvRecommend.setAdapter(adapter);
				hlvRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						ArrayList<String> tempTimePos = new ArrayList<>();
						Common.stateFilter(Common.getTempTimePos(),viewMode);
						for (String timePos : getTimeList(((TextView) view.findViewById(R.id.time)).getText()
								.toString())) {
							tempTimePos.add(timePos);
							TimePos.valueOf(timePos).setPosState(PosState.TEMPORARY);
						}
						Common.setTempTimePos(tempTimePos);
					}
				});
				adapterFlag=true;
			}else{
				adapter.notifyDataSetChanged();
			}
		}else{
			Toast.makeText(SubMainActivity.this,"먼저 대학을 선택하세요", Toast.LENGTH_SHORT).show();
		}
	}

	private String[] getTimeList(String time){
		String[] timeList=null;
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
					URL url = new URL("http://hernia.cafe24.com/android/db/"+univName+".sqlite");
					URLConnection conection = url.openConnection();
					conection.connect();

					// input stream to read file - with 8k buffer
					createFolder();
					InputStream input = new BufferedInputStream(url.openStream(), 8192);
					OutputStream output = new FileOutputStream("/sdcard/.TimeDAO/"+univName+".sqlite");

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
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//appcontroller에서 앱 실행시 초기에 불러오게될 정보를 저장함
		SharedPreferences.Editor editor = MyPreferences.USERINFO.getEditor();
		editor.putBoolean("GroupListDownloadState",User.USER.isGroupListDownloadState());
		editor.putBoolean("SubjectDownloadState", User.USER.isSubjectDownloadState());
		editor.putString("EngUnivName", User.USER.getEngUnivName());
		editor.putString("viewMode",viewMode);
		editor.commit();
		Common.setLlIncludeDepIn(false);
		indexForTitle = 0;
		adapterFlag = false;
	}
}
