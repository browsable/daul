package com.daemin.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.daemin.common.BasicFragment;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.timetable.R;
import com.daemin.main.MainActivity;

import org.greenrobot.eventbus.EventBus;

public class SettingOpenSrcFragment extends BasicFragment {
    public SettingOpenSrcFragment() {
        super(R.layout.fragment_setting_opensrc, "SettingOpenSrcFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);

        EventBus.getDefault().post(new BackKeyEvent("SettingOpenSrcFragment", new String[]{"ibBack"}, new String[]{"ibMenu"}));

        //ibBack = MainActivity.getInstance().getIbBack();
        ibBack = MainActivity.getInstance().getIbBack();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class, "설정"));
                EventBus.getDefault().post(new BackKeyEvent("", new String[]{"ibMenu"}, new String[]{"ibBack"}));
            }
        });
        //tvLocalVer = (TextView) root.findViewById(R.id.tvLocalVer);
        //tvServerVer = (TextView) root.findViewById(R.id.tvServerVer);
        //btUpdate = (TextView) root.findViewById(R.id.btUpdate);
        //tvLocalVer.setText("v" + User.INFO.appVer);
        /**if (User.INFO.appServerVer == null) getVersionFromServer(getActivity());
        else tvServerVer.setText("v" + User.INFO.appServerVer);
        if (User.INFO.appVer.equals(User.INFO.appServerVer)) {
            equalFlag = true;
            btUpdate.setText(getActivity().getResources().getString(R.string.setting_ver_equal));
        } else {
            if (tvServerVer.getText().toString().equals("")) {
                equalFlag = true;
                btUpdate.setText(getActivity().getResources().getString(R.string.network_error2));
            } else {
                equalFlag = false;
                btUpdate.setText(getActivity().getResources().getString(R.string.setting_ver_update));
            }
        }
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!equalFlag) {
                    DialDefault dd = new DialDefault(getActivity(),
                            getActivity().getResources().getString(R.string.update_title),
                            getActivity().getResources().getString(R.string.update_notice),
                            0);
                    dd.show();
                }
            }
        });**/
        return root;
    }

    ImageButton ibBack;
    //TextView btUpdate, tvLocalVer, tvServerVer;
    //Boolean equalFlag;

    private static String KEY_STATUS = "status";
    public static final String GET_VERSION = "http://timenuri.com/ajax/app/get_version";
/**
    public void getVersionFromServer(final Context context) {
        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.GET, GET_VERSION, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString(KEY_STATUS).equals("Success")) {
                                JSONObject data = response.getJSONObject("data");
                                User.INFO.appServerVer = data.getString("appversion");
                                tvServerVer.setText("v" + User.INFO.appServerVer);
                                if (User.INFO.appVer.equals(User.INFO.appServerVer)) {
                                    equalFlag = true;
                                    btUpdate.setText(getActivity().getResources().getString(R.string.setting_ver_equal));
                                } else {
                                    equalFlag = false;
                                    btUpdate.setText(getActivity().getResources().getString(R.string.setting_ver_update));
                                }
                            } else {
                                equalFlag = true;
                                btUpdate.setText(getActivity().getResources().getString(R.string.network_error2));
                                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                equalFlag = true;
                btUpdate.setText(getActivity().getResources().getString(R.string.network_error2));
                Toast.makeText(context, context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
        MyVolley.getRequestQueue().add(rq);
    }**/

}
