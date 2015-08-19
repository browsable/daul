package com.daemin.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daemin.common.Common;
import com.daemin.community.Comment;
import com.daemin.community.github.FreeBoard;
import com.daemin.timetable.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun-yeong on 2015-07-07.
 */
public class ActionSlideExpandableListAdapter extends BaseAdapter {
    private List<FreeBoard.Data> data;
    private List<Comment> comment;
    private String userId;
    private List<Integer> expandCollapseList;
    private Activity activity;
    public final int COLLAPSED = 0;
    public final int EXPANDED = 1;

    public ActionSlideExpandableListAdapter(List<FreeBoard.Data> data, String userId, Activity activity) {
        this.data = data;
        this.userId = userId;
        this.expandCollapseList = new ArrayList<>();
        this.activity = activity;

        FreeBoard.Data sampleData = new FreeBoard.Data();
        sampleData.setTitle("연결고리#힙합");
        sampleData.setWhen("2015.08.07 12:32");
        sampleData.setAccount_no(1111);
        sampleData.setBody("너와 나의 연결고리 이건 우리안의 소리! 너와 나의 연결고리 이건 우리안의 소리! 너와 나의 연결고리 이건 우리안의 소리! 너와 나의 연결고리 이건 우리안의 소리!");
        data.add(0, sampleData);

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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_expandable, parent, false);
        }

        TextView tvGroupTitle = (TextView) convertView.findViewById(R.id.tvGroupTitle);
        TextView tvGroupTime = (TextView) convertView.findViewById(R.id.tvGroupTime);
        TextView tvGroupId = (TextView) convertView.findViewById(R.id.tvGroupId);
        TextView tvGroupContent = (TextView) convertView.findViewById(R.id.tvGroupContent);
        final TextView tvCountComment = (TextView) convertView.findViewById(R.id.tvCountComment);
        ImageView expandable_arrow = (ImageView) convertView.findViewById(R.id.expandable_arrow);

        tvGroupTitle.setText(((FreeBoard.Data) getItem(position)).getTitle());
        tvGroupTime.setText(((FreeBoard.Data) getItem(position)).getWhen());
        tvGroupId.setText(String.valueOf(((FreeBoard.Data) getItem(position)).getAccount_no()));
        tvGroupContent.setText(((FreeBoard.Data) getItem(position)).getBody());
        tvCountComment.setText(String.valueOf(comment.size()));
        if(expandCollapseList.get(position) == EXPANDED)
            expandable_arrow.setImageResource(R.drawable.ic_action_collapse);
        else
            expandable_arrow.setImageResource(R.drawable.ic_action_expand);

        final ListView lvComment = (ListView) convertView.findViewById(R.id.lvComment);
        final CommentListAdapter commentListAdapter = new CommentListAdapter(comment, userId, lvComment, tvCountComment, activity);
        lvComment.setAdapter(commentListAdapter);
        Common.setListViewHeightBasedOnChildren(lvComment);

        Button btEditComment = (Button) convertView.findViewById(R.id.btEditComment);
        final EditText etComment = (EditText) convertView.findViewById(R.id.etComment);
        btEditComment.setOnClickListener(new View.OnClickListener() {
            String commentContent;

            @Override
            public void onClick(View v) {
                commentContent = etComment.getText().toString();

                if (commentContent.length() <= 0) {
                    Toast.makeText(context, "댓글 내용을 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    comment.add(new Comment(0, commentContent, new SimpleDateFormat("MM.dd HH:mm").format(new Date(System.currentTimeMillis())), userId));
                    etComment.setText("");
                    tvCountComment.setText(String.valueOf(comment.size()));
                    notifyDataSetChanged();
                    Common.setListViewHeightBasedOnChildren(lvComment);
                }
            }
        });

        return convertView;
    }

    public void setExpandCollapseList(int position, int expandOrCollapse){
        expandCollapseList.set(position, expandOrCollapse);
    }
}


class CommentListAdapter extends BaseAdapter{
    private List<Comment> comment;
    private String userId;
    private ListView listView;
    private TextView tvCountComment;
    private Activity activity;

    public CommentListAdapter(List<Comment> comment, String userId, ListView listView, TextView tvCountComment, Activity activity) {
        this.comment = comment;
        this.userId = userId;
        this.listView = listView;
        this.tvCountComment = tvCountComment;
        this.activity = activity;
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

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_child, parent, false);
        }

        TextView tvChildId = (TextView) convertView.findViewById(R.id.tvChildId);
        TextView tvChildDate = (TextView) convertView.findViewById(R.id.tvChildDate);
        EditText tvChildContent = (EditText) convertView.findViewById(R.id.tvChildContent);
        LinearLayout llCommentBts = (LinearLayout) convertView.findViewById(R.id.llCommentBts);

        tvChildId.setText(((Comment) getItem(position)).getUserId());
        tvChildDate.setText(((Comment) getItem(position)).getDate());
        tvChildContent.setText(Common.breakText(tvChildContent.getPaint(),
                ((Comment) getItem(position)).getBody(),
                tvChildContent.getLayoutParams().width - tvChildContent.getPaddingLeft() - tvChildContent.getPaddingRight()));
                // 마지막 인자는 TextView에서 글이 삽입되는 최대 width를 구함
        tvChildContent.setFocusableInTouchMode(false);

        if(userId != tvChildId.getText())
            llCommentBts.setVisibility(View.GONE);
        else{
            llCommentBts.setVisibility(View.VISIBLE);

            Button btChildEdit = (Button) convertView.findViewById(R.id.btChildEdit);
            Button btChildRemove = (Button) convertView.findViewById(R.id.btChildRemove);

            btChildRemove.setOnClickListener(new View.OnClickListener() {
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
}