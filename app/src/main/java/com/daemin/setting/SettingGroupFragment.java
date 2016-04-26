package com.daemin.setting;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daemin.common.BasicFragment;
import com.daemin.common.Common;
import com.daemin.common.MyRequest;
import com.daemin.common.MyVolley;
import com.daemin.data.GroupListData;
import com.daemin.enumclass.User;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.event.PostGroupListEvent;
import com.daemin.main.MainActivity;
import com.daemin.timetable.R;
import com.daemin.working.MainActivity2;
import com.navercorp.volleyextensions.request.Jackson2Request;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class SettingGroupFragment extends BasicFragment {
    public SettingGroupFragment() {
        super(R.layout.fragment_setting_group, "SettingGroupFragment");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        EventBus.getDefault().post(new BackKeyEvent("SettingGroupFragment", new String[]{"ibBack"}, new String[]{"ibMenu"}));
        //ibBack = MainActivity.getInstance().getIbBack();
        ibBack = MainActivity2.getInstance().getIbBack();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class, "설정"));
                EventBus.getDefault().post(new BackKeyEvent("", new String[]{"ibMenu"}, new String[]{"ibBack"}));
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(actvUniv.getWindowToken(), 0);
            }
        });
        univList = new ArrayList<>();
        actvUniv = (AutoCompleteTextView) root.findViewById(R.id.actvUniv);
        btShowUniv = (ToggleButton) root.findViewById(R.id.btShowUniv);
        btEnter = (Button) root.findViewById(R.id.btEnter);
        llVer = (LinearLayout) root.findViewById(R.id.llVer);
        tvHyperText = (TextView) root.findViewById(R.id.tvHyperText);
        tvHyperText.setMovementMethod(LinkMovementMethod.getInstance());
        tvServerVer = (TextView) root.findViewById(R.id.tvServerVer);
        tvLocalVer = (TextView) root.findViewById(R.id.tvLocalVer);
        tvGroupName = (TextView) root.findViewById(R.id.tvGroupName);
        try {
            String[] tmp = User.INFO.getGroupTtVer().split("-");
            String tmpS = tmp[0] + getActivity().getResources().getString(R.string.year) + " "
                    + Integer.parseInt(tmp[1]) + getActivity().getResources().getString(R.string.semester) + " "
                    + User.INFO.getGroupDBVer();
            tvLocalVer.setText(tmpS);
        }
        catch (ArrayIndexOutOfBoundsException e){
            tvLocalVer.setText("");
            llVer.setVisibility(View.GONE);
        }catch(NullPointerException e){
            tvLocalVer.setText("");
            llVer.setVisibility(View.GONE);
        }
        try {
            String[] tmp2 = User.INFO.ttServerVer.split("-");
            String tmpS2 = tmp2[0] + getActivity().getResources().getString(R.string.year) + " "
                    + Integer.parseInt(tmp2[1]) + getActivity().getResources().getString(R.string.semester) + " "
                    + User.INFO.dbServerVer;
            tvServerVer.setText(tmpS2);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            tvServerVer.setText("");
        }
        catch(NullPointerException e){
            tvServerVer.setText("");
        }
        groupName = User.INFO.getGroupName();
        if(!groupName.equals(""))tvGroupName.setText(groupName);

        if(User.INFO.groupListData.size()==0) {
            getGroupList(getActivity());
        }
        else{
            univList.clear();
            for (GroupListData.Data d : User.INFO.groupListData) {
                univList.add(d.getKo() + "/" + d.getTt_version() + "/" + d.getDb_version());
            }
            univAdapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_univ, univList);
            SettingACTV(actvUniv, univAdapter);
        }

        actvUniv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                try {
                    String[] tmp = actvUniv.getText().toString().split("/");
                    User.INFO.groupPK = position+1;
                    groupName = tmp[0];
                    ttVersion =tmp[1];
                    User.INFO.ttServerVer = ttVersion;
                    User.INFO.dbServerVer = tmp[2];
                } catch (ArrayIndexOutOfBoundsException e) { //등록대기중 인 경우
                    e.printStackTrace();
                    User.INFO.ttServerVer ="";
                    User.INFO.dbServerVer = "";
                }
                // 열려있는 키패드 닫기
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(actvUniv.getWindowToken(), 0);
                btShowUniv.setVisibility(View.GONE);
                btEnter.setVisibility(View.VISIBLE);
            }
        });
        actvUniv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                btEnter.setVisibility(View.GONE);
                btShowUniv.setVisibility(View.VISIBLE);
            }
        });
        btShowUniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Common.isOnline())
                    Toast.makeText(getActivity(), getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                else {
                    if (User.INFO.groupListData.size() == 0) getGroupList(getActivity());
                }
                if (btShowUniv.isChecked()) actvUniv.showDropDown();
                else actvUniv.dismissDropDown();
            }
        });
        btEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btShowUniv.setVisibility(View.VISIBLE);
                btEnter.setVisibility(View.GONE);
                actvUniv.setText("");
                if (User.INFO.getGroupName().equals(groupName)) { //처음 대학을 그대로 선택시
                    if (!User.INFO.getGroupDBVer().equals(User.INFO.dbServerVer)) {
                        Toast.makeText(getActivity(), getString(R.string.univ_dbupdate), Toast.LENGTH_SHORT).show();
                        new DownloadFileFromURL().execute(groupName);
                    }
                } else {
                    if (ttVersion.equals(getResources().getString(R.string.wait1))) {
                        Toast.makeText(getActivity(), getString(R.string.wait2), Toast.LENGTH_SHORT).show();
                    } else {
                        new DownloadFileFromURL().execute(groupName);
                        Toast.makeText(getActivity(), getString(R.string.setting_group_modify), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return root;
    }
    public AutoCompleteTextView SettingACTV(AutoCompleteTextView actv, ArrayAdapter<String> adapter) {
        actv.requestFocus();
        actv.setThreshold(1);// will start working from first character
        actv.setAdapter(adapter);// setting the adapter data into the
        actv.setTextColor(Color.DKGRAY);
        actv.setDropDownVerticalOffset(User.INFO.intervalSize);
        return actv;
    }

    public void getGroupList(final Context context) {
        Jackson2Request<GroupListData> jackson2Request = new Jackson2Request<>(
                Request.Method.POST, "http://timenuri.com/ajax/app/get_univ_list", GroupListData.class,
                new Response.Listener<GroupListData>() {
                    @Override
                    public void onResponse(GroupListData response) {
                        univList.clear();
                        for (GroupListData.Data d :  response.getData()) {
                            univList.add(d.getKo() + "/" + d.getTt_version() + "/" +d.getDb_version());
                        }
                        univAdapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_univ, univList);
                        SettingACTV(actvUniv, univAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
        MyVolley.getRequestQueue().add(jackson2Request);
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        Boolean downComplete;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            downComplete = false;
            pDialog = new ProgressDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pDialog.setMessage(getString(R.string.downloding));
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... univName) {
            int count;
            try {
                URL url = new URL("http://timenuri.com/ajax/app/get_univ_db?school=" + URLEncoder.encode(univName[0]));
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                createFolder();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream("/sdcard/.TimeDAO/subject.sqlite");
                ///data/data/com.daemin.timetable/databases
                byte data[] = new byte[2048];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
                if (total == lenghtOfFile) downComplete = true;
            } catch (Exception e) {
                e.printStackTrace();
                downComplete = false;
            }
            return null;
        }
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }
        @Override
        protected void onPostExecute(String param) {
            if (downComplete) {
                llVer.setVisibility(View.VISIBLE);
                pDialog.dismiss();
                String[] tmp = User.INFO.ttServerVer.split("-");
                String tmpS = tmp[0]+getActivity().getResources().getString(R.string.year)+" "
                        +Integer.parseInt(tmp[1]) +getActivity().getResources().getString(R.string.semester)+" "
                        +User.INFO.dbServerVer;
                tvLocalVer.setText(tmpS);
                tvServerVer.setText(tmpS);
                tvGroupName.setText(groupName);
                User.INFO.getEditor().putString("groupName", groupName).commit();
                if(User.INFO.dbServerVer==null){
                    User.INFO.dbServerVer=User.INFO.getGroupDBVer();
                }
                User.INFO.getEditor().putString("groupTtVer", User.INFO.ttServerVer).commit();
                User.INFO.getEditor().putString("groupDBVer", User.INFO.dbServerVer).commit();
                User.INFO.getEditor().putInt("groupPK", User.INFO.groupPK).commit();
            } else {//다운로드 실패
                User.INFO.ttServerVer=User.INFO.getGroupTtVer();
                User.INFO.dbServerVer=User.INFO.getGroupDBVer();
                Toast.makeText(getActivity(), getString(R.string.down_error), Toast.LENGTH_SHORT).show();
                btShowUniv.setVisibility(View.VISIBLE);
                btEnter.setVisibility(View.GONE);
            }
        }
    }
    public static void createFolder() {
        try {
            //check sdcard mount state
            String str = Environment.getExternalStorageState();
            if (str.equals(Environment.MEDIA_MOUNTED)) {
                String mTargetDirPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/.TimeDAO/";

                File file = new File(mTargetDirPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
            } else {
            }
        } catch (Exception e) {
        }
    }
    ImageButton ibBack;
    ToggleButton btShowUniv;
    Button btEnter;
    TextView tvHyperText,tvGroupName,tvLocalVer,tvServerVer;
    LinearLayout llVer;
    private ArrayAdapter<String> univAdapter;
    private ArrayList<String> univList;
    private AutoCompleteTextView actvUniv;
    private String groupName,ttVersion; //ttVersion: 학기버전
    private ProgressDialog pDialog;
}
