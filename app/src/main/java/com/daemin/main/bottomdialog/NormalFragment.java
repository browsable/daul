package com.daemin.main.bottomdialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daemin.common.BasicFragment;
import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-07-05.
 */
public class NormalFragment extends BasicFragment {

    public NormalFragment() {
        super(R.layout.bottom_dialog_normal, "NormalFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        if (layoutId > 0) {

        }
        return root;
    }
}
