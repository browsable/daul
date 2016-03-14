package com.daemin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daemin.common.Convert;
import com.daemin.dialog.DialAddTimePicker;
import com.daemin.dialog.DialColor;
import com.daemin.dialog.DialRepeat;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.User;
import com.daemin.event.EditCheckEvent;
import com.daemin.event.RemoveEnrollEvent;
import com.daemin.event.SetCreditEvent;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import timedao.MyTime;

/**
 * Created by HOME on 2015-09-11.
 */
public class EnrollAdapter extends ArrayAdapter<MyTime> {
    private LayoutInflater mInflater;
    private Context context;
    private Boolean editFlag;

    public EnrollAdapter(Context context, List<MyTime> values) {
        super(context, R.layout.listitem_enroll, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.editFlag = false;
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
            holder.tvMD = (TextView) convertView.findViewById(R.id.tvMD);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
            holder.tvSingleSchedule = (TextView) convertView.findViewById(R.id.tvSingleSchedule);
            holder.etTitle = (EditText) convertView.findViewById(R.id.etTitle);
            holder.etMemo = (EditText) convertView.findViewById(R.id.etMemo);
            holder.etPlace = (EditText) convertView.findViewById(R.id.etPlace);
            holder.btCommunity = (Button) convertView.findViewById(R.id.btCommunity);
            holder.btInvite = (Button) convertView.findViewById(R.id.btInvite);
            holder.btCheck = (Button) convertView.findViewById(R.id.btCheck);
            holder.btEdit = (Button) convertView.findViewById(R.id.btEdit);
            holder.btRemove = (Button) convertView.findViewById(R.id.btRemove);
            holder.btColor = (Button) convertView.findViewById(R.id.btColor);
            holder.btShare = (Button) convertView.findViewById(R.id.btShare);
            holder.btAlarm = (Button) convertView.findViewById(R.id.btAlarm);
            holder.btRepeat = (Button) convertView.findViewById(R.id.btRepeat);
            holder.llTime = (LinearLayout) convertView.findViewById(R.id.llTime);
            holder.gd = (GradientDrawable) holder.btColor.getBackground().mutate();
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        MyTime mt = getItem(position);
        String md;
        if (mt.getTimetype() == 0)
            md = Dates.NOW.month + context.getResources().getString(R.string.month) + " " + mt.getDayofmonth() + context.getResources().getString(R.string.day);
        else
            md = Convert.XthToDayOfWeek(mt.getDayofweek());
        holder.tvMD.setText(md);
        String time = Convert.IntToString(mt.getStarthour()) + ":"
                + Convert.IntToString(mt.getStartmin()) + "~"
                + Convert.IntToString(mt.getEndhour()) + ":"
                + Convert.IntToString(mt.getEndmin());
        holder.tvTime.setText(time);
        holder.etTitle.setText(mt.getName());
        holder.etMemo.setText(mt.getMemo());
        holder.etPlace.setText(mt.getPlace());
        holder.tvSingleSchedule.setText(MyTimeRepo.singleCheckWithTimeCode(context, mt.getTimecode()) + "");
        holder.tvPosition.setText("" + position);
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
                final int position = Integer.parseInt(holder.tvPosition.getText().toString());
                final MyTime mt = getItem(position);
                final String title = holder.etTitle.getText().toString();
                final String place = holder.etPlace.getText().toString();
                final String memo = holder.etMemo.getText().toString();
                final boolean changed = mt.isTimeChanged()||mt.isRepeatChanged();
                if (holder.etTitle.length() == 0) {
                    Toast.makeText(context, context.getResources().getString(R.string.normal_empty), Toast.LENGTH_SHORT).show();
                    holder.etTitle.setHintTextColor(Color.RED);
                } else {
                    final String colorName;
                    if (holder.btColor.getTag() == null)
                        colorName = mt.getColor();
                    else
                        colorName = context.getResources().getString((int) holder.btColor.getTag());
                    Boolean single = Boolean.parseBoolean(holder.tvSingleSchedule.getText().toString());
                    if (single||changed) {
                        AddSchedule(mt, title, place, memo, colorName);
                        EventBus.getDefault().post(new RemoveEnrollEvent(mt.getId()));
                        if(single){
                            EventBus.getDefault().post(new EditCheckEvent(true));
                        }
                        else{
                            EventBus.getDefault().post(new EditCheckEvent(false));
                        }
                    } else {
                        final View dialogView = mInflater.inflate(R.layout.dialog_effect, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setView(dialogView);
                        TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
                        tvTitle.setText(context.getResources().getString(R.string.dialenroll_edit));
                        builder.setPositiveButton(context.getResources().getString(R.string.btDialSetting), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                RadioGroup rgGroup = (RadioGroup) dialogView.findViewById(R.id.rgGroup);
                                if (rgGroup.getCheckedRadioButtonId() != -1) {
                                    int id = rgGroup.getCheckedRadioButtonId();
                                    View radioButton = rgGroup.findViewById(id);
                                    int radioId = rgGroup.indexOfChild(radioButton);
                                    if (radioId == 0) {
                                        AddSchedule(mt, title, place, memo, colorName);
                                        //MyTimeRepo.deleteWithId(context, mt.getId());
                                        EventBus.getDefault().post(new RemoveEnrollEvent(mt.getId()));
                                    } else {
                                        for (MyTime m : MyTimeRepo.getMyTimeForTimeCode(context, mt.getTimecode())) {
                                            AddSchedule(m, title, place, memo, colorName);
                                            EventBus.getDefault().post(new RemoveEnrollEvent(m.getId()));
                                        }
                                    }
                                }
                                if(changed)
                                    EventBus.getDefault().post(new EditCheckEvent(false));
                                else
                                    EventBus.getDefault().post(new EditCheckEvent(true));
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton(context.getResources().getString(R.string.btDialCancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                }
            }
        });
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFlag = true;
                int position = Integer.parseInt(holder.tvPosition.getText().toString());
                holder.gd.setColor(Color.parseColor(getItem(position).getColor()));
                holder.gd.invalidateSelf();
                holder.btRemove.setVisibility(View.GONE);
                holder.btEdit.setVisibility(View.GONE);
                holder.btCheck.setVisibility(View.VISIBLE);
                holder.btColor.setVisibility(View.VISIBLE);
                holder.tvMD.setVisibility(View.VISIBLE);
                //holder.btShare.setVisibility(View.GONE);
                //holder.btAlarm.setVisibility(View.GONE);
                holder.etPlace.setEnabled(true);
                holder.etPlace.setHint(context.getResources().getString(R.string.normal_place));
                holder.etPlace.setSelection(holder.etPlace.length());
                holder.etMemo.setEnabled(true);
                holder.etMemo.setHint(context.getResources().getString(R.string.normal_memo));
                holder.etMemo.requestFocus();
                holder.etPlace.setBackgroundResource(R.drawable.bg_black_bottomline);
                holder.etMemo.setBackgroundResource(R.drawable.bg_black_bottomline);
                holder.llTime.setBackgroundResource(R.drawable.bg_black_bottomline);
                if (getItem(position).getTimetype() == 0) {
                    holder.etTitle.setEnabled(true);
                    holder.etTitle.setHint(context.getResources().getString(R.string.normal_name));
                    holder.etTitle.setSelection(holder.etTitle.length());
                    holder.etTitle.setBackgroundResource(R.drawable.bg_black_bottomline);
                    holder.btRepeat.setVisibility(View.VISIBLE);
                } else {
                    holder.btRepeat.setVisibility(View.GONE);
                }
            }
        });
        holder.llTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editFlag) {
                    int position = Integer.parseInt(holder.tvPosition.getText().toString());
                    MyTime mt = getItem(position);
                    int dayOfMonth;
                    String MD[];
                    if(mt.getTimetype()==0){
                        dayOfMonth = mt.getDayofmonth();
                        MD= Dates.NOW.getMonthDay();
                    }else{
                        dayOfMonth = mt.getDayofweek();
                        MD=context.getResources().getStringArray(R.array.dayArray);
                    }
                    DialAddTimePicker datp = new DialAddTimePicker(
                            context,mt.getTimetype(), MD,
                            dayOfMonth,
                            position,
                            mt.getStarthour() + "",
                            mt.getStartmin() + "",
                            mt.getEndhour() + "",
                            mt.getEndmin() + "");
                    datp.show();
                }
            }
        });
        holder.btRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(holder.tvPosition.getText().toString());
                int dayOfWeek = getItem(position).getDayofweek();
                HashMap dayIndex = new HashMap<>();
                dayIndex.put(dayOfWeek, dayOfWeek);
                DialRepeat dr = new DialRepeat(context, dayIndex, getItem(position).getRepeat(),position);
                dr.show();
            }
        });
        holder.btColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialColor dc = new DialColor(context, holder.btColor);
                dc.show();
            }
        });
        holder.btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = Integer.parseInt(holder.tvPosition.getText().toString());
                final MyTime mt = getItem(position);
                if (Boolean.parseBoolean(holder.tvSingleSchedule.getText().toString())) {
                    if(mt.getTimetype()==1)
                        EventBus.getDefault().post(new SetCreditEvent(mt.getName()));
                    MyTimeRepo.deleteWithId(context, mt.getId());
                    EventBus.getDefault().post(new RemoveEnrollEvent(mt.getId()));
                } else {
                    final View dialogView = mInflater.inflate(R.layout.dialog_effect, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(dialogView);
                    TextView tvTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
                    tvTitle.setText(context.getResources().getString(R.string.dialenroll_delete));
                    builder.setPositiveButton(context.getResources().getString(R.string.btDialSetting), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            RadioGroup rgGroup = (RadioGroup) dialogView.findViewById(R.id.rgGroup);
                            if (rgGroup.getCheckedRadioButtonId() != -1) {
                                int id = rgGroup.getCheckedRadioButtonId();
                                View radioButton = rgGroup.findViewById(id);
                                int radioId = rgGroup.indexOfChild(radioButton);
                                if (radioId == 0) {
                                    MyTimeRepo.deleteWithId(context, mt.getId());
                                    EventBus.getDefault().post(new RemoveEnrollEvent(mt.getId()));
                                } else {
                                    if(mt.getTimetype()==1)
                                        EventBus.getDefault().post(new SetCreditEvent(mt.getName()));
                                    MyTimeRepo.deleteWithTimeCode(context, mt.getTimecode());
                                    EventBus.getDefault().post(new RemoveEnrollEvent(mt.getId()));
                                }
                            }
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton(context.getResources().getString(R.string.btDialCancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }
        });
        return convertView;
    }

    private static class Holder {
        public TextView tvTime, tvMD, tvPosition, tvSingleSchedule;
        public EditText etTitle, etPlace, etMemo;
        public Button btCommunity, btCheck, btEdit, btInvite, btRemove, btColor, btShare, btAlarm, btRepeat;
        public GradientDrawable gd;
        public LinearLayout llTime;
    }

    public void AddSchedule(MyTime mt, String title, String place, String memo, String colorName) {
        if(mt.isRepeatChanged()){
            MyTimeRepo.deleteWithTimeCode(context, mt.getTimecode());
        }else{
            MyTimeRepo.deleteWithId(context, mt.getId());
        }
        String repeat = mt.getRepeat();
        int timeType = mt.getTimetype();
        int startHour = mt.getStarthour();
        int startMin = mt.getStartmin();
        int endHour = mt.getEndhour();
        int endMin = mt.getEndmin();
        int repeatType, repeatNum, repeatPeriod; //반복횟수와 기간;
        repeatNum = 1;
        repeatPeriod = 0;
        try {
            if (repeat.length() > 1) { //~주마다, ~개월마다, ~년마다
                String s[] = repeat.split(":");
                repeatType = Integer.parseInt(s[0]);
                repeatPeriod = Integer.parseInt(s[1]);
                repeatNum = Integer.parseInt(s[2]);
            } else {
                repeatType = Integer.parseInt(repeat);
            }
        } catch (Exception e) {
            repeatType = 0;
            repeat = "0";
        }
        if (mt.isTimeChanged() || !mt.isRepeatChanged()) {
            repeatType = 0;
            repeat = "0";
            repeatNum = 1;
            repeatPeriod = 0;
        }
        for (int i = 0; i < repeatNum; i++) {
            MyTime myTime;
            if (timeType == 0) {
                int year;
                int titleMonth = Dates.NOW.month;
                int month = mt.getMonthofyear();
                if (month != titleMonth && titleMonth == 1)
                    year = Dates.NOW.year - 1;
                else year = Dates.NOW.year;
                int day = mt.getDayofmonth();
                DateTime startDt = Dates.NOW.getDateMillisWithRepeat(year, month, day, startHour, startMin, repeatType, repeatPeriod * i);
                DateTime endDt = Dates.NOW.getDateMillisWithRepeat(year, month, day, endHour, endMin, repeatType, repeatPeriod * i);
                int xth = Convert.dayOfWeekTowXth(startDt.getDayOfWeek());
                long startMillis = startDt.getMillis();
                myTime = new MyTime(null,
                        mt.getTimecode(), 0,
                        title,
                        startDt.getYear(), startDt.getMonthOfYear(), startDt.getDayOfMonth(),
                        xth, startHour, startMin, endHour, endMin,
                        startMillis, endDt.getMillis() - 1,
                        memo,
                        place,
                        User.INFO.latitude, User.INFO.longitude,
                        mt.getShare(),
                        mt.getAlarm(),
                        repeat,
                        colorName);

            } else {
                myTime = new MyTime(null,
                        mt.getTimecode(), 1,
                        mt.getName(),
                        null, null, null,
                        mt.getDayofweek(),
                        startHour, startMin, endHour, endMin,
                        null, null,
                        memo,
                        place,
                        User.INFO.latitude, User.INFO.longitude,
                        null,
                        null,
                        repeat,
                        colorName);
            }
            MyTimeRepo.insertOrUpdate(context, myTime);
        }
    }
}
