package com.daemin.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.daemin.area.AreaFragment;
import com.daemin.common.BackPressCloseHandler;
import com.daemin.common.Common;
import com.daemin.common.RoundedCornerNetworkImageView;
import com.daemin.community.CommunityFragment2;
import com.daemin.dialog.DialSchedule;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.TimePos;
import com.daemin.enumclass.User;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.event.ClearNormalEvent;
import com.daemin.event.FinishDialogEvent;
import com.daemin.event.SetBtPlusEvent;
import com.daemin.event.SetBtUnivEvent;
import com.daemin.friend.FriendFragment;
import com.daemin.setting.SettingFragment;
import com.daemin.timetable.InitSurfaceView;
import com.daemin.timetable.R;
import com.daemin.timetable.TimetableFragment;
import com.daemin.widget.WidgetUpdateService;

import java.io.File;

import de.greenrobot.event.EventBus;

public class MainActivity extends FragmentActivity {
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mLeftDrawer)) {
            mDrawerLayout.closeDrawer(mLeftDrawer);
        } else {
            backPressCloseHandler.onBackPressed(backKeyName);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        if (surfaceFlag) {
            initSurfaceView.surfaceCreated(initSurfaceView.getHolder());
            surfaceFlag = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialogFlag) {
            if (widget5_5) {
                Intent update = new Intent(this, WidgetUpdateService.class);
                update.putExtra("action", "update5_5");
                update.putExtra("viewMode", viewMode);
                this.startService(update);
            }
            if (widget4_4) {
                Intent update = new Intent(this, WidgetUpdateService.class);
                update.putExtra("action", "update4_4");
                update.putExtra("viewMode", viewMode);
                this.startService(update);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //appcontroller에서 앱 실행시 초기에 불러오게될 정보를 저장함
        Common.stateFilter(viewMode);
        clearApplicationCache(getExternalCacheDir());
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mContent != null)
            getSupportFragmentManager().putFragment(outState, "mContent",
                    mContent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewMode = User.INFO.getViewMode();
        singleton = this;
        EventBus.getDefault().register(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        setLayout();
        setTitle();
        //Log.i("phone", User.USER.getPhoneNum());
    }

    /*private void screenshot() {
        Log.i("widget", "capture start");
        Bitmap bm = initSurfaceView.getInitThread().captureImg();
        try {
            File path = new File(Environment.getExternalStorageDirectory().toString() + "/.TimeDAO/");
            if (!path.isDirectory()) {
                path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(
                    Environment.getExternalStorageDirectory().toString() + "/.TimeDAO/timetable.jpg");
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            bm.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
        Log.i("widget", "capture end");
        Intent intent = new Intent();
        intent.setAction(Common.ACTION_UPDATE);
        sendBroadcast(intent);
    }*/
    public void setTitle() {
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
        if (viewMode == 0) {
            Dates.NOW.setWeekData();
            switcher.setText(setMonthWeek());
            tvTitleYear.setText(Dates.NOW.year + getString(R.string.year));
        } else {
            Dates.NOW.setMonthData();
            switcher.setText(setYearMonth());
            tvTitleYear.setVisibility(View.GONE);
        }
    }
    public String setMonthWeek(){
        return " " + Dates.NOW.month + this.getString(R.string.month) + " "
                + Dates.NOW.weekOfMonth + this.getString((R.string.weekofmonth));
    }
    public String setYearMonth(){
        return " " + Dates.NOW.year + this.getString(R.string.year) + " "
                + Dates.NOW.month + this.getString((R.string.month));
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
                if (!title.equals("")) tvTitle.setText(title);

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
            if (!title.equals("")) tvTitle.setText(title);
        }
    }

    private void clearApplicationCache(File dir) {
        if (dir == null)
            dir = getCacheDir();
        else ;
        if (dir == null)
            return;
        else ;
        File[] children = dir.listFiles();
        try {
            for (int i = 0; i < children.length; i++)
                if (children[i].isDirectory())
                    clearApplicationCache(children[i]);
                else children[i].delete();
        } catch (Exception e) {
        }
    }

    private void changeSetting() {
        EventBus.getDefault().post(new ClearNormalEvent());
        llTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        btPlus.setVisibility(View.GONE);
        flSurface.setVisibility(View.GONE);
        frame_container.setVisibility(View.VISIBLE);
        surfaceFlag = true;
        initSurfaceView.surfaceDestroyed(initSurfaceView.getHolder());
    }

    public void btBackEvent() {
        switch (viewMode) {
            case 0:
                --dayIndex;
                if (dayIndex < 0) {
                    Dates.NOW.setBackWeekData(-dayIndex);
                    tvTitleYear.setText(Dates.NOW.year + getString(R.string.year));
                    switcher.setText(setMonthWeek());
                } else {
                    Dates.NOW.setPreWeekData(dayIndex);
                    tvTitleYear.setText(Dates.NOW.year + getString(R.string.year));
                    switcher.setText(setMonthWeek());
                }
                Common.fetchWeekData();
                break;
            case 1:
                --dayIndex;
                if (dayIndex < 0) {
                    Dates.NOW.setBackMonthData(-dayIndex);
                    switcher.setText(setYearMonth());
                } else {
                    Dates.NOW.setPreMonthData(dayIndex);
                    switcher.setText(setYearMonth());
                }
                break;
        }
    }

    public void btForwardEvent() {
        switch (viewMode) {
            case 0:
                ++dayIndex;
                if (dayIndex < 0) {
                    Dates.NOW.setBackWeekData(-dayIndex);
                    tvTitleYear.setText(Dates.NOW.year + getString(R.string.year));
                    switcher.setText(setMonthWeek());
                } else {
                    Dates.NOW.setPreWeekData(dayIndex);
                    tvTitleYear.setText(Dates.NOW.year + getString(R.string.year));
                    switcher.setText(setMonthWeek());
                }
                Common.fetchWeekData();
                break;
            case 1:
                ++dayIndex;
                if (dayIndex < 0) {
                    Dates.NOW.setBackMonthData(-dayIndex);
                    switcher.setText(setYearMonth());
                } else {
                    Dates.NOW.setPreMonthData(dayIndex);
                    switcher.setText(setYearMonth());
                }
                break;
        }
    }

    // 드로어 메뉴 버튼 클릭 리스너
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.ibMenu:
                EventBus.getDefault().post(new FinishDialogEvent());
                if (mDrawerLayout.isDrawerOpen(mLeftDrawer)) {
                    mDrawerLayout.closeDrawer(mLeftDrawer);
                } else {
                    mDrawerLayout.openDrawer(mLeftDrawer);
                }
                break;
            case R.id.btTimetable:
                EventBus.getDefault().post(new ClearNormalEvent());
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
            case R.id.btPlus:
                dialogFlag = false;
                Intent i = new Intent(MainActivity.this, DialSchedule.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btMode:
                EventBus.getDefault().post(new FinishDialogEvent());
                EventBus.getDefault().post(new ClearNormalEvent());
                llTitle.setVisibility(View.VISIBLE);
                initSurfaceView.surfaceDestroyed(initSurfaceView.getHolder());
                dayIndex = 0;
                Common.stateFilter(viewMode);
                if (btMode.isChecked()) {
                    viewMode = 1;
                    EventBus.getDefault().postSticky(new SetBtUnivEvent(false));
                    Dates.NOW.setMonthData();
                    switcher.setText("");
                    switcher.setText(setYearMonth());
                    tvTitleYear.setVisibility(View.GONE);
                    DrawMode.CURRENT.setMode(0);
                    changeFragment(TimetableFragment.class, "");
                    flSurface.setVisibility(View.VISIBLE);
                    frame_container.setVisibility(View.GONE);
                    initSurfaceView.setMode(viewMode);
                }else{
                    viewMode = 0;
                    Dates.NOW.setWeekData();
                    EventBus.getDefault().postSticky(new SetBtUnivEvent(true));
                    tvTitleYear.setVisibility(View.VISIBLE);
                    tvTitleYear.setText(Dates.NOW.year + getString(R.string.year));
                    switcher.setText("");
                    switcher.setText(setMonthWeek());
                    changeFragment(TimetableFragment.class, "");
                    flSurface.setVisibility(View.VISIBLE);
                    frame_container.setVisibility(View.GONE);
                    initSurfaceView.setMode(viewMode);
                }
                User.INFO.getEditor().putInt("viewMode", viewMode).commit();
                initSurfaceView.surfaceCreated(initSurfaceView.getHolder());
                surfaceFlag = false;
                break;
            case R.id.btBack:
                Common.stateFilter(viewMode);
                EventBus.getDefault().post(new ClearNormalEvent());
                btBackEvent();
                break;
            case R.id.btForward:
                Common.stateFilter(viewMode);
                EventBus.getDefault().post(new ClearNormalEvent());
                btForwardEvent();
                break;
            case R.id.left_drawer://드로어 뒤 터치를 막기위한 dummy event
                break;
        }

    }

    private void setLayout() {
        backKeyName = "";
        mContent = null;
        surfaceFlag = false;
        dialogFlag = true;
        DrawMode.CURRENT.setMode(0);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        widget5_5 = User.INFO.getWidget5_5();
        widget4_4 = User.INFO.getWidget4_4();
        dayIndex = 0;
        ibMenu = (ImageButton) findViewById(R.id.ibMenu);
        ibBack = (ImageButton) findViewById(R.id.ibBack);
        mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
        llTitle = (LinearLayout) findViewById(R.id.llTitle);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitleYear = (TextView) findViewById(R.id.tvTitleYear);
        btPlus = (Button) findViewById(R.id.btPlus);
        btMode = (ToggleButton) findViewById(R.id.btMode);
        ivProfile = (RoundedCornerNetworkImageView) findViewById(R.id.ivProfile);
        //ivProfile.setImageUrl(SAMPLE_IMAGE_URL, MyVolley.getImageLoader());
        switcher = (TextSwitcher) findViewById(R.id.switcher);
        flSurface = (FrameLayout) findViewById(R.id.flSurface);
        frame_container = (FrameLayout) findViewById(R.id.frame_container);
        initSurfaceView = new InitSurfaceView(this, viewMode);
        flSurface.addView(initSurfaceView);
        backPressCloseHandler = new BackPressCloseHandler(this);
        if(viewMode==1){
            btMode.setChecked(true);
            EventBus.getDefault().postSticky(new SetBtUnivEvent(false));
        }
    }
    private static final String SAMPLE_IMAGE_URL = "http://hernia.cafe24.com/android/test2.png";
    private InitSurfaceView initSurfaceView;
    private DrawerLayout mDrawerLayout;
    private RoundedCornerNetworkImageView ivProfile;
    private LinearLayout mLeftDrawer, llTitle;
    private ImageButton ibMenu, ibBack;
    private TextView tvTitle, tvTitleYear;
    private Button btPlus;
    private ToggleButton btMode;
    private FrameLayout flSurface, frame_container;
    private Fragment mContent;
    private BackPressCloseHandler backPressCloseHandler;
    private String backKeyName, korName, engName;
    private Boolean surfaceFlag, dialogFlag, widget5_5, widget4_4;
    private TextSwitcher switcher;
    private static MainActivity singleton;
    private int viewMode;
    private static int dayIndex;

    public ImageButton getIbBack() {
        return ibBack;
    }
    public ImageButton getIbMenu() {
        return ibMenu;
    }
    public static MainActivity getInstance() {
        return singleton;
    }

    public void onEventMainThread(SetBtPlusEvent e) {
        if (e.isSetVisable())
            btPlus.setVisibility(View.VISIBLE);
        else
            btPlus.setVisibility(View.GONE);
        dialogFlag = true;
        System.gc();
    }

    public void onEventMainThread(BackKeyEvent e) {
        backKeyName = e.getFragName();
    }

    public void onEventMainThread(ChangeFragEvent e) {
        changeFragment(e.getCl(), e.getTitleName());
    }
}
