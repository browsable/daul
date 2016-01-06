package com.daemin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daemin.common.Common;
import com.daemin.data.EnrollData;
import com.daemin.dialog.DialColor;
import com.daemin.enumclass.User;
import com.daemin.event.RemoveEnrollEvent;
import com.daemin.event.SetColorEvent;
import com.daemin.event.SetCreditEvent;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;

import java.util.List;

import de.greenrobot.event.EventBus;
import timedao.MyTime;

/**
 * Created by HOME on 2015-09-11.
 */
public class EnrollAdapter  extends ArrayAdapter<EnrollData> {
    private LayoutInflater mInflater;
    private Context context;
    private Boolean weekFlag;
    public EnrollAdapter(Context context, List<EnrollData> values, Boolean weekFlag) {
        super(context, R.layout.listitem_enroll, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.weekFlag = weekFlag;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.listitem_enroll, parent, false);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.etTitle = (EditText) convertView.findViewById(R.id.etTitle);
            holder.etMemo = (EditText) convertView.findViewById(R.id.etMemo);
            holder.etPlace = (EditText) convertView.findViewById(R.id.etPlace);
            holder.tvId = (TextView) convertView.findViewById(R.id.tvId);
            holder.tvTimeCode = (TextView) convertView.findViewById(R.id.tvTimeCode);
            holder.tvTimeType = (TextView) convertView.findViewById(R.id.tvTimeType);
            holder.tvCredit = (TextView) convertView.findViewById(R.id.tvCredit);
            holder.tvColor = (TextView) convertView.findViewById(R.id.tvColor);
            holder.btCommunity  = (Button) convertView.findViewById(R.id.btCommunity);
            holder.btInvite  = (Button) convertView.findViewById(R.id.btInvite);
            holder.btCheck  = (Button) convertView.findViewById(R.id.btCheck);
            holder.btEdit = (Button) convertView.findViewById(R.id.btEdit);
            holder.btRemove  = (Button) convertView.findViewById(R.id.btRemove);
            holder.btColor  = (Button) convertView.findViewById(R.id.btColor);
            holder.btExpand = (Button) convertView.findViewById(R.id.btExpand);
            holder.btShare = (Button) convertView.findViewById(R.id.btShare);
            holder.btAlarm = (Button) convertView.findViewById(R.id.btAlarm);
            holder.btRepeat = (Button) convertView.findViewById(R.id.btRepeat);
            holder.gd = (GradientDrawable) holder.btColor.getBackground().mutate();
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        // Populate the text
        holder.tvTime.setText(getItem(position).getTime());
        holder.etTitle.setText(getItem(position).getTitle());
        holder.etPlace.setText(getItem(position).getPlace());
        holder.etMemo.setText(getItem(position).getMemo());
        holder.tvId.setText(String.valueOf(getItem(position).get_id()));
        holder.tvTimeCode.setText(getItem(position).getTimeCode());
        holder.tvTimeType.setText(getItem(position).getTimeType());
        holder.tvCredit.setText(getItem(position).getCredit());
        holder.tvColor.setText(getItem(position).getColor());
        holder.btCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title =  holder.etTitle.getText().toString();
                String place =  holder.etPlace.getText().toString();
                String memo =  holder.etMemo.getText().toString();
                if(holder.etTitle.length()==0){
                    Toast.makeText(context, context.getResources().getString(R.string.normal_empty), Toast.LENGTH_SHORT).show();
                    holder.etTitle.setHintTextColor(Color.RED);
                }else {
                    holder.btRemove.setVisibility(View.VISIBLE);
                    holder.btEdit.setVisibility(View.VISIBLE);
                    holder.btCheck.setVisibility(View.GONE);
                    holder.btExpand.setVisibility(View.GONE);
                    holder.btColor.setVisibility(View.GONE);
                    holder.btShare.setVisibility(View.GONE);
                    holder.btAlarm.setVisibility(View.GONE);
                    holder.btRepeat.setVisibility(View.GONE);
                    holder.etTitle.setEnabled(false);
                    holder.etPlace.setEnabled(false);
                    holder.etMemo.setEnabled(false);
                    holder.etTitle.setTextColor(Color.BLACK);
                    holder.etPlace.setTextColor(Color.BLACK);
                    holder.etMemo.setTextColor(Color.BLACK);
                    holder.etTitle.setHint("");
                    holder.etPlace.setHint("");
                    holder.etMemo.setHint("");
                    String colorName;
                    if(holder.btColor.getTag()==null) colorName = holder.tvColor.getText().toString();
                    else colorName = context.getResources().getString((int)holder.btColor.getTag());
                    holder.tvColor.setText(colorName);
                    for (MyTime mt : MyTimeRepo.getMyTimeWithTimeCode(context, holder.tvTimeCode.getText().toString())) {
                        mt.setName(title);
                        mt.setPlace(place);
                        mt.setMemo(memo);
                        mt.setColor(colorName);
                        MyTimeRepo.insertOrUpdate(context, mt);
                    }
                    if(weekFlag)Common.fetchWeekData();
                    else Common.fetchMonthData();
                }
            }
        });
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btRemove.setVisibility(View.GONE);
                holder.btEdit.setVisibility(View.GONE);
                holder.btCheck.setVisibility(View.VISIBLE);
                holder.btExpand.setVisibility(View.VISIBLE);
                holder.etPlace.setEnabled(true);
                holder.etPlace.setTextColor(Color.GRAY);
                holder.etPlace.setHint(context.getResources().getString(R.string.normal_place));
                holder.etPlace.setSelection(holder.etPlace.length());
                holder.etPlace.requestFocus();
                if(holder.tvTimeType.getText().toString().equals("0")){
                    holder.etTitle.setEnabled(true);
                    holder.etMemo.setEnabled(true);
                    holder.etTitle.setTextColor(Color.GRAY);
                    holder.etMemo.setTextColor(Color.GRAY);
                    holder.etTitle.setHint(context.getResources().getString(R.string.normal_name));
                    holder.etMemo.setHint(context.getResources().getString(R.string.normal_memo));
                    holder.etTitle.setSelection(holder.etTitle.length());
                    holder.etTitle.requestFocus();
                }
            }
        });
        holder.btExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btExpand.setVisibility(View.GONE);
                holder.btColor.setVisibility(View.VISIBLE);
                //holder.btShare.setVisibility(View.VISIBLE);
                //holder.btAlarm.setVisibility(View.VISIBLE);
                //holder.btRepeat.setVisibility(View.VISIBLE);
                holder.gd.setColor(Color.parseColor(holder.tvColor.getText().toString()));
                holder.gd.invalidateSelf();
            }
        });
        holder.btColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialColor dc = new DialColor(context,holder.btColor);
                dc.show();
            }
        });
        holder.btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTimeRepo.deleteWithTimeCode(context, holder.tvTimeCode.getText().toString());
                if(holder.tvTimeType.getText().toString().equals("1")){
                    String creditSum = String.valueOf(Integer.parseInt(User.INFO.getCreditSum())
                            -Integer.parseInt(holder.tvCredit.getText().toString()));
                    User.INFO.getEditor().putString("creditSum",creditSum).commit();
                }
                Common.fetchWeekData();
                EventBus.getDefault().post(new RemoveEnrollEvent(holder.tvTimeCode.getText().toString()));
                EventBus.getDefault().post(new SetCreditEvent());
            }
        });
        return convertView;
    }
    private static class Holder {
        public TextView tvTime, tvId, tvTimeCode, tvTimeType, tvCredit,tvColor;
        public EditText etTitle, etMemo, etPlace;
        public Button btCommunity,btCheck,btEdit,btInvite, btRemove,btColor,btShare,btAlarm,btRepeat,btExpand;
        private GradientDrawable gd;
    }
}
