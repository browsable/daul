package com.daemin.community;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daemin.common.BasicFragment;
import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Jun-yeong on 2015-08-09.
 */
public class WritePostFragment extends BasicFragment {
    private static WritePostFragment singleton;
    private View root;
    private Button btWriteArticle;
    private EditText etArticleTitle, etArticleContent;
    private class mOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (etArticleTitle.getText().length() <= 0){
                Toast.makeText(getActivity(), "제목을 입력하세요", Toast.LENGTH_LONG).show();
                return;
            }
            else if(etArticleContent.getText().length() <= 0){
                Toast.makeText(getActivity(), "내용을 입력하세요", Toast.LENGTH_LONG).show();
                return;
            }

            Post.setIsWritten(true);
            Post.setTitle(etArticleTitle.getText().toString());
            Post.setContent(etArticleContent.getText().toString());
            Post.setDate(new SimpleDateFormat("MM.dd HH:mm").format(new Date(System.currentTimeMillis())));
            Post.setUserId("joyyir");

            /*FreeBoard.Data data = new FreeBoard.Data();
            data.setWhen(new SimpleDateFormat("MM.dd HH:mm").format(new Date(System.currentTimeMillis())));
            data.setBody(etArticleContent.getText().toString());
            data.setTitle(etArticleTitle.getText().toString());
            data.setAccount_no(921111);

            CommunityFragment2.getInstance().getData().add(0, data);*/

            // 키보드 내리기
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etArticleTitle.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(etArticleContent.getWindowToken(), 0);

            initEditText();
            SubMainActivity.getInstance().changeFragment(CommunityFragment2.class, "커뮤니티", R.color.orange);
        }
    }

    public WritePostFragment(){
        super(R.layout.fragment_write_article, "WritePostFragment");
        singleton = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = super.onCreateView(inflater, container, savedInstanceState);

        if (layoutId > 0) {
            SubMainActivity.getInstance().setBackKeyName("WritePostFragment");

            etArticleTitle = (EditText) root.findViewById(R.id.etArticleTitle);
            etArticleContent = (EditText) root.findViewById(R.id.etArticleContent);
            btWriteArticle = (Button) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.btWriteArticle);

            btWriteArticle.setText("확인");
            btWriteArticle.setOnClickListener(new mOnClick());

        }

        return root;
    }

    public void initEditText() {
        etArticleTitle.setText("");
        etArticleContent.setText("");
    }

    public static WritePostFragment getInstance(){
        return singleton;
    }
}
