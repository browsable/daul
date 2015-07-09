package com.daemin.main;

import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daemin.area.AreaFragment;
import com.daemin.common.BackPressCloseHandler;
import com.daemin.common.Common;
import com.daemin.common.CurrentTime;
import com.daemin.community.CommunityFragment2;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.EnumDialog;
import com.daemin.enumclass.MyPreferences;
import com.daemin.enumclass.User;
import com.daemin.friend.FriendFragment;
import com.daemin.setting.SettingFragment;
import com.daemin.timetable.InitSurfaceView;
import com.daemin.timetable.R;
import com.daemin.timetable.TimetableFragment;


public class SubMainActivity extends FragmentActivity {

	static final String TAG = "MainActivity";
	InitSurfaceView InitSurfaceView;
	DrawerLayout mDrawerLayout;
	LinearLayout mLeftDrawer;
	ImageButton ibMenu, ibBack;
	Button btPlus;
	TextView tvTitle;
	FrameLayout flSurface, frame_container;
	RelativeLayout rlBar;
	Fragment mContent = null;
	GradientDrawable gd;
	CurrentTime ct;
	Boolean surfaceFlag = false, colorFlag = false;;
	BackPressCloseHandler backPressCloseHandler;
	String BackKeyName="";
	static SubMainActivity singleton;
	public static SubMainActivity getInstance() {
		return singleton;
	}
	public ImageButton getIbBack() {
		return ibBack;
	}
	public ImageButton getIbMenu() { return ibMenu; }
	public void setBackKeyName(String backKeyName) {
		BackKeyName = backKeyName;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		singleton = this;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN );
		setContentView(R.layout.activity_main2);
		EnumDialog.BOTTOMDIAL.setContext(this);
		DrawMode.CURRENT.setMode(0);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		ibMenu = (ImageButton) findViewById(R.id.ibMenu);
		ibBack = (ImageButton) findViewById(R.id.ibBack);
		btPlus = (Button) findViewById(R.id.btPlus);


		tvTitle = (TextView) findViewById(R.id.tvTitle);
		rlBar = (RelativeLayout) findViewById(R.id.rlBar);
		frame_container = (FrameLayout) findViewById(R.id.frame_container);
		flSurface = (FrameLayout) findViewById(R.id.flSurface);
		InitSurfaceView = new InitSurfaceView(this);
		flSurface.addView(InitSurfaceView);
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

		tvTitle.setText(getString(R.string.app_name));

		//Log.i("phone", User.USER.getPhoneNum());
		backPressCloseHandler = new BackPressCloseHandler(this);
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
	public void mOnClick(View v) {
		switch (v.getId()) {
			case R.id.ibMenu:
				boolean drawerOpen = mDrawerLayout.isDrawerOpen(mLeftDrawer);
				if (drawerOpen)
					mDrawerLayout.closeDrawer(mLeftDrawer);
				else
					mDrawerLayout.openDrawer(mLeftDrawer);
				EnumDialog.BOTTOMDIAL.Cancel();
				break;
			case R.id.btTimetable:
				changeFragment(TimetableFragment.class, "시간표", R.color.maincolor);
				flSurface.setVisibility(View.VISIBLE);
				btPlus.setVisibility(View.VISIBLE);
				frame_container.setVisibility(View.GONE);
				if (surfaceFlag) {
					InitSurfaceView.surfaceCreated(InitSurfaceView.getHolder());
					surfaceFlag = false;
				}
				BackKeyName = "";
				break;
			case R.id.btFriend:
				changeFragment(FriendFragment.class, "친구시간표", R.color.orange);
				flSurface.setVisibility(View.GONE);
				btPlus.setVisibility(View.GONE);
				frame_container.setVisibility(View.VISIBLE);
				InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
				surfaceFlag = true;
				BackKeyName = "";
				break;
			case R.id.btArea:
				changeFragment(AreaFragment.class, "주변시간표", R.color.maincolor);
				flSurface.setVisibility(View.GONE);
				btPlus.setVisibility(View.GONE);
				frame_container.setVisibility(View.VISIBLE);
				InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
				surfaceFlag = true;
				BackKeyName = "";
				break;
			case R.id.btCommunity:
				changeFragment(CommunityFragment2.class, "커뮤니티", R.color.orange);
				flSurface.setVisibility(View.GONE);
				btPlus.setVisibility(View.GONE);
				frame_container.setVisibility(View.VISIBLE);
				InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
				surfaceFlag = true;
				BackKeyName = "";
				break;
			case R.id.btSetting:
				changeFragment(SettingFragment.class, "설정", R.color.maincolor);
				flSurface.setVisibility(View.GONE);
				btPlus.setVisibility(View.GONE);
				frame_container.setVisibility(View.VISIBLE);
				InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
				surfaceFlag = true;
				BackKeyName = "";
				break;
			case R.id.btPlus:
				EnumDialog.BOTTOMDIAL.Show();
				break;
		}
	}

	public void onBackPressed() {
		//super.onBackPressed();
		backPressCloseHandler.onBackPressed(BackKeyName);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//appcontroller에서 앱 실행시 초기에 불러오게될 정보를 저장함
		SharedPreferences.Editor editor = MyPreferences.USERINFO.getEditor();
		editor.putBoolean("GroupListDownloadState",User.USER.isGroupListDownloadState());
		editor.putBoolean("SubjectDownloadState",User.USER.isSubjectDownloadState());
		editor.commit();
		Common.setLlIncludeDepIn(false);
		EnumDialog.BOTTOMDIAL.setAdapterFlag(false);
	}
}
