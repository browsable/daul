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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.data.EnrollData;
import com.daemin.dialog.DialAddTimePicker;
import com.daemin.dialog.DialColor;
import com.daemin.dialog.DialDefault;
import com.daemin.dialog.DialRepeat;
import com.daemin.enumclass.Dates;
import com.daemin.event.EditCheckEvent;
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
    private Boolean weekFlag,editFlag;
    private int dayOfWeek,dayOfMonth;
    private static int pos;
    public EnrollAdapter(Context context, List<EnrollData> values, Boolean weekFlag, int xth, int dayOfMonth) {
        super(context, R.layout.listitem_enroll, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.weekFlag = weekFlag;
        this.dayOfWeek = xth;
        this.dayOfMonth = dayOfMonth;
        this.editFlag = false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        this.pos = position;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.listitem_enroll, parent, false);
            holder.tvMD = (TextView) convertView.findViewById(R.id.tvMD);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.tvSingleSchedule = (TextView) convertView.findViewById(R.id.tvSingleSchedule);
            holder.etTitle = (EditText) convertView.findViewById(R.id.etTitle);
            holder.etMemo = (EditText) convertView.findViewById(R.id.etMemo);
            holder.etPlace = (EditText) convertView.findViewById(R.id.etPlace);
            holder.btCommunity  = (Button) convertView.findViewById(R.id.btCommunity);
            holder.btInvite  = (Button) convertView.findViewById(R.id.btInvite);
            holder.btCheck  = (Button) convertView.findViewById(R.id.btCheck);
            holder.btEdit = (Button) convertView.findViewById(R.id.btEdit);
            holder.btRemove  = (Button) convertView.findViewById(R.id.btRemove);
            holder.btColor  = (Button) convertView.findViewById(R.id.btColor);
            holder.btShare = (Button) convertView.findViewById(R.id.btShare);
            holder.btAlarm = (Button) convertView.findViewById(R.id.btAlarm);
            holder.btRepeat = (Button) convertView.findViewById(R.id.btRepeat);
            holder.llTime = (LinearLayout) convertView.findViewById(R.id.llTime);
            holder.gd = (GradientDrawable) holder.btColor.getBackground().mutate();
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        // Populate the text
        String md = Dates.NOW.month+context.getResources().getString(R.string.month)+" "+getItem(position).getDayOfMonth()+context.getResources().getString(R.string.day);
        holder.tvMD.setText(md);
        String time = getItem(position).getStartHour()+":"+getItem(position).getStartMin()+"~"
                +getItem(position).getEndHour()+":"+getItem(position).getEndMin();
        holder.tvTime.setText(time);
        holder.etTitle.setText(getItem(position).getTitle());
        holder.etMemo.setText(getItem(position).getMemo());
        holder.etPlace.setText(getItem(position).getPlace());
        holder.tvSingleSchedule.setText("" + MyTimeRepo.singleCheckWithTimeCode(context, getItem(position).getTimeCode()));

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
                editFlag = false;
                String title = holder.etTitle.getText().toString();
                String place = holder.etPlace.getText().toString();
                String memo = holder.etMemo.getText().toString();
                String startHour = getItem(pos).getStartHour();
                String startMin = getItem(pos).getStartMin();
                String endHour = getItem(pos).getEndHour();
                String endMin = getItem(pos).getEndMin();
                if(holder.etTitle.length()==0){
                    Toast.makeText(context, context.getResources().getString(R.string.normal_empty), Toast.LENGTH_SHORT).show();
                    holder.etTitle.setHintTextColor(Color.RED);
                }else {
                    if(Boolean.parseBoolean(holder.tvSingleSchedule.getText().toString())){
                        if (getItem(pos).isSingleEffect()) { // 하나의 일정 수정
                            String colorName;
                            if (holder.btColor.getTag() == null)
                                colorName = getItem(pos).getColor();
                            else
                                colorName = context.getResources().getString((int) holder.btColor.getTag());
                            MyTime mt = MyTimeRepo.getMyTimeForId(context, getItem(pos).get_id());
                            if (getItem(pos).isTimeChanged()) {
                                int year = Dates.NOW.year;
                                int month = Dates.NOW.month;
                                int day = Integer.parseInt(getItem(pos).getDayOfMonth());
                                int dayOfWeek = Dates.NOW.getDayOfWeek(year, month, day);
                                int sHour = Integer.parseInt(startHour);
                                int sMin = Integer.parseInt(startMin);
                                int eHour = Integer.parseInt(endHour);
                                int eMin = Integer.parseInt(endMin);
                                int xth = Convert.dayOfWeekTowXth(dayOfWeek);
                                mt.setDayofweek(xth);
                                mt.setDayofmonth(day);
                                mt.setStarthour(sHour);
                                mt.setStartmin(sMin);
                                mt.setEndhour(eHour);
                                mt.setEndmin(eMin);
                                if (getItem(pos).getTimeType().equals("0")) { // 일반 스케쥴인 경우만
                                    long startMillies = Dates.NOW.getDateMillis(year, month, day, sHour, sMin);
                                    long endMillies = Dates.NOW.getDateMillis(year, month, day, eHour, eMin);
                                    mt.setStartmillis(startMillies);
                                    mt.setEndmillis(endMillies);
                                }
                                mt.setName(title);
                                mt.setPlace(place);
                                mt.setMemo(memo);
                                mt.setColor(colorName);
                                EventBus.getDefault().post(new EditCheckEvent(true));
                            } else {
                                mt.setName(title);
                                mt.setPlace(place);
                                mt.setMemo(memo);
                                mt.setColor(colorName);
                                EventBus.getDefault().post(new EditCheckEvent(false));
                            }
                            MyTimeRepo.insertOrUpdate(context, mt);
                            if (weekFlag) Common.fetchWeekData();
                            else Common.fetchMonthData();
                        }else { //모든일정 수정

                        }
                    }else{
                        DialDefault dd = new DialDefault(context,
                                context.getString(R.string.dialenroll_edit),
                                pos + "",
                                6);
                        dd.show();
                    }

                }
            }
        });
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFlag = true;
                holder.gd.setColor(Color.parseColor(getItem(pos).getColor()));
                holder.gd.invalidateSelf();
                holder.btRemove.setVisibility(View.GONE);
                holder.btEdit.setVisibility(View.GONE);
                holder.btCheck.setVisibility(View.VISIBLE);
                holder.btColor.setVisibility(View.VISIBLE);
                holder.tvMD.setVisibility(View.VISIBLE);
                //holder.btShare.setVisibility(View.GONE);
                //holder.btAlarm.setVisibility(View.GONE);
                holder.etTitle.setEnabled(true);
                holder.etTitle.setHint(context.getResources().getString(R.string.normal_name));
                holder.etTitle.setSelection(holder.etTitle.length());
                holder.etTitle.setBackgroundResource(R.drawable.bg_black_bottomline);
                holder.etPlace.setEnabled(true);
                holder.etPlace.setHint(context.getResources().getString(R.string.normal_place));
                holder.etPlace.setSelection(holder.etPlace.length());
                holder.etMemo.setEnabled(true);
                holder.etMemo.setHint(context.getResources().getString(R.string.normal_memo));
                holder.etMemo.requestFocus();
                holder.etPlace.setBackgroundResource(R.drawable.bg_black_bottomline);
                holder.etMemo.setBackgroundResource(R.drawable.bg_black_bottomline);
                holder.llTime.setBackgroundResource(R.drawable.bg_black_bottomline);
                if(getItem(pos).getTimeType().equals("0")){
                    holder.btRepeat.setVisibility(View.VISIBLE);
                }else{
                    holder.btRepeat.setVisibility(View.GONE);
                }
            }
        });
        holder.llTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editFlag) {
                    DialAddTimePicker datp = new DialAddTimePicker(
                            context, Dates.NOW.getMonthDay(),
                            dayOfMonth,
                            pos,
                            getItem(pos).getStartHour(),
                            getItem(pos).getStartMin(),
                            getItem(pos).getEndHour(),
                            getItem(pos).getEndMin());
                    datp.show();
                }
            }
        });

        holder.btRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap dayIndex = new HashMap<>();
                dayIndex.put(dayOfWeek,dayOfWeek);
                DialRepeat dr = new DialRepeat(context,dayIndex,getItem(pos).getRepeat(),pos);
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
                if(Boolean.parseBoolean(holder.tvSingleSchedule.getText().toString())){
                    MyTimeRepo.deleteWithTimeCode(context, getItem(pos).getTimeCode());
                    if (weekFlag) Common.fetchWeekData();
                    else Common.fetchMonthData();
                    EventBus.getDefault().post(new RemoveEnrollEvent(pos));
                    EventBus.getDefault().post(new SetCreditEvent());
                }else{
                    DialDefault dd = new DialDefault(context,
                            context.getString(R.string.dialenroll_delete),
                            pos + "",
                            6);
                    dd.show();
                }
            }
        });
        return convertView;
    }

    private static class Holder {
        public TextView tvTime,tvMD, tvSingleSchedule;
        public EditText etTitle, etPlace,etMemo;
        public Button btCommunity,btCheck,btEdit,btInvite, btRemove,btColor,btShare,btAlarm,btRepeat;
        public GradientDrawable gd;
        public LinearLayout llTime;
    }
}
