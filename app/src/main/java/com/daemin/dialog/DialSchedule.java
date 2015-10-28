package com.daemin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemin.common.HorizontalListView;
import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialSchedule extends Dialog implements View.OnClickListener{
    Context context;
    public DialSchedule(Context context) {
        super(context, R.style.DialogSlideAnim);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_schedule);
        setCancelable(true);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = layoutParams.MATCH_PARENT;
        layoutParams.height = layoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.BOTTOM);
        setLayout();
    }

    private void setLayout() {
        btNormal=(Button)findViewById(R.id.btNormal);
        btUniv=(Button)findViewById(R.id.btUniv);
        btColor=(Button)findViewById(R.id.btColor);
        btAddSchedule=(Button)findViewById(R.id.btAddSchedule);
        btCancel=(Button)findViewById(R.id.btCancel);
        btCommunity=(Button)findViewById(R.id.btCommunity);
        btInvite=(Button)findViewById(R.id.btInvite);
        btRemove=(Button)findViewById(R.id.btRemove);
        btShowDep=(Button)findViewById(R.id.btShowDep);
        llColor=(LinearLayout)findViewById(R.id.llColor);
        llNormal=(LinearLayout)findViewById(R.id.llNormal);
        llButtonArea=(LinearLayout)findViewById(R.id.llButtonArea);
        llUniv=(LinearLayout)findViewById(R.id.llUniv);
        btNew1=(LinearLayout)findViewById(R.id.btNew1);
        btNew2=(LinearLayout)findViewById(R.id.btNew2);
        btPlace=(LinearLayout)findViewById(R.id.btPlace);
        btShare=(LinearLayout)findViewById(R.id.btShare);
        btAlarm=(LinearLayout)findViewById(R.id.btAlarm);
        btRepeat=(LinearLayout)findViewById(R.id.btRepeat);
        llIncludeDep=(LinearLayout)findViewById(R.id.llIncludeDep);
        tvShare=(TextView)findViewById(R.id.tvShare);
        tvAlarm=(TextView)findViewById(R.id.tvAlarm);
        tvRepeat=(TextView)findViewById(R.id.tvRepeat);
        etName=(EditText)findViewById(R.id.etName);
        etPlace=(EditText)findViewById(R.id.etPlace);
        etMemo=(EditText)findViewById(R.id.etMemo);
        lvTime=(HorizontalListView)findViewById(R.id.lvTime);
        btNormal.setOnClickListener(this);
        btUniv.setOnClickListener(this);
        btColor.setOnClickListener(this);
        btAddSchedule.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btCommunity.setOnClickListener(this);
        btInvite.setOnClickListener(this);
        btRemove.setOnClickListener(this);
        btShowDep.setOnClickListener(this);
        btNew1.setOnClickListener(this);
        btNew2.setOnClickListener(this);
        btPlace.setOnClickListener(this);
        btShare.setOnClickListener(this);
        btAlarm.setOnClickListener(this);
        btRepeat.setOnClickListener(this);
    }
    private Button btNormal,btUniv,btColor,btAddSchedule,btCancel,btCommunity,btInvite,btRemove,btShowDep;
    private LinearLayout llColor,llNormal,llButtonArea,llUniv,llIncludeDep,btNew1,btNew2,btPlace,btShare,btAlarm,btRepeat;
    TextView tvShare,tvAlarm,tvRepeat;
    EditText etName,etPlace, etMemo;
    HorizontalListView lvTime;
    int orgX, orgY;
    int offsetX, offsetY;
    boolean colorFlag = false;
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btNormal:
                break;
            case R.id.btUniv:
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
            case R.id.btAddSchedule:
                cancel();
                break;
            case R.id.btCancel:
                cancel();
                break;
            case R.id.btCommunity:
                break;
            case R.id.btInvite:
                break;
            case R.id.btRemove:
                break;
            case R.id.btShowDep:
                break;
            case R.id.btNew1:
                break;
            case R.id.btNew2:
                break;
            case R.id.btPlace:
                break;
            case R.id.btShare:
                break;
            case R.id.btAlarm:
                break;
            case R.id.btRepeat:
                break;
        }
    }
}
