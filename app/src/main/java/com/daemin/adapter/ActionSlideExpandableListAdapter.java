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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daemin.common.Common;
import com.daemin.common.CurrentTime;
import com.daemin.community.Comment;
import com.daemin.community.github.FreeBoard;
import com.daemin.timetable.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun-yeong on 2015-07-07.
 */
public class ActionSlideExpandableListAdapter extends BaseAdapter {
    private List<FreeBoard.Data> data;
    private List<Comment> comment;
    private String userId;
    private int userAccountNum;
    private List<Integer> expandCollapseList;
    private Activity activity;
    public final int COLLAPSED = 0;
    public final int EXPANDED = 1;
    LayoutInflater inflater;
    CommentListAdapter commentListAdapter;
    public ActionSlideExpandableListAdapter(List<FreeBoard.Data> data, String userId, int userAccountNum, Activity activity) {
        this.data = data;
        this.userId = userId;
        this.userAccountNum = userAccountNum;
        this.activity = activity;
        this.expandCollapseList = new ArrayList<>();
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        comment = new ArrayList<>();
        comment.add(new Comment(1, "애자일 소프트웨어 개발(Agile software development) 혹은 애자일 개발 프로세스는 " +
                "소프트웨어 엔지니어링에 대한 개념적인 얼개로, 프로젝트의 생명주기동안 반복적인 개발을 촉진한다. " +
                "최근에는 애자일 게임 보급 등의 여파로 소프트웨어 엔지니어링 뿐 아니라 다양한 전문 분야에서 실용주의적 " +
                "사고를 가진 사람들이 애자일 방법론을 적용하려는 시도를 하고 있다.", "07.07 21:00", "joyyir"));
        comment.add(new Comment(2, "반가워요 호호호호", "07.07 22:00", "skyrocket"));
        comment.add(new Comment(3, "이거 좋아요!", "07.07 23:00", "fade2black"));
        comment.add(new Comment(4, "ㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱ", "08.06 16:00", "baboda"));

        for(int i = 0; i < data.size(); i++)
            expandCollapseList.add(i, COLLAPSED);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.listitem_community_post, parent, false);
            holder.tvGroupTitle = (TextView) convertView.findViewById(R.id.tvGroupTitle);
            holder.tvGroupTime = (TextView) convertView.findViewById(R.id.tvGroupTime);
            holder.tvGroupId = (TextView) convertView.findViewById(R.id.tvGroupId);
            holder.tvGroupContent = (TextView) convertView.findViewById(R.id.tvGroupContent);
            holder.tvCountComment = (TextView) convertView.findViewById(R.id.tvCountComment);
            holder.expandable_arrow = (ImageView) convertView.findViewById(R.id.expandable_arrow);
            holder.llButtonGroup = (LinearLayout) convertView.findViewById(R.id.llButtonGroup);
            holder.btEditComment = (Button) convertView.findViewById(R.id.btEditComment);
            holder.etComment = (EditText) convertView.findViewById(R.id.etComment);
            holder.lvComment = (ListView) convertView.findViewById(R.id.lvComment);
            commentListAdapter = new CommentListAdapter(comment, userId, holder.lvComment, holder.tvCountComment, activity);
            holder.lvComment.setAdapter(commentListAdapter);
            Common.setListViewHeightBasedOnChildren(holder.lvComment);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvGroupTitle.setText(((FreeBoard.Data) getItem(position)).getTitle());
        holder.tvGroupTime.setText(((FreeBoard.Data) getItem(position)).getDate() + ' ' + ((FreeBoard.Data) getItem(position)).getTime());
        holder.tvGroupId.setText(String.valueOf(((FreeBoard.Data) getItem(position)).getNickname()));
        holder.tvGroupContent.setText(((FreeBoard.Data) getItem(position)).getBody_t());
        holder.tvCountComment.setText(String.valueOf(comment.size()));
        holder.etComment.requestFocus();

        if (expandCollapseList.get(position) == EXPANDED)
            holder.expandable_arrow.setImageResource(R.drawable.ic_action_collapse);
        else
            holder.expandable_arrow.setImageResource(R.drawable.ic_action_expand);

        if (userAccountNum != ((FreeBoard.Data) getItem(position)).getAccount_no())
            holder.llButtonGroup.setVisibility(View.INVISIBLE);
        else
            holder.llButtonGroup.setVisibility(View.VISIBLE);



        holder.btEditComment.setOnClickListener(new View.OnClickListener() {
            String commentContent;
            @Override
            public void onClick(View v) {
                commentContent = holder.etComment.getText().toString();
                if (commentContent.length() <= 0) {
                    Toast.makeText(context, "댓글 내용을 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (commentListAdapter.getEditMode() == commentListAdapter.COMMENT_ADD_MODE) {
                        Log.d("junyeong", "삽입 모드");
                        comment.add(new Comment(0, commentContent, CurrentTime.getMD(), userId));

                    } else if (commentListAdapter.getEditMode() == commentListAdapter.COMMENT_MODIFY_MODE) {
                        Log.d("junyeong", "수정 모드");
                        comment.set(commentListAdapter.getModifiedPos(),
                                new Comment(0, commentContent, CurrentTime.getMD(), userId));
                        commentListAdapter.setEditMode(commentListAdapter.COMMENT_ADD_MODE);
                    }
                }
                holder.etComment.setText("");
                holder.tvCountComment.setText(String.valueOf(comment.size()));
                notifyDataSetChanged();
                commentListAdapter.notifyDataSetChanged();
                Common.setListViewHeightBasedOnChildren(holder.lvComment);
                // 키보드 내리기
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(holder.etComment.getWindowToken(), 0);
                holder.etComment.requestFocus();

            }
        });
        return convertView;
    }

    public void setExpandCollapseList(int position, int expandOrCollapse){
        expandCollapseList.set(position, expandOrCollapse);
    }
    private static class Holder {
        public TextView tvGroupTitle;
        public TextView tvGroupTime;
        public TextView tvGroupId;
        public TextView tvGroupContent;
        public TextView tvCountComment;
        public ImageView expandable_arrow;
        public LinearLayout llButtonGroup;
        public Button btEditComment;
        public ListView lvComment;
        public EditText etComment;
    }
}


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
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Holder holder;
        if(convertView == null){
            holder = new Holder();
            convertView = inflater.inflate(R.layout.listitem_community_comment, parent, false);
            holder.tvChildId = (TextView) convertView.findViewById(R.id.tvChildId);
            holder.tvChildDate = (TextView) convertView.findViewById(R.id.tvChildDate);
            holder.tvChildContent = (TextView) convertView.findViewById(R.id.tvChildContent);
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

                    EditText etComment = (EditText) ((View) listView.getParent()).findViewById(R.id.etComment);

                    etComment.setText(holder.tvChildContent.getText());
                    etComment.setSelection(etComment.length());
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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