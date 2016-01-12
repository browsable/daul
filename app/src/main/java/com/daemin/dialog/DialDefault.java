package com.daemin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daemin.common.CustomJSONObjectRequest;
import com.daemin.common.MyRequest;
import com.daemin.common.MyVolley;
import com.daemin.data.UpdateListData;
import com.daemin.enumclass.User;
import com.daemin.event.SetCreditEvent;
import com.daemin.event.SetShareEvent;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;
import com.navercorp.volleyextensions.request.Jackson2Request;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialDefault extends Dialog {
    public DialDefault(Context context, String title, String content,int callFuncIndex) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.context=context;
        this.title=title;
        this.content=content;
        this.callFuncIndex=callFuncIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_default);
        setCancelable(true);
        window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        layoutParams = window.getAttributes();
        dm = getContext().getResources().getDisplayMetrics();
        layoutParams.width = dm.widthPixels * 2 / 3;
        layoutParams.height = dm.heightPixels* 3/10;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        setLayout();
    }
    public void goToPlayMarket(){
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://market.android.com/details?id=com.daemin.timetable"));
        context.startActivity(intent);
    }
    private void setLayout() {
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        btDialSetting = (Button) findViewById(R.id.btDialSetting);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvUpdateList = (TextView) findViewById(R.id.tvUpdateList);
        sv = (ScrollView) findViewById(R.id.sv);
        if(callFuncIndex==0) getUpdateList();
        tvTitle.setText(title);
        tvContent.setText(content);
        btDialSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    switch (callFuncIndex){
                        case 0: //업데이트시 마켓이동
                            goToPlayMarket();
                            break;
                        case 1: //시간표 초기화
                            MyTimeRepo.clearMyTime(context);
                            break;
                        case 2: //과목시간표 초기화
                            User.INFO.getEditor().putString("creditSum", "0").commit();
                            MyTimeRepo.deleteWithTimetype(context,1);
                            break;
                    }
                    cancel();
                }
        });
        btDialCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
    private Button btDialCancel;
    private Button btDialSetting;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvUpdateList;
    private ScrollView sv;
    private String title, content;
    private int callFuncIndex;
    private Context context;
    private WindowManager.LayoutParams layoutParams;
    private DisplayMetrics dm;
    private Window window;

    public static final String GET_UPDATE_LIST = "http://timenuri.com/ajax/app/get_update_history?v="+User.INFO.appVer;
    public void getUpdateList() {
        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.GET, GET_UPDATE_LIST, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("Success")) {
                                JSONObject data = response.getJSONObject("data");
                                sv.setVisibility(View.VISIBLE);
                                tvUpdateList.setText(data.getString("update_history"));
                                layoutParams.height = layoutParams.WRAP_CONTENT;
                                window.setAttributes(layoutParams);
                                window.setGravity(Gravity.CENTER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MyVolley.getRequestQueue().add(rq);
    }
}
