package com.daemin.enumclass;

import android.content.Context;
import android.content.SharedPreferences;

import com.daemin.common.AppController;

/**
 * Created by hernia on 2015-07-06.
 */
public enum MyPreferences {
    USERINFO(AppController.getInstance());

    SharedPreferences pref;

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
    public SharedPreferences getPref() {
        return pref;
    }

    SharedPreferences.Editor editor;

    MyPreferences(Context context){
        pref = context.getSharedPreferences("USERINFO", context.MODE_PRIVATE);
        editor = pref.edit();
    }

   /* // put 예시
    editor.putBoolean("check1", checkbox.isChecked());
    editor.commit();
    // get 예시
    Boolean chk1 = pref.getBoolean("check1", false);*/
}
