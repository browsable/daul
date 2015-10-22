/*
package com.daemin.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.daemin.common.Common;
import com.daemin.data.Comment;
import com.daemin.timetable.R;

import java.util.List;


class CommentListAdapter extends BaseAdapter{
    private List<Comment> comment;
    private String userId;
    private ListView listView;
    private TextView tvCountComment;
    private Activity activity;
    private int editMode, modifiedPos;
    public LayoutInflater inflater;
    public final int COMMENT_ADD_MODE = 0;
    public final int COMMENT_MODIFY_MODE = 1;

    public CommentListAdapter(List<Comment> comment, String userId, ListView listView, TextView tvCountComment, Activity activity) {
        this.comment = comment;
        this.userId = userId;
        this.listView = listView;
        this.tvCountComment = tvCountComment;
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("junyeong", this.toString() + " 생성자 : 삽입모드로 바뀜");
        editMode = COMMENT_ADD_MODE;
    }

    @Override
    public int getCount() {
        return comment.size();
    }

    @Override
    public Object getItem(int position) {
        return comment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final int pos = position;
        final Holder holder;
        if(convertView == null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.listitem_community_comment, parent, false);
            holder.tvChildId = (TextView) convertView.findViewById(R.id.tvChildId);
            holder.tvChildDate = (TextView) convertView.findViewById(R.id.tvChildDate);
            holder.tvChildContent = (EditText) convertView.findViewById(R.id.tvChildContent);
            holder.llCommentBts = (LinearLayout) convertView.findViewById(R.id.llCommentBts);
            holder.btChildEdit = (Button) convertView.findViewById(R.id.btChildEdit);
            holder.btChildRemove = (Button) convertView.findViewById(R.id.btChildRemove);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.tvChildId.setText(((Comment) getItem(position)).getUserId());
        holder.tvChildDate.setText(((Comment) getItem(position)).getDate());
        holder.tvChildContent.setText(Common.breakText(holder.tvChildContent.getPaint(),
                ((Comment) getItem(position)).getBody(),
                holder.tvChildContent.getLayoutParams().width - holder.tvChildContent.getPaddingLeft() - holder.tvChildContent.getPaddingRight()));
        // 마지막 인자는 TextView에서 글이 삽입되는 최대 width를 구함
        holder.tvChildContent.setFocusableInTouchMode(false);

        if(!userId.equals(holder.tvChildId.getText()))
            holder.llCommentBts.setVisibility(View.GONE);
        else{
            holder.llCommentBts.setVisibility(View.VISIBLE);

            holder.btChildEdit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.d("junyeong", this.toString() + " onClick : 수정모드로 바뀜");
                    editMode = COMMENT_MODIFY_MODE;
                    modifiedPos = pos;
                    final EditText etComment = (EditText) ((View) listView.getParent()).findViewById(R.id.etComment);
                    etComment.setText(holder.tvChildContent.getText());
                    etComment.setSelection(etComment.length());
                    etComment.requestFocus();
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_FORCED);

                }
            });
            holder.btChildRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);

                    alert.setTitle("정말 삭제하시겠습니까?");
                    alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            comment.remove(getItem(pos));
                            tvCountComment.setText(String.valueOf(comment.size()));
                            notifyDataSetChanged();
                            Common.setListViewHeightBasedOnChildren(listView);

                            dialog.dismiss();
                        }
                    });
                    alert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    Dialog dialog = alert.create();
                    dialog.show();
                }
            });
        }

        return convertView;
    }

    public void setEditMode(int editMode) {
        this.editMode = editMode;
    }

    public int getEditMode() {
        return editMode;
    }

    public int getModifiedPos() {
        return modifiedPos;
    }
    private static class Holder {
        public TextView tvChildId;
        public TextView tvChildDate;
        public TextView tvChildContent;
        public LinearLayout llCommentBts;
        public Button btChildEdit;
        public Button btChildRemove;
    }
}
*/
