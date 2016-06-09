package com.daemin.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.daemin.area.AreaFragment;
import com.daemin.common.BackPressCloseHandler;
import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.common.DatabaseHandler;
import com.daemin.common.MyRequest;
import com.daemin.common.RoundedCornerNetworkImageView;
import com.daemin.community.CommunityFragment2;
import com.daemin.dialog.DialDefault;
import com.daemin.dialog.DialSchedule;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.User;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.event.ClearNormalEvent;
import com.daemin.event.CreateDialEvent;
import com.daemin.event.FinishDialogEvent;
import com.daemin.event.RefreshDayListEvent;
import com.daemin.event.SetBtPlusEvent;
import com.daemin.event.SetBtUnivEvent;
import com.daemin.event.SetCreditEvent;
import com.daemin.friend.FriendFragment;
import com.daemin.setting.SettingFragment;
import com.daemin.timetable.InitSurfaceView;
import com.daemin.timetable.R;
import com.daemin.timetable.TimetableFragment;
import com.daemin.widget.WidgetUpdateService;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;


public class MainActivity extends FragmentActivity {
    @Override
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialogFlag) {
            if(widget5_5){
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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        mFirebaseAnalytics.setUserId(User.INFO.getUserPK());
        Bundle params = new Bundle();
        params.putString("user name", User.INFO.getUserPK());
        params.putString("connect time ", Dates.NOW.getDatefromMillis(Dates.NOW.getNowMillis()));
        mFirebaseAnalytics.logEvent("user action",params);
        singleton = this;
        viewMode = User.INFO.getViewMode();
        String groupName = User.INFO.getGroupName();
        if(groupName.equals(""))MyRequest.getGroupList(this);
        else MyRequest.getDBVerWithMyGroup(this, User.INFO.groupPK);
        checkLocationPermission();
        EventBus.getDefault().register(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        setLayout();
        setTitle();
        MyRequest.getVersionFromServer(this);
    }
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
            Dates.NOW.setWeekData(0);
            switcher.setText(setMonthWeek());
            tvTitleYear.setText(Dates.NOW.year + getString(R.string.year));
        } else {
            Dates.NOW.setMonthData(0);
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
            ft.commitAllowingStateLoss();
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
        initSurfaceView.setVisibility(View.GONE);
    }

    public void btBackEvent() {
        switch (viewMode) {
            case 0:
                Dates.NOW.setWeekData(--dayIndex);
                tvTitleYear.setText(Dates.NOW.year + getString(R.string.year));
                switcher.setText(setMonthWeek());
                Common.fetchWeekData();
                initSurfaceView.getInitThread().setDate();
                break;
            case 1:
                Dates.NOW.setMonthData(--dayIndex);
                switcher.setText(setYearMonth());
                Common.fetchMonthData();
                break;
        }
    }

    public void btForwardEvent() {
        switch (viewMode) {
            case 0:
                Dates.NOW.setWeekData(++dayIndex);
                tvTitleYear.setText(Dates.NOW.year + getString(R.string.year));
                switcher.setText(setMonthWeek());
                Common.fetchWeekData();
                initSurfaceView.getInitThread().setDate();
                break;
            case 1:
                Dates.NOW.setMonthData(++dayIndex);
                switcher.setText(setYearMonth());
                Common.fetchMonthData();
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
                btMode.setVisibility(View.VISIBLE);
                backKeyName = "";
                if(listMode) {
                    changeSetting();
                    changeFragment(TimetableFragment.class, Dates.NOW.month+getString(R.string.month)+" "+Dates.NOW.getDayOfMonth()+getString(R.string.day) +" " +Convert.XthToDayOfWeek(2*Dates.NOW.getDayOfWeek()+1));
                    btMode.setText(R.string.day);
                    EventBus.getDefault().post(new RefreshDayListEvent());
                }else{
                    EventBus.getDefault().post(new ClearNormalEvent());
                    llTitle.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.GONE);
                    btPlus.setVisibility(View.VISIBLE);
                    changeFragment(TimetableFragment.class, "");
                    flSurface.setVisibility(View.VISIBLE);
                    frame_container.setVisibility(View.GONE);
                    initSurfaceView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btFriend:
                checkContactsPermission();
                break;
            case R.id.btArea:
                btMode.setVisibility(View.GONE);
                changeSetting();
                changeFragment(AreaFragment.class, "주변시간표");
                break;
            case R.id.btCommunity:
                btMode.setVisibility(View.GONE);
                changeSetting();
                changeFragment(CommunityFragment2.class, "커뮤니티");
                break;
            case R.id.btSetting:
                btMode.setVisibility(View.GONE);
                changeSetting();
                changeFragment(SettingFragment.class, "설정");
                break;
            case R.id.btPlus:
                checkStoragePermission();
                break;
            case R.id.btMode:
                EventBus.getDefault().post(new FinishDialogEvent());
                EventBus.getDefault().post(new ClearNormalEvent());
                initSurfaceView.surfaceDestroyed(initSurfaceView.getHolder());
                dayIndex = 0;
                Common.stateFilter(viewMode);
                PopupMenu popup = new PopupMenu(MainActivity.this, btMode);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_viewmode, popup.getMenu());
                popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
                popup.show();
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
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            backKeyName = "";
            switch (menuItem.getItemId()) {
                case R.id.week:
                    listMode = false;
                    llTitle.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.GONE);
                    btPlus.setVisibility(View.VISIBLE);
                    flSurface.setVisibility(View.VISIBLE);
                    initSurfaceView.setVisibility(View.VISIBLE);
                    frame_container.setVisibility(View.GONE);
                    btMode.setText(R.string.week);
                    Dates.NOW.setWeekData(0);
                    EventBus.getDefault().postSticky(new SetBtUnivEvent(true));
                    tvTitleYear.setVisibility(View.VISIBLE);
                    tvTitleYear.setText(Dates.NOW.year + getString(R.string.year));
                    switcher.setText("");
                    switcher.setText(setMonthWeek());
                    viewMode = 0;
                    initSurfaceView.setMode(viewMode);
                    changeFragment(TimetableFragment.class, "");
                    flSurface.setVisibility(View.VISIBLE);
                    frame_container.setVisibility(View.GONE);
                    if (!initSurfaceView.isDestroyed()) {
                        initSurfaceView.surfaceDestroyed(initSurfaceView.getHolder());
                    }
                    initSurfaceView.surfaceCreated(initSurfaceView.getHolder());
                    User.INFO.getEditor().putInt("viewMode", viewMode).commit();
                    return true;
                case R.id.month:
                    listMode = false;
                    llTitle.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.GONE);
                    btPlus.setVisibility(View.VISIBLE);
                    flSurface.setVisibility(View.VISIBLE);
                    initSurfaceView.setVisibility(View.VISIBLE);
                    frame_container.setVisibility(View.GONE);
                    btMode.setText(R.string.month);
                    EventBus.getDefault().postSticky(new SetBtUnivEvent(false));
                    Dates.NOW.setMonthData(0);
                    switcher.setText("");
                    switcher.setText(setYearMonth());
                    tvTitleYear.setVisibility(View.GONE);
                    DrawMode.CURRENT.setMode(0);
                    viewMode = 1;
                    initSurfaceView.setMode(viewMode);
                    changeFragment(TimetableFragment.class, "");
                    flSurface.setVisibility(View.VISIBLE);
                    frame_container.setVisibility(View.GONE);
                    if (!initSurfaceView.isDestroyed()) {
                        initSurfaceView.surfaceDestroyed(initSurfaceView.getHolder());
                    }
                    initSurfaceView.surfaceCreated(initSurfaceView.getHolder());
                    User.INFO.getEditor().putInt("viewMode", viewMode).commit();
                    return true;
                case R.id.day:
                    listMode = true;
                    changeSetting();
                    changeFragment(TimetableFragment.class, Dates.NOW.month+getString(R.string.month)+" "+Dates.NOW.getDayOfMonth()+getString(R.string.day) +" " +Convert.XthToDayOfWeek(2*Dates.NOW.getDayOfWeek()+1));
                    btMode.setText(R.string.day);
                    EventBus.getDefault().post(new RefreshDayListEvent());
                    return true;
                default:
            }
            return false;
        }
    }

    private void setLayout() {
        backKeyName = "";
        mContent = null;
        dialogFlag = true;
        listMode = false;
        DrawMode.CURRENT.setMode(0);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        mDrawerLayout.setScrimColor(getResources().getColor(R.color.middlegray));
        widget5_5 = User.INFO.getWidget5_5();
        widget4_4 = User.INFO.getWidget4_4();
        dayIndex = 0;
        ibBack = (ImageButton) findViewById(R.id.ibBack);
        mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
        llTitle = (LinearLayout) findViewById(R.id.llTitle);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitleYear = (TextView) findViewById(R.id.tvTitleYear);
        btPlus = (Button) findViewById(R.id.btPlus);
        btMode = (Button) findViewById(R.id.btMode);
        ivProfile = (RoundedCornerNetworkImageView) findViewById(R.id.ivProfile);
        //ivProfile.setImageUrl(SAMPLE_IMAGE_URL, MyVolley.getImageLoader());
        switcher = (TextSwitcher) findViewById(R.id.switcher);
        flSurface = (FrameLayout) findViewById(R.id.flSurface);
        frame_container = (FrameLayout) findViewById(R.id.frame_container);
        initSurfaceView = new InitSurfaceView(this, viewMode);
        flSurface.addView(initSurfaceView);
        backPressCloseHandler = new BackPressCloseHandler(this);
        if(viewMode==0){
            btMode.setText(R.string.week);
            EventBus.getDefault().postSticky(new SetBtUnivEvent(true));
        }else{
            btMode.setText(R.string.month);
            EventBus.getDefault().postSticky(new SetBtUnivEvent(false));
        }
    }
    private static final String SAMPLE_IMAGE_URL = "http://hernia.cafe24.com/android/test2.png";
    private InitSurfaceView initSurfaceView;
    private DrawerLayout mDrawerLayout;
    private RoundedCornerNetworkImageView ivProfile;
    private LinearLayout mLeftDrawer, llTitle;
    private ImageButton ibBack;
    private TextView tvTitle, tvTitleYear;
    private Button btPlus, btMode;
    private FrameLayout flSurface, frame_container;
    private Fragment mContent;
    private BackPressCloseHandler backPressCloseHandler;
    private String backKeyName;
    private Boolean dialogFlag, widget5_5,widget4_4,listMode;
    private TextSwitcher switcher;
    private static MainActivity singleton;
    private int viewMode;
    private static int dayIndex;
    private FirebaseAnalytics mFirebaseAnalytics;

    public ImageButton getIbBack() {
        return ibBack;
    }
    public static MainActivity getInstance() {
        return singleton;
    }
    public InitSurfaceView getInitSurfaceView() {
        return initSurfaceView;
    }
    @Subscribe
    public void onEventMainThread(SetBtPlusEvent e) {
        if (e.isSetVisable())
            btPlus.setVisibility(View.VISIBLE);
        else
            btPlus.setVisibility(View.GONE);
        dialogFlag = true;
    }
    @Subscribe
    public void onEventMainThread(CreateDialEvent e) {
        if(e.isDialFlag()) dialogFlag=true;
        else dialogFlag = false;
    }
    @Subscribe
    public void onEventMainThread(BackKeyEvent e) {
        backKeyName = e.getFragName();
        if(e.getVisibleBt()!=null) {
            for (String bt : e.getVisibleBt()){
                int resId = getResources().getIdentifier(bt, "id", getPackageName());
                ImageButton ib = (ImageButton)findViewById(resId);
                ib.setVisibility(View.VISIBLE);
            }
            for (String bt : e.getGoneBt()){
                int resId = getResources().getIdentifier(bt, "id", getPackageName());
                ImageButton ib = (ImageButton)findViewById(resId);
                ib.setVisibility(View.GONE);
            }
        }
    }
    @Subscribe
    public void onEventMainThread(ChangeFragEvent e) {
        changeFragment(e.getCl(), e.getTitleName());
    }
    @Subscribe
    public void onEventMainThread(SetCreditEvent e) {
        DatabaseHandler db = new DatabaseHandler(this);
        Float credit = Float.parseFloat(db.getCredit(e.getSubtitle()));
        User.INFO.credit-=credit;
        if(User.INFO.credit<0f) User.INFO.credit=0f;
        User.INFO.getEditor().putFloat("credit",User.INFO.credit).commit();
    }
    /*public void GPSSetting(){
        GPSInfo gps = new GPSInfo(this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            User.INFO.setLatitude(gps.getLatitude());
            User.INFO.setLongitude(gps.getLongitude());
            gps.stopUsingGPS();
        } else {
            // GPS 를 사용할수 없으므로
			*//*Toast.makeText(
					this,
					"현재 GPS가 켜져있지 않습니다.",
					Toast.LENGTH_LONG).show();*//*
            gps.stopUsingGPS();
        }
    }*/
    public void btFriendEvent(){
        btMode.setVisibility(View.GONE);
        changeSetting();
        changeFragment(FriendFragment.class, "친구시간표");
    }
    public void btPlusEvent(){
        if(viewMode==1) EventBus.getDefault().postSticky(new SetBtUnivEvent(false));
        dialogFlag = false;
        Intent i = new Intent(MainActivity.this, DialSchedule.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        if(User.INFO.getExplain1()) {
            Intent in = new Intent(MainActivity.this, ExplainActivity.class);
            startActivity(in);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
    private final int REQUEST_LOCATION = 101;
    private void checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, getString(R.string.permission_location), Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
                // MY_PERMISSION_REQUEST_STORAGE is an
                // app-defined int constant
            }else {
                // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
                //GPSSetting();
            }

        }else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
            //GPSSetting();
        }
    }
    private final int REQUEST_CONTACTS = 102;
    private void checkContactsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, getString(R.string.permission_contacts), Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
                        REQUEST_CONTACTS);
            }else {
                btFriendEvent();
            }
        }else {
            btFriendEvent();
        }
    }
    private final int REQUEST_STORAGE = 103;
    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, getString(R.string.permission_storage), Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE);
            }else {
                btPlusEvent();
            }
        }else {
            btPlusEvent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            /*case REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){ //&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    GPSSetting();
                } else {
                    DialDefault dd = new DialDefault(this,
                            getString(R.string.permission_request),
                            getString(R.string.permission_location)+
                            getString(R.string.permission_deny),
                            4);
                    dd.show();
                }
                break;*/
            case REQUEST_CONTACTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){ //&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    btFriendEvent();
                } else {
                    DialDefault dd = new DialDefault(this,
                            getString(R.string.permission_request),
                            getString(R.string.permission_contacts)+
                                    getString(R.string.permission_deny),
                            5);
                    dd.show();
                }
                break;
            case REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){ //&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    btPlusEvent();
                } else {
                    DialDefault dd = new DialDefault(this,
                            getString(R.string.permission_request),
                            getString(R.string.permission_storage)+
                                    getString(R.string.permission_deny),
                            5);
                    dd.show();
                }
                break;
        }
    }
}
