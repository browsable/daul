package com.daemin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.daemin.common.Common;
import com.daemin.data.EnrollData;
import com.daemin.enumclass.User;
import com.daemin.event.RemoveEnrollEvent;
import com.daemin.event.SetCreditEvent;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by HOME on 2015-09-11.
 */
public class EnrollAdapter  extends ArrayAdapter<EnrollData> {
    private LayoutInflater mInflater;
    private Context context;
    public EnrollAdapter(Context context, List<EnrollData> values) {
        super(context, R.layout.listitem_enroll, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
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
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvMemo = (TextView) convertView.findViewById(R.id.tvMemo);
            holder.tvPlace = (TextView) convertView.findViewById(R.id.tvPlace);
            holder.tvId = (TextView) convertView.findViewById(R.id.tvId);
            holder.tvTimeCode = (TextView) convertView.findViewById(R.id.tvTimeCode);
            holder.tvTimeType = (TextView) convertView.findViewById(R.id.tvTimeType);
            holder.tvCredit = (TextView) convertView.findViewById(R.id.tvCredit);
            holder.btCommunity  = (Button) convertView.findViewById(R.id.btCommunity);
            holder.btInvite  = (Button) convertView.findViewById(R.id.btInvite);
            holder.btEdit = (Button) convertView.findViewById(R.id.btEdit);
            holder.btRemove  = (Button) convertView.findViewById(R.id.btRemove);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        // Populate the text
        holder.tvTime.setText(getItem(position).getTime());
        holder.tvTitle.setText(getItem(position).getTitle());
        holder.tvPlace.setText(getItem(position).getPlace());
        holder.tvMemo.setText(getItem(position).getMemo());
        holder.tvId.setText(String.valueOf(getItem(position).get_id()));
        holder.tvTimeCode.setText(getItem(position).getTimeCode());
        holder.tvTimeType.setText(getItem(position).getTimeType());
        holder.tvCredit.setText(getItem(position).getCredit());
        /*if(holder.tvPlace.getText().toString().equals(""))
            holder.tvPlace.setVisibility(View.GONE);
        if(holder.tvMemo.getText().toString().equals(""))
            holder.tvMemo.setVisibility(View.GONE);*/
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
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        public TextView tvTime, tvTitle, tvMemo, tvPlace, tvId, tvTimeCode, tvTimeType, tvCredit;
        public Button btCommunity, btInvite, btEdit, btRemove;
    }
}
