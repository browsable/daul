package com.daemin.common;


import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daemin.community.github.GithubActivity;
import com.daemin.data.GroupListData;
import com.daemin.enumclass.User;
import com.daemin.repository.GroupListFromServerRepository;
import com.daemin.timetable.R;
import com.navercorp.volleyextensions.request.Jackson2Request;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import timedao_group.GroupListFromServer;

/**
 * Created by hernia on 2015-07-02.
 */
public class MyRequest {
    public static RequestQueue requestQueue = MyVolley.getRequestQueue();
    public static ArrayList<String> groupListData = new ArrayList<>();
    public static Context context = AppController.getInstance();
    public static final String GET_GROUPLIST_URL = "http://timedao.heeguchi.me/app/getGroupList";
    public static final String UNIVDB_URL = "http://hernia.cafe24.com/android/db/";
    //public static GroupListFromServer groupListFromServer = new GroupListFromServer();
    public static void getGroupList(){
        Jackson2Request<GroupListData> jackson2Request = new Jackson2Request<>(
                Request.Method.POST,GET_GROUPLIST_URL, GroupListData.class,
                new Response.Listener<GroupListData>() {
                    @Override
                    public void onResponse(GroupListData response) {
                        /*for(GroupListData.Data d : response.getData()){
                                groupListData.add(d.getName());
                        }*/
                        GroupListFromServerRepository.clearGroupListFromServer(context);
                        for(GroupListData.Data d : response.getData()){
                            GroupListFromServer groupListFromServer = new GroupListFromServer();
                            groupListFromServer.setName(d.getName());
                            GroupListFromServerRepository.insertOrUpdate(context, groupListFromServer);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(GithubActivity.class.getSimpleName(), ""
                        + error.getMessage());
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
        requestQueue.add(jackson2Request);
    }


    public static void DownloadSqlite() {
        // 비동기로 실행될 코드
        Callable<Void> callable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                int count;
                try {
                    URL url = new URL(UNIVDB_URL+ User.USER.getUnivName()+"/subject.sqlite");
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

        };

        new AsyncExecutor<Void>()
                .setCallable(callable)
                .setCallback(callback)
                .execute();
    }

    // 비동기로 실행된 결과를 받아 처리하는 코드
    private static AsyncCallback<Void> callback = new AsyncCallback<Void>() {
        @Override
        public void onResult(Void result) {
            //Toast.makeText(getActivity(), getActivity().getString(R.string.dataloading_error), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void exceptionOccured(Exception e) {
            Toast.makeText(context, context.getString(R.string.dataloading_error), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void cancelled() {
            Toast.makeText(context, context.getString(R.string.dataloading_error), Toast.LENGTH_SHORT).show();
        }
    };
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
}
