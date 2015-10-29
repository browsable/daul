package com.daemin.main;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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

import com.daemin.adapter.HorizontalListAdapter;
import com.daemin.area.AreaFragment;
import com.daemin.common.BackPressCloseHandler;
import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.common.CurrentTime;
import com.daemin.common.DatabaseHandler;
import com.daemin.common.HorizontalListView;
import com.daemin.common.MyRequest;
import com.daemin.common.MyVolley;
import com.daemin.common.RoundedCornerNetworkImageView;
import com.daemin.community.CommunityFragment2;
import com.daemin.data.SubjectData;
import com.daemin.dialog.DialSchedule;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.MyPreferences;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.enumclass.User;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.event.ClearNormalEvent;
import com.daemin.event.SetTitleTImeEvent;
import com.daemin.friend.FriendFragment;
import com.daemin.setting.SettingFragment;
import com.daemin.timetable.InitDayFragment;
import com.daemin.timetable.InitMonthThread;
import com.daemin.timetable.InitSurfaceView;
import com.daemin.timetable.InitWeekThread;
import com.daemin.timetable.R;
import com.daemin.timetable.TimetableFragment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends FragmentActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		singleton = this;
		EventBus.getDefault().register(this);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		setContentView(R.layout.activity_main);
		initUI();
		setTitle();
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
				TextView myText = new TextView(MainActivity.this);
				myText.setGravity(Gravity.BOTTOM);
				myText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				myText.setTypeface(Typeface.DEFAULT_BOLD);
				myText.setTextColor(Color.BLACK);
				return myText;
			}
		});
		Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
		Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
		switcher.setInAnimation(in);
		switcher.setOutAnimation(out);
		SetTitleTImeEvent stickyEvent = EventBus.getDefault()
				.getStickyEvent(SetTitleTImeEvent.class);
		if (stickyEvent != null) {
			EventBus.getDefault().removeStickyEvent(stickyEvent);
			switcher.setText(stickyEvent.getTitleTime());
		}

	}

	//3:9:00:10:00 , 3:9:00:11:00, 3:9:00:11:30
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
			}
			++j;
		}
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mContent != null)
			getSupportFragmentManager().putFragment(outState, "mContent",
					mContent);
	}

	public void changeFragment(Class cl, String title) {
		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();

		String fragmentTag = cl.getSimpleName();
		Fragment newFragment = fm.findFragmentByTag(fragmentTag);

		if (newFragment != null) {
			if (newFragment == mContent) {
				// toggle();
				if (mDrawerLayout.isDrawerOpen(mLeftDrawer))
					mDrawerLayout.closeDrawer(mLeftDrawer);
				if(!title.equals("")) tvTitle.setText(title);

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
			if(!title.equals("")) tvTitle.setText(title);
		}
	}
	// 드로어 메뉴 버튼 클릭 리스너
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public void mOnClick(View v) {


		switch (v.getId()) {
			case R.id.ibMenu:
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isAcceptingText()) {
					llProfile.setVisibility(View.VISIBLE);
				} else {
					llProfile.setVisibility(View.GONE);
				}
				boolean drawerOpen = mDrawerLayout.isDrawerOpen(mLeftDrawer);
				if (drawerOpen) {
					mDrawerLayout.closeDrawer(mLeftDrawer);
				} else {
					mDrawerLayout.openDrawer(mLeftDrawer);
				}
				break;
			case R.id.btTimetable:
				EventBus.getDefault().post(new ClearNormalEvent());
				rlArea.setVisibility(View.GONE);
				llSpinner.setVisibility(View.VISIBLE);
				llTitle.setVisibility(View.VISIBLE);
				tvTitle.setVisibility(View.GONE);
				btPlus.setVisibility(View.VISIBLE);
				changeFragment(TimetableFragment.class, "");
				flSurface.setVisibility(View.VISIBLE);
				frame_container.setVisibility(View.GONE);
				if (surfaceFlag) {
					initSurfaceView.surfaceCreated(initSurfaceView.getHolder());
					surfaceFlag = false;
				}
				break;
			case R.id.btFriend:
				changeSetting();
				changeFragment(FriendFragment.class, "친구시간표");

				break;
			case R.id.btArea:
			case R.id.ibareaSchedule:
				ibfindSchedule.setVisibility(View.VISIBLE);
				ibareaSchedule.setVisibility(View.GONE);
				changeSetting();
				changeFragment(AreaFragment.class, "주변시간표");
				break;
			case R.id.btCommunity:
				changeSetting();
				changeFragment(CommunityFragment2.class, "커뮤니티");
				break;
			case R.id.btSetting:
				changeSetting();
				changeFragment(SettingFragment.class, "설정");
				break;
			case R.id.btNormal:
				rlArea.setVisibility(View.GONE);
				DrawMode.CURRENT.setMode(0);
				EventBus.getDefault().post(new ClearNormalEvent());
				DrawMode.CURRENT.setMode(0);
				Common.stateFilter(Common.getTempTimePos(), viewMode);
				break;
			case R.id.btUniv:
				EventBus.getDefault().post(new ClearNormalEvent());
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
				DrawMode.CURRENT.setMode(1);
				Common.stateFilter(Common.getTempTimePos(), viewMode);

				//ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.dropdown_search, MyRequest.getGroupListFromLocal());
				ArrayList<String> univname = new ArrayList<>();
				univname.add("한국기술교육대학교");
				ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_search, univname);
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
						/*korName = actvSelectUniv.getText().toString();
						User.USER.setKorUnivName(korName);
						engName = GroupListFromServerRepository
								.getEngByKor(SubMainActivity.this, korName);
						User.USER.setEngUnivName(engName);*/


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
										Toast.makeText(MainActivity.this, "이미 다운로드된 상태이므로 과목셋팅만", Toast.LENGTH_SHORT).show();
										setupSubjectDatas();
									} else {
										Toast.makeText(MainActivity.this, "첫 과목 다운로드", Toast.LENGTH_SHORT).show();
										new DownloadFileFromURL().execute("koreatech");
										//DownloadSqlite("koreatech");
									}
								} else {
									if (User.USER.isSubjectDownloadState()) {
										Toast.makeText(MainActivity.this, "네트워크는 비연결, 오프라인으로 조회", Toast.LENGTH_SHORT).show();
										setupSubjectDatas();
									} else {
										Toast.makeText(MainActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
									}
								}
							}
						});
					}
				});
				btEnter.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (Common.isOnline()) {
							if (User.USER.isSubjectDownloadState()) {
								Toast.makeText(MainActivity.this, "이미 다운로드된 상태이므로 과목셋팅만", Toast.LENGTH_SHORT).show();
								setupSubjectDatas();
							} else {
								Toast.makeText(MainActivity.this, "첫 과목 다운로드", Toast.LENGTH_SHORT).show();

								new DownloadFileFromURL().execute("koreatech");
								//DownloadSqlite("koreatech");
								//EventBus.getDefault().post(new DownloadSqliteEvent("koreatech"));
							}
						} else {
							if (User.USER.isSubjectDownloadState()) {
								Toast.makeText(MainActivity.this, "네트워크는 비연결, 오프라인으로 조회", Toast.LENGTH_SHORT).show();
								setupSubjectDatas();
							} else {
								Toast.makeText(MainActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
				btShowUniv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (clickFlag1) {
							actvSelectUniv.dismissDropDown();
							btShowUniv.setBackgroundResource(R.drawable.ic_expand);
							clickFlag1 = false;

						} else {
							if (!User.USER.isGroupListDownloadState()) {
								MyRequest.getGroupList();
							}
							actvSelectUniv.showDropDown();
							btShowUniv.setBackgroundResource(R.drawable.ic_collapse);
							clickFlag1 = true;

						}
					}
				});

				actvSelectUniv.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
					@Override
					public void onDismiss() {
						btShowUniv.setBackgroundResource(R.drawable.ic_expand);
					}
				});
				break;
			case R.id.btPlus:
				if(DrawMode.CURRENT.getMode()==3) DrawMode.CURRENT.setMode(1);
					Intent i = new Intent(MainActivity.this, DialSchedule.class);
					startActivity(i);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				break;
			case R.id.btAddTime:
				switch (DrawMode.CURRENT.getMode()) {
					case 0:
						break;
					case 1:
						Intent in = new Intent(MainActivity.this, DialSchedule.class);
						startActivity(in);
						overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
						break;
				}
				break;
			case R.id.btCancel:
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
				EventBus.getDefault().post(new ClearNormalEvent());
				switch(viewMode) {
					case 0:
						InitWeekThread iw = (InitWeekThread) initSurfaceView.getInitThread();
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
						InitMonthThread im = (InitMonthThread) initSurfaceView.getInitThread();
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
				EventBus.getDefault().post(new ClearNormalEvent());
				switch(viewMode) {
					case 0:
						InitWeekThread iw = (InitWeekThread) initSurfaceView.getInitThread();
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
						InitMonthThread im = (InitMonthThread) initSurfaceView.getInitThread();
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
			case R.id.left_drawer://드로어 뒤 터치를 막기위한 dummy event
				break;
		}

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private void setupSubjectDatas() {
		llIncludeDep.setVisibility(View.VISIBLE);
		Common.setLlIncludeDepIn(true);
		db = new DatabaseHandler(this);
		final List<SubjectData> subjects = db.getAllSubjectDatas();
		hoAdapter = new HorizontalListAdapter(this, subjects);
		hlv.setAdapter(hoAdapter);
		hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String[] temps = null;
				Common.stateFilter(Common.getTempTimePos(), viewMode);
				for (String timePos : getTimeList(((TextView) view.findViewById(R.id.time)).getText()
						.toString())) {
					temps = timePos.split(":");
					addWeek(Integer.parseInt(temps[0])
							, Integer.parseInt(temps[1])
							, Integer.parseInt(temps[2])
							, Integer.parseInt(temps[3])
							, Integer.parseInt(temps[4]));
				}
			}
		});
		final AutoCompleteTextView actvSub,actvDep;
		actvDep = (AutoCompleteTextView) findViewById(R.id.actvDep);
		actvSub = (AutoCompleteTextView) findViewById(R.id.actvSub);
		final Spinner depGradeSpinner = (Spinner) findViewById(R.id.depGradeSpinner);
		ArrayAdapter depGradeAdapter = ArrayAdapter
				.createFromResource(this, R.array.univ_depgrade,
						R.layout.univ_spinner_item);
		ArrayList<String>
				dep = new ArrayList<>(),
				subOrProf = new ArrayList<>();
		ArrayAdapter<String>
				depAdapter = new ArrayAdapter<>(this,
				R.layout.dropdown_searchuniv,dep),
				subAdapter = new ArrayAdapter<>(this,
				R.layout.dropdown_searchuniv,subOrProf);
		dep.addAll(db.getDepList());
		subOrProf.addAll(db.getSubOrProfList());
		actvDep.requestFocus();
		actvDep.setThreshold(1);// will start working from first character
		actvDep.setAdapter(depAdapter);// setting the adapter data into the
		actvDep.setTextColor(Color.DKGRAY);
		actvDep.setDropDownVerticalOffset(10);
		actvDep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
										int position, long id) {
				actvSub.setText("");
				subjects.clear();
				subjects.addAll(db.getAllWithDepAndGrade(actvDep.getText().toString(), String.valueOf(depGradeSpinner.getSelectedItemPosition())));
				hoAdapter.notifyDataSetChanged();
				//hoAdapter = new HorizontalListAdapter(SubMainActivity.this, subjects);
				//SubMainActivity.this.hoAdapter.notifyDataSetChanged();
			}
		});
		actvDep.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
			@Override
			public void onDismiss() {
				btShowDep.setBackgroundResource(R.drawable.ic_expand);
				actvDepFlag = false;
			}
		});
		btShowDep.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (actvDepFlag) {
					btShowDep.setBackgroundResource(R.drawable.ic_expand);
					actvDep.dismissDropDown();
					actvDepFlag = false;
				} else {
					btShowDep.setBackgroundResource(R.drawable.ic_collapse);
					actvDep.showDropDown();
					actvDepFlag = true;
				}
			}
		});
		actvSub.requestFocus();
		actvSub.setThreshold(1);// will start working from first character
		actvSub.setAdapter(subAdapter);// setting the adapter data into the
		actvSub.setTextColor(Color.DKGRAY);
		actvSub.setDropDownVerticalOffset(10);
		actvSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
									int position, long id) {
				actvDep.setText("");
				selected = true;
				depGradeSpinner.setSelection(0, false);
				subjects.clear();
				subjects.addAll(db.getAllWithSubOrProf(actvSub.getText().toString()));
				hoAdapter.notifyDataSetChanged();
			}
		});

		depGradeAdapter
				.setDropDownViewResource(R.layout.univ_spinner_dropdown_item);
		depGradeSpinner.setAdapter(depGradeAdapter);
		depGradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				if(!selected) {
					actvSub.setText("");
					subjects.clear();
					subjects.addAll(db.getAllWithDepAndGrade(actvDep.getText().toString(), String.valueOf(position)));
					hoAdapter.notifyDataSetChanged();
				}
				selected = false;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
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
			Toast.makeText(MainActivity.this, getString(R.string.e_learning), Toast.LENGTH_SHORT).show();
		}
		return timeList;
	}
	public void onBackPressed() {
		backPressCloseHandler.onBackPressed(backKeyName);
	}

	class DownloadFileFromURL extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String ...univName) {
			int count;
			try {
				URL url = new URL("http://hernia.cafe24.com/android/db/"+univName[0]+"/subject.sqlite");
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
		@Override
		protected void onPostExecute(String param) {
			User.USER.setSubjectDownloadState(true);
			setupSubjectDatas();
		}

	}
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
	public void onResume() {
		super.onResume();
		if(!EventBus.getDefault().isRegistered(this))EventBus.getDefault().register(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		//screenshot();
		Intent intent = new Intent();
		//intent.setAction(android.appwidget.action.APPWIDGET_UPDATE);
		intent.setAction(Common.ACTION_UPDATE);
		sendBroadcast(intent);
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//screenshot();
		Intent intent = new Intent();
		//intent.setAction(android.appwidget.action.APPWIDGET_UPDATE);
		intent.setAction(Common.ACTION_UPDATE);
		sendBroadcast(intent);

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
		EventBus.getDefault().unregister(this);
		Common.setLlIncludeDepIn(false);
		Common.stateFilter(Common.getTempTimePos(), viewMode);
		CurrentTime.setTitleMonth(CurrentTime.getNow().getMonthOfYear());
		indexForTitle = 0;
		adapterFlag = false;
		clearApplicationCache(getExternalCacheDir());
	}

	private void clearApplicationCache(File dir){
		if(dir==null)
			dir = getCacheDir();
		else;
		if(dir==null)
			return;
		else;
		File[] children = dir.listFiles();
		try{
			for(int i=0;i<children.length;i++)
				if(children[i].isDirectory())
					clearApplicationCache(children[i]);
				else children[i].delete();
		}
		catch(Exception e){}
	}
	private void screenshot() {
//캡처
		flSurface.buildDrawingCache();
		flSurface.setDrawingCacheEnabled(true);
		capture = flSurface.getDrawingCache();

		try {
			File path = new File(Environment.getExternalStorageDirectory().toString() + "/.TimeDAO/");

			if (!path.isDirectory()) {
				path.mkdirs();
			}

			FileOutputStream out = new FileOutputStream(
					Environment.getExternalStorageDirectory().toString() + "/.TimeDAO/timetable.jpg");
			capture.compress(Bitmap.CompressFormat.JPEG, 100, out);

		} catch (FileNotFoundException e) {
			Log.d("FileNotFoundException:", e.getMessage());
		}
	}
	private void changeSetting(){
		EventBus.getDefault().post(new ClearNormalEvent());
		rlArea.setVisibility(View.GONE);
		llTitle.setVisibility(View.GONE);
		tvTitle.setVisibility(View.VISIBLE);
		btPlus.setVisibility(View.GONE);
		llSpinner.setVisibility(View.GONE);
		flSurface.setVisibility(View.GONE);
		frame_container.setVisibility(View.VISIBLE);
		surfaceFlag = true;
		initSurfaceView.surfaceDestroyed(initSurfaceView.getHolder());
	}


	private void initUI(){
		backKeyName="";
		if (User.USER.isSubjectDownloadState()) db = new DatabaseHandler(this);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		viewMode = MyPreferences.USERINFO.getPref().getInt("viewMode",0);
		DrawMode.CURRENT.setMode(0);
		ibMenu = (ImageButton) findViewById(R.id.ibMenu);
		ibBack = (ImageButton) findViewById(R.id.ibBack);
		ibfindSchedule = (ImageButton)findViewById(R.id.ibfindSchedule);
		ibwriteSchedule = (ImageButton)findViewById(R.id.ibwriteSchedule);
		ibareaSchedule = (ImageButton)findViewById(R.id.ibareaSchedule);
		mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		llProfile = (LinearLayout) findViewById(R.id.llProfile);
		llNormal = (LinearLayout) findViewById(R.id.llNormal);
		llUniv = (LinearLayout) findViewById(R.id.llUniv);
		llIncludeDep = (LinearLayout) findViewById(R.id.llIncludeDep);
		llSpinner = (LinearLayout) findViewById(R.id.llSpinner);
		llTitle = (LinearLayout) findViewById(R.id.llTitle);
		rlArea = (RelativeLayout) findViewById(R.id.rlArea);
		etPlace = (EditText) findViewById(R.id.etPlace);
		etMemo = (EditText) findViewById(R.id.etMemo);
		tvShare = (TextView) findViewById(R.id.tvShare);
		tvAlarm = (TextView) findViewById(R.id.tvAlarm);
		tvRepeat = (TextView) findViewById(R.id.tvRepeat);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitleYear = (TextView) findViewById(R.id.tvTitleYear);
		btPlus = (Button) findViewById(R.id.btPlus);
		btNormal = (Button) findViewById(R.id.btNormal);
		btUniv = (Button) findViewById(R.id.btUniv);
		btShowDep = (Button) findViewById(R.id.btShowDep);
		btColor = (Button) findViewById(R.id.btColor);
		hlv = (HorizontalListView) findViewById(R.id.hlv);
		ivProfile = (RoundedCornerNetworkImageView) findViewById(R.id.ivProfile);
		ivProfile.setImageUrl(SAMPLE_IMAGE_URL, MyVolley.getImageLoader());
		switcher = (TextSwitcher) findViewById(R.id.switcher);
		/*ViewGroup.LayoutParams params = llDialog.getLayoutParams();
		DisplayMetrics dm = getResources().getDisplayMetrics();
		params.height = dm.heightPixels/3 - 10;
		llDialog.setLayoutParams(params);*/
		flSurface = (FrameLayout) findViewById(R.id.flSurface);
		frame_container = (FrameLayout) findViewById(R.id.frame_container);
		/*params = flSurface.getLayoutParams();
		params.height = dm.heightPixels * 7 / 8;
		flSurface.setLayoutParams(params);*/
		initSurfaceView = new InitSurfaceView(this,viewMode);
		flSurface.addView(initSurfaceView);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		spinner = (Spinner) findViewById(R.id.spinner);
		spinnerAdapter = ArrayAdapter
				.createFromResource(this, R.array.wdm,
						R.layout.wdm_spinner_item);
		spinnerAdapter
				.setDropDownViewResource(R.layout.wdm_spinner_dropdown_item);

		// Apply the adapter to the spinner
		spinner.setAdapter(spinnerAdapter);
		spinner.setSelection(viewMode);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				EventBus.getDefault().post(new ClearNormalEvent());
				llTitle.setVisibility(View.VISIBLE);
				initSurfaceView.surfaceDestroyed(initSurfaceView.getHolder());
				indexForTitle = 0;
				Common.stateFilter(Common.getTempTimePos(), viewMode);
				switch (position) {
					case 0: //주
						barText = CurrentTime.getTitleMonthWeek(MainActivity.this);
						switcher.setText("");
						switcher.setText(barText);
						tvTitleYear.setVisibility(View.VISIBLE);
						btUniv.setVisibility(View.VISIBLE);
						btUniv.setTextColor(getResources().getColor(
								R.color.gray));
						viewMode = 0;
						changeFragment(TimetableFragment.class, "");
						flSurface.setVisibility(View.VISIBLE);
						frame_container.setVisibility(View.GONE);
						initSurfaceView.setMode(viewMode);
						break;
					case 1: // 일
						barText = CurrentTime.getTitleMonthWeek(MainActivity.this);
						switcher.setText("");
						switcher.setText(barText);
						tvTitleYear.setVisibility(View.GONE);
						btUniv.setVisibility(View.INVISIBLE);
						DrawMode.CURRENT.setMode(0);
						llNormal.setVisibility(View.VISIBLE);
						llUniv.setVisibility(View.GONE);
						btNormal.setTextColor(getResources().getColor(
								android.R.color.white));
						btUniv.setVisibility(View.VISIBLE);
						btUniv.setTextColor(getResources().getColor(
								R.color.gray));
						flSurface.setVisibility(View.GONE);
						frame_container.setVisibility(View.VISIBLE);
						changeFragment(InitDayFragment.class, "일");
						break;
					case 2: // 월
						barText = CurrentTime.getTitleYearMonth(MainActivity.this);
						switcher.setText("");
						switcher.setText(barText);
						tvTitleYear.setVisibility(View.GONE);
						btUniv.setVisibility(View.INVISIBLE);
						DrawMode.CURRENT.setMode(0);
						llNormal.setVisibility(View.VISIBLE);
						llUniv.setVisibility(View.GONE);
						btNormal.setTextColor(getResources().getColor(
								android.R.color.white));
						viewMode = 2;
						changeFragment(TimetableFragment.class, "");
						flSurface.setVisibility(View.VISIBLE);
						frame_container.setVisibility(View.GONE);
						initSurfaceView.setMode(viewMode);
						break;
				}
				initSurfaceView.surfaceCreated(initSurfaceView.getHolder());
				surfaceFlag = false;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
	}
	private static final String SAMPLE_IMAGE_URL = "http://hernia.cafe24.com/android/test2.png";
	InitSurfaceView initSurfaceView;
	DrawerLayout mDrawerLayout;
	RoundedCornerNetworkImageView ivProfile;
	LinearLayout mLeftDrawer,llProfile, llNormal, llUniv, llIncludeDep,llTitle,llSpinner;
	ImageButton ibMenu, ibBack, ibfindSchedule, ibwriteSchedule, ibareaSchedule;
	TextView tvTitle,tvTitleYear,tvShare,tvAlarm,tvRepeat;
	EditText etPlace,etMemo;
	Button btPlus,btNormal, btUniv, btColor, btShowUniv,btEnter,btShowDep;
	FrameLayout flSurface, frame_container;
	RelativeLayout rlArea;
	Fragment mContent = null;
	BackPressCloseHandler backPressCloseHandler;
	String backKeyName,korName,engName,barText;
	AutoCompleteTextView actvSelectUniv;
	Boolean clickFlag1=false,actvDepFlag=false,selected=false,adapterFlag=false,surfaceFlag = false, colorFlag = false;;
	static int indexForTitle=0;
	int viewMode;
	private HorizontalListView hlv;
	DatabaseHandler db;
	Bitmap capture = null;
	static MainActivity singleton;
	Spinner spinner;
	ArrayAdapter<CharSequence> spinnerAdapter;
	HorizontalListAdapter hoAdapter;
	TextSwitcher switcher;
	public ImageButton getIbBack() {return ibBack;}
	public ImageButton getIbMenu() {return ibMenu;}
	public String getBarText() {
		return barText;
	}
	public InitSurfaceView getInitSurfaceView() {
		return initSurfaceView;
	}
	public static MainActivity getInstance() {
		return singleton;
	}
	public int getViewMode() {
		return viewMode;
	}


	public void onEventMainThread(BackKeyEvent e){
		backKeyName = e.getFragName();
	}
	public void onEventMainThread(ChangeFragEvent e){
		changeFragment(e.getCl(), e.getTitleName());
	}
}
