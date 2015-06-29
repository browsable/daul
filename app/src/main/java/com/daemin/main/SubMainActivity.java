package com.daemin.main;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daemin.area.AreaFragment;
import com.daemin.community.CommunityFragment;
import com.daemin.enumclass.EnumDialog;
import com.daemin.friend.FriendFragment;
import com.daemin.setting.SettingFragment;
import com.daemin.timetable.InitSurfaceView;
import com.daemin.timetable.R;
import com.daemin.timetable.TimetableFragment;
import com.daemin.timetable.common.BackPressCloseHandler;
import com.daemin.timetable.common.CurrentTime;

import greendao.DaoSession;


public class SubMainActivity extends FragmentActivity implements OnClickListener {

	static final String TAG = "MainActivity";
	InitSurfaceView InitSurfaceView;
	static SubMainActivity singleton;
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
	Boolean surfaceFlag = false;
	public DaoSession daoSession;
	BackPressCloseHandler backPressCloseHandler;
	String BackKeyName="";

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
		setContentView(R.layout.activity_main2);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		EnumDialog.BOTTOMDIAL.setContext(this);


		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		ibMenu = (ImageButton) findViewById(R.id.ibMenu);
		ibBack = (ImageButton) findViewById(R.id.ibBack);
		btPlus = (Button) findViewById(R.id.btPlus);
		ibMenu.setOnClickListener(this);
		btPlus.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		rlBar = (RelativeLayout) findViewById(R.id.rlBar);
		frame_container = (FrameLayout) findViewById(R.id.frame_container);
		flSurface = (FrameLayout) findViewById(R.id.flSurface);
		InitSurfaceView = new InitSurfaceView(this);
		flSurface.addView(InitSurfaceView);
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

		tvTitle.setText(getString(R.string.app_name));

		/*Box box = new Box();
		box.setId(5L); // if box with id 5 already exists in DB, it will be edited instead of created
		box.setName("My box");
		box.setSlots(39);
		box.setDescription("This is my box. I can put in it anything I wish.");
		BoxRepository.insertOrUpdate(this, box);*/

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
		tvTitle.setText(title);

		switch (barcolor) {
		case R.color.maincolor:
			rlBar.setBackgroundResource(R.color.maincolor);
			break;
		case R.color.orange:
			rlBar.setBackgroundResource(R.color.orange);
			break;
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ibMenu:
				boolean drawerOpen = mDrawerLayout.isDrawerOpen(mLeftDrawer);
				if (drawerOpen)
					mDrawerLayout.closeDrawer(mLeftDrawer);
				else
					mDrawerLayout.openDrawer(mLeftDrawer);
				break;
			case R.id.btPlus:
					//if(Common.checkTableStateIsNothing){
						EnumDialog.BOTTOMDIAL.Show();
				break;
		}
	}
	// 드로어 메뉴 버튼 클릭 리스너
	public void mOnClick(View v) {
		switch (v.getId()) {

		case R.id.btTimetable:
			changeFragment(TimetableFragment.class, "시간표", R.color.maincolor);
			flSurface.setVisibility(View.VISIBLE);
			btPlus.setVisibility(View.VISIBLE);
			frame_container.setVisibility(View.GONE);
			if(surfaceFlag) {
				InitSurfaceView.surfaceCreated(InitSurfaceView.getHolder());
				surfaceFlag = false;
			}
			BackKeyName ="";
			break;
		case R.id.btFriend:
			changeFragment(FriendFragment.class, "친구시간표", R.color.orange);
			flSurface.setVisibility(View.GONE);
			btPlus.setVisibility(View.GONE);
			frame_container.setVisibility(View.VISIBLE);
			InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
			surfaceFlag = true;
			BackKeyName ="";
			break;
		case R.id.btArea:
			changeFragment(AreaFragment.class, "주변시간표", R.color.maincolor);
			flSurface.setVisibility(View.GONE);
			btPlus.setVisibility(View.GONE);
			frame_container.setVisibility(View.VISIBLE);
			InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
			surfaceFlag = true;
			BackKeyName ="";
			break;
		case R.id.btCommunity:
			changeFragment(CommunityFragment.class, "커뮤니티", R.color.orange);
			flSurface.setVisibility(View.GONE);
			btPlus.setVisibility(View.GONE);
			frame_container.setVisibility(View.VISIBLE);
			InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
			surfaceFlag = true;
			BackKeyName ="";
			break;
		case R.id.btSetting:
			changeFragment(SettingFragment.class, "설정", R.color.maincolor);
			flSurface.setVisibility(View.GONE);
			btPlus.setVisibility(View.GONE);
			frame_container.setVisibility(View.VISIBLE);
			InitSurfaceView.surfaceDestroyed(InitSurfaceView.getHolder());
			surfaceFlag = true;
			BackKeyName ="";
			break;

		}
	}

	public void onBackPressed() {
		//super.onBackPressed();
		backPressCloseHandler.onBackPressed(BackKeyName);
	}

}
