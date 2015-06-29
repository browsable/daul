package com.daemin.community.github;

/**
 * Created by hernia on 2015-06-20.
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.daemin.timetable.R;
import com.daemin.timetable.common.MyVolley;
import com.navercorp.volleyextensions.request.Jackson2Request;
import com.navercorp.volleyextensions.request.JacksonRequest;

import java.util.HashMap;
import java.util.Map;

public class GithubActivity extends Activity {
    private static final String GET_PERSON_URL = "http://heeguchi.cafe24.com/app/getArticleList";
    private RequestQueue requestQueue;
    private TextView personInfoTextForJackson2;
    private TextView personInfoTextForJackson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = MyVolley.getRequestQueue();
        setContentView(R.layout.activity_github);
        personInfoTextForJackson = (TextView) findViewById(R.id.personInfoTextForJackson);
        personInfoTextForJackson2 = (TextView) findViewById(R.id.personInfoTextForJackson2);

     
        Jackson2Request<FreeBoard> jackson2Request = new Jackson2Request<FreeBoard>(
                Request.Method.POST,GET_PERSON_URL,  FreeBoard.class,
                new Listener<FreeBoard>() {
                    @Override
                    public void onResponse(FreeBoard response) {
                        String s = response.toString();
                        for(FreeBoard.Data d : response.getData()){
                            s += d.toString();
                        }
                        personInfoTextForJackson2.setText(s);
                    }
                }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(GithubActivity.class.getSimpleName(), ""
                        + error.getMessage());
            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("table","freeboard");
                map.put("page","1");
                return map;
            }

            /*@Override
            public byte[] getBody() throws AuthFailureError {
                return "{\"table\":\"freeboard\", \"page\": \"1\"}".getBytes();
                //return postFreeboard.getBytes();
            }*/
        };
          requestQueue.add(jackson2Request);

            //loadPersonInfo();
        }

    private void loadPersonInfo() {
        JacksonRequest<Person> jacksonRequest = new JacksonRequest<Person>(
                GET_PERSON_URL, Person.class, new Listener<Person>() {

            @Override
            public void onResponse(Person person) {
                personInfoTextForJackson.setText(person.toString());
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(GithubActivity.class.getSimpleName(), ""
                        + volleyError.getMessage());
            }
        });

        Jackson2Request<Person> jackson2Request = new Jackson2Request<Person>(
                GET_PERSON_URL, Person.class, new Listener<Person>() {

            @Override
            public void onResponse(Person person) {
                personInfoTextForJackson2.setText(person.toString());
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(GithubActivity.class.getSimpleName(), ""
                        + volleyError.getMessage());
            }
        });
        requestQueue.add(jacksonRequest);
        requestQueue.add(jackson2Request);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}