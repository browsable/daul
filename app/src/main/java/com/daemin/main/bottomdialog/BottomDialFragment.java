package com.daemin.main.bottomdialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-07-05.
 */
public class BottomDialFragment extends DialogFragment {

    Button btNormal, btUniv, btRecommend,btCancel,btAddTime,btColor;
    Fragment mContent = null;
    boolean colorFlag = false;
    LinearLayout llColor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bottom_dialog_main, container,
                false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(false);
        Window window = getDialog().getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        llColor = (LinearLayout) root.findViewById(R.id.llColor);
        btNormal = (Button) root.findViewById(R.id.btNormal);
        btUniv = (Button) root.findViewById(R.id.btUniv);
        btRecommend = (Button) root.findViewById(R.id.btRecommend);
        btAddTime = (Button) root.findViewById(R.id.btAddTime);
        btCancel = (Button) root.findViewById(R.id.btCancel);
        btColor = (Button) root.findViewById(R.id.btColor);
        btNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btAddTime.setVisibility(View.VISIBLE);
                btNormal.setTextColor(getActivity().getResources().getColor(
                        R.color.white));
                btUniv.setTextColor(getActivity().getResources().getColor(
                        R.color.gray));
                btRecommend.setTextColor(getActivity().getResources().getColor(
                        R.color.gray));
                changeBottomFragment(NormalFragment.class);
            }
        });

        btUniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btAddTime.setVisibility(View.GONE);

                btNormal.setTextColor(getActivity().getResources().getColor(
                        R.color.gray));
                btRecommend.setTextColor(getActivity().getResources().getColor(
                        R.color.gray));
                btUniv.setTextColor(getActivity().getResources().getColor(
                        R.color.white));
                changeBottomFragment(UnivFragment.class);
            }
        });
        btRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btNormal.setTextColor(getActivity().getResources().getColor(
                        R.color.gray));
                btRecommend.setTextColor(getActivity().getResources().getColor(
                        R.color.white));
                btUniv.setTextColor(getActivity().getResources().getColor(
                        R.color.gray));
                changeBottomFragment(RecommendFragment.class);
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!colorFlag) {
                    llColor.setVisibility(View.VISIBLE);
                    colorFlag = true;
                } else {
                    llColor.setVisibility(View.INVISIBLE);
                    colorFlag = false;
                }
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        params.height = dm.heightPixels/3;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);

        super.onResume();
    }
    public void changeBottomFragment(Class cl) {
        final FragmentManager fm = getChildFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        String fragmentTag = cl.getSimpleName();
        Fragment newFragment = fm.findFragmentByTag(fragmentTag);

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
                ft.add(R.id.frame_container_bottom, newFragment, fragmentTag);
            }
        } else
            ft.attach(newFragment);

        if (newFragment != null) {
            mContent = newFragment;
            ft.commit();
        }
    }
}
