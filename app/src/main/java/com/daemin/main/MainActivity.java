package com.daemin.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.daemin.area.AreaFragment;
import com.daemin.common.BackPressCloseHandler;
import com.daemin.common.Common;
import com.daemin.common.CurrentTime;
import com.daemin.common.MyVolley;
import com.daemin.common.RoundedCornerNetworkImageView;
import com.daemin.community.CommunityFragment2;
import com.daemin.dialog.DialSchedule;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.MyPreferences;
import com.daemin.enumclass.User;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.event.ClearNormalEvent;
import com.daemin.event.FinishDialogEvent;
import com.daemin.event.SetBtPlusEvent;
import com.daemin.event.SetBtUnivEvent;
import com.daemin.friend.FriendFragment;
import com.daemin.setting.SettingFragment;
import com.daemin.timetable.InitDayFragment;
import com.daemin.timetable.InitMonthThread;
import com.daemin.timetable.InitSurfaceView;
import com.daemin.timetable.InitWeekThread;
import com.daemin.timetable.R;
import com.daemin.timetable.TimetableFragment;

import java.io.File;

import de.greenrobot.event.EventBus;

public class MainActivity extends FragmentActivity{
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mLeftDrawer)) {
            mDrawerLayout.closeDrawer(mLeftDrawer);
            Toast.makeText(MainActivity.this, "open", Toast.LENGTH_SHORT).show();
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
        if(dialogFlag) {
            Intent intent = new Intent();
            intent.setAction(Common.ACTION_UPDATE);
            intent.putExtra("viewMode", viewMode);
            sendBroadcast(intent);
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
        editor.commit();
        Common.stateFilter(Common.getTempTimePos(), viewMode);
        CurrentTime.setTitleMonth(CurrentTime.getNow().getMonthOfYear());
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
        singleton = this;
        EventBus.getDefault().register(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        setLayout();
        setTitle();
        //Log.i("phone", User.USER.getPhoneNum());
        backPressCloseHandler = new BackPressCloseHandler(this);
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
        switcher.setText(CurrentTime.getTitleMonthWeek(this));
        if (viewMode == 2) {
            barText = CurrentTime.getTitleYearMonth(this);
            switcher.setText(barText);
            tvTitleYear.setVisibility(View.GONE);
        }
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
        llSpinner.setVisibility(View.GONE);
        flSurface.setVisibility(View.GONE);
        frame_container.setVisibility(View.VISIBLE);
        surfaceFlag = true;
        initSurfaceView.surfaceDestroyed(initSurfaceView.getHolder());
    }
    public void btBackEvent(){
        switch (viewMode) {
            case 0:
                InitWeekThread iw = (InitWeekThread) initSurfaceView.getInitThread();
                --dayIndex;
                if (dayIndex < 0) {
                    tvTitleYear.setText(CurrentTime.backTitleYear(-dayIndex) + getString(R.string.year));
                    barText = CurrentTime.backTitleMonthWeek(this, -dayIndex);
                    switcher.setText(barText);
                    iw.setCurrentTime(CurrentTime.getBackDateOfWeek(-dayIndex));
                } else {
                    tvTitleYear.setText(CurrentTime.preTitleYear(dayIndex) + getString(R.string.year));
                    barText = CurrentTime.preTitleMonthWeek(this, dayIndex);
                    switcher.setText(barText);
                    iw.setCurrentTime(CurrentTime.getPreDateOfWeek(dayIndex));
                }
                break;
            case 2:
                InitMonthThread im = (InitMonthThread) initSurfaceView.getInitThread();
                --dayIndex;
                if (dayIndex < 0) {
                    barText = CurrentTime.backTitleYearMonth(this, -dayIndex);
                    switcher.setText(barText);
                    im.setCurrentTime(CurrentTime.getBackDayOfLastMonth(-dayIndex),
                            CurrentTime.getBackDayOfWeekOfLastMonth(-dayIndex),
                            CurrentTime.getBackDayNumOfMonth(-dayIndex));
                } else {
                    barText = CurrentTime.preTitleYearMonth(this, dayIndex);
                    switcher.setText(barText);
                    im.setCurrentTime(CurrentTime.getPreDayOfLastMonth(dayIndex),
                            CurrentTime.getPreDayOfWeekOfLastMonth(dayIndex),
                            CurrentTime.getPreDayNumOfMonth(dayIndex));
                }
                break;
        }
    }
    public void btForwardEvent(){
        switch (viewMode) {
            case 0:
                InitWeekThread iw = (InitWeekThread) initSurfaceView.getInitThread();
                ++dayIndex;
                if (dayIndex < 0) {
                    tvTitleYear.setText(CurrentTime.backTitleYear(-dayIndex) + getString(R.string.year));
                    barText = CurrentTime.backTitleMonthWeek(this, -dayIndex);
                    switcher.setText(barText);
                    iw.setCurrentTime(CurrentTime.getBackDateOfWeek(-dayIndex));
                } else {
                    tvTitleYear.setText(CurrentTime.preTitleYear(dayIndex) + getString(R.string.year));
                    barText = CurrentTime.preTitleMonthWeek(this, dayIndex);
                    switcher.setText(barText);
                    iw.setCurrentTime(CurrentTime.getPreDateOfWeek(dayIndex));
                }
                break;
            case 2:
                InitMonthThread im = (InitMonthThread) initSurfaceView.getInitThread();
                ++dayIndex;
                if (dayIndex < 0) {
                    barText = CurrentTime.backTitleYearMonth(this, -dayIndex);
                    switcher.setText(barText);
                    im.setCurrentTime(CurrentTime.getBackDayOfLastMonth(-dayIndex),
                            CurrentTime.getBackDayOfWeekOfLastMonth(-dayIndex),
                            CurrentTime.getBackDayNumOfMonth(-dayIndex));
                } else {
                    barText = CurrentTime.preTitleYearMonth(this, dayIndex);
                    switcher.setText(barText);
                    im.setCurrentTime(CurrentTime.getPreDayOfLastMonth(dayIndex),
                            CurrentTime.getPreDayOfWeekOfLastMonth(dayIndex),
                            CurrentTime.getPreDayNumOfMonth(dayIndex));
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
                dialogFlag =false;
                 Intent i = new Intent(MainActivity.this, DialSchedule.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btBack:
                Common.stateFilter(Common.getTempTimePos(), viewMode);
                EventBus.getDefault().post(new ClearNormalEvent());
                btBackEvent();
                break;
            case R.id.btForward:
                Common.stateFilter(Common.getTempTimePos(), viewMode);
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
        surfaceFlag = false; dialogFlag=true;
        DrawMode.CURRENT.setMode(0);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        viewMode = MyPreferences.USERINFO.getPref().getInt("viewMode", 0);
        dayIndex = 0;
        ibMenu = (ImageButton) findViewById(R.id.ibMenu);
        ibBack = (ImageButton) findViewById(R.id.ibBack);
        mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
        llSpinner = (LinearLayout) findViewById(R.id.llSpinner);
        llTitle = (LinearLayout) findViewById(R.id.llTitle);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitleYear = (TextView) findViewById(R.id.tvTitleYear);
        btPlus = (Button) findViewById(R.id.btPlus);
        btBack = (Button) findViewById(R.id.btBack);
        btForward = (Button) findViewById(R.id.btForward);
        ivProfile = (RoundedCornerNetworkImageView) findViewById(R.id.ivProfile);
        ivProfile.setImageUrl(SAMPLE_IMAGE_URL, MyVolley.getImageLoader());
        switcher = (TextSwitcher) findViewById(R.id.switcher);
        flSurface = (FrameLayout) findViewById(R.id.flSurface);
        frame_container = (FrameLayout) findViewById(R.id.frame_container);
        initSurfaceView = new InitSurfaceView(this, viewMode);
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
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EventBus.getDefault().post(new FinishDialogEvent());
                return false;
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                EventBus.getDefault().post(new ClearNormalEvent());
                llTitle.setVisibility(View.VISIBLE);
                initSurfaceView.surfaceDestroyed(initSurfaceView.getHolder());
                dayIndex = 0;
                Common.stateFilter(Common.getTempTimePos(), viewMode);
                switch (position) {
                    case 0: //주
                        viewMode = 0;
                        SharedPreferences.Editor wEditor = MyPreferences.USERINFO.getEditor();
                        wEditor.putInt("viewMode", viewMode);
                        wEditor.commit();
                        EventBus.getDefault().postSticky(new SetBtUnivEvent(true));
                        barText = CurrentTime.getTitleMonthWeek(MainActivity.this);
                        switcher.setText("");
                        switcher.setText(barText);
                        tvTitleYear.setVisibility(View.VISIBLE);
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
                        DrawMode.CURRENT.setMode(0);
                        flSurface.setVisibility(View.GONE);
                        frame_container.setVisibility(View.VISIBLE);
                        changeFragment(InitDayFragment.class, "일");
                        break;
                    case 2: // 월
                        viewMode = 2;
                        SharedPreferences.Editor mEditor = MyPreferences.USERINFO.getEditor();
                        mEditor.putInt("viewMode", viewMode);
                        mEditor.commit();
                        EventBus.getDefault().postSticky(new SetBtUnivEvent(false));
                        barText = CurrentTime.getTitleYearMonth(MainActivity.this);
                        switcher.setText("");
                        switcher.setText(barText);
                        tvTitleYear.setVisibility(View.GONE);
                        DrawMode.CURRENT.setMode(0);
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
    private InitSurfaceView initSurfaceView;
    private DrawerLayout mDrawerLayout;
    private RoundedCornerNetworkImageView ivProfile;
    private LinearLayout mLeftDrawer, llTitle, llSpinner;
    private ImageButton ibMenu, ibBack;
    private TextView tvTitle, tvTitleYear;
    private Button btPlus,btBack,btForward;
    private FrameLayout flSurface, frame_container;
    private Fragment mContent;
    private BackPressCloseHandler backPressCloseHandler;
    private String backKeyName, korName, engName, barText;
    private Boolean surfaceFlag,dialogFlag;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;
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
    public void onEventMainThread(SetBtPlusEvent e) {
        if(e.isSetVisable())
            btPlus.setVisibility(View.VISIBLE);
        else
            btPlus.setVisibility(View.GONE);
        dialogFlag = true;
    }
    public void onEventMainThread(BackKeyEvent e) {
        backKeyName = e.getFragName();
    }
    public void onEventMainThread(ChangeFragEvent e) {
        changeFragment(e.getCl(), e.getTitleName());
    }

}
