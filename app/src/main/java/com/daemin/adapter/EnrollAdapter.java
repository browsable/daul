package com.daemin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daemin.common.Common;
import com.daemin.data.EnrollData;
import com.daemin.dialog.DialColor;
import com.daemin.dialog.DialRepeat;
import com.daemin.event.RemoveEnrollEvent;
import com.daemin.event.SetCreditEvent;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;

import java.util.HashMap;
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
    private int dayOfWeek;
    private String timeType;
    public EnrollAdapter(Context context, List<EnrollData> values, Boolean weekFlag, int xth) {
        super(context, R.layout.listitem_enroll, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.weekFlag = weekFlag;
        this.dayOfWeek = xth;
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
            holder.tvStartHour = (TextView) convertView.findViewById(R.id.tvStartHour);
            holder.tvStartMin = (TextView) convertView.findViewById(R.id.tvStartMin);
            holder.tvEndHour = (TextView) convertView.findViewById(R.id.tvEndHour);
            holder.tvEndMin = (TextView) convertView.findViewById(R.id.tvEndMin);
            holder.etTitle = (EditText) convertView.findViewById(R.id.etTitle);
            holder.etMemo = (EditText) convertView.findViewById(R.id.etMemo);
            holder.etPlace = (EditText) convertView.findViewById(R.id.etPlace);
            holder.tvId = (TextView) convertView.findViewById(R.id.tvId);
            holder.tvTimeCode = (TextView) convertView.findViewById(R.id.tvTimeCode);
            holder.tvTimeType = (TextView) convertView.findViewById(R.id.tvTimeType);
            holder.tvRepeat = (TextView) convertView.findViewById(R.id.tvRepeat);
            holder.tvColor = (TextView) convertView.findViewById(R.id.tvColor);
            holder.btCommunity  = (Button) convertView.findViewById(R.id.btCommunity);
            holder.btInvite  = (Button) convertView.findViewById(R.id.btInvite);
            holder.btCheck  = (Button) convertView.findViewById(R.id.btCheck);
            holder.btEdit = (Button) convertView.findViewById(R.id.btEdit);
            holder.btRemove  = (Button) convertView.findViewById(R.id.btRemove);
            holder.btColor  = (Button) convertView.findViewById(R.id.btColor);
            holder.btShare = (Button) convertView.findViewById(R.id.btShare);
            holder.btAlarm = (Button) convertView.findViewById(R.id.btAlarm);
            holder.btTime = (Button) convertView.findViewById(R.id.btTime);
            holder.btRepeat = (Button) convertView.findViewById(R.id.btRepeat);
            holder.gd = (GradientDrawable) holder.btColor.getBackground().mutate();
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        // Populate the text
        holder.tvStartHour.setText(getItem(position).getStartHour());
        holder.tvStartMin.setText(getItem(position).getStartMin());
        holder.tvEndHour.setText(getItem(position).getEndHour());
        holder.tvEndMin.setText(getItem(position).getEndMin());
        holder.etTitle.setText(getItem(position).getTitle());
        holder.etPlace.setText(getItem(position).getPlace());
        holder.tvId.setText(String.valueOf(getItem(position).get_id()));
        holder.tvTimeCode.setText(getItem(position).getTimeCode());
        timeType = getItem(position).getTimeType();
        holder.tvTimeType.setText(timeType);
        holder.tvRepeat.setText(getItem(position).getRepeat());
        holder.tvColor.setText(getItem(position).getColor());
        holder.etMemo.setText(getItem(position).getMemo());
        holder.etMemo.setId(position);
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
                String title = holder.etTitle.getText().toString();
                String place = holder.etPlace.getText().toString();
                String memo = holder.etMemo.getText().toString();
                final int position = holder.etMemo.getId();
                getItem(position).setMemo(memo);
                if(holder.etTitle.length()==0){
                    Toast.makeText(context, context.getResources().getString(R.string.normal_empty), Toast.LENGTH_SHORT).show();
                    holder.etTitle.setHintTextColor(Color.RED);
                }else {
                    holder.btRemove.setVisibility(View.VISIBLE);
                    holder.btEdit.setVisibility(View.VISIBLE);
                    holder.btCheck.setVisibility(View.GONE);
                    holder.btColor.setVisibility(View.GONE);
                    holder.btShare.setVisibility(View.GONE);
                    holder.btAlarm.setVisibility(View.GONE);
                    holder.btRepeat.setVisibility(View.GONE);
                    holder.btTime.setVisibility(View.GONE);
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
                holder.gd.setColor(Color.parseColor(holder.tvColor.getText().toString()));
                holder.gd.invalidateSelf();
                holder.btRemove.setVisibility(View.GONE);
                holder.btEdit.setVisibility(View.GONE);
                holder.btCheck.setVisibility(View.VISIBLE);
                holder.btColor.setVisibility(View.VISIBLE);
                holder.btTime.setVisibility(View.VISIBLE);
                //holder.btShare.setVisibility(View.GONE);
                //holder.btAlarm.setVisibility(View.GONE);
                holder.etPlace.setEnabled(true);
                holder.etPlace.setTextColor(Color.GRAY);
                holder.etPlace.setHint(context.getResources().getString(R.string.normal_place));
                holder.etPlace.setSelection(holder.etPlace.length());
                holder.etMemo.setEnabled(true);
                holder.etMemo.setTextColor(Color.GRAY);
                holder.etMemo.setHint(context.getResources().getString(R.string.normal_memo));
                holder.etMemo.requestFocus();
                if(timeType.equals("0")){
                    holder.btRepeat.setVisibility(View.VISIBLE);
                    holder.etTitle.setEnabled(true);
                    holder.etTitle.setTextColor(Color.GRAY);
                    holder.etTitle.setHint(context.getResources().getString(R.string.normal_name));
                    holder.etTitle.setSelection(holder.etTitle.length());
                }else{
                    holder.btRepeat.setVisibility(View.GONE);
                }
            }
        });
        holder.btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        holder.btRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap dayIndex = new HashMap<>();
                dayIndex.put(dayOfWeek,dayOfWeek);
                DialRepeat dr = new DialRepeat(context,dayIndex,holder.tvRepeat.getText().toString());
                dr.show();
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
                if (weekFlag) Common.fetchWeekData();
                else Common.fetchMonthData();
                EventBus.getDefault().post(new RemoveEnrollEvent(holder.tvTimeCode.getText().toString()));
                EventBus.getDefault().post(new SetCreditEvent());
            }
        });
        return convertView;
    }

    private static class Holder {
        public TextView tvId, tvTimeCode, tvTimeType,tvRepeat,tvColor,tvStartHour,tvStartMin,tvEndHour,tvEndMin;
        public EditText etTitle, etPlace,etMemo;
        public Button btCommunity,btCheck,btEdit,btInvite, btRemove,btColor,btTime,btShare,btAlarm,btRepeat;
        private GradientDrawable gd;
    }
}
