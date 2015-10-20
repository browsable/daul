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
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

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

            /*Post.setIsWritten(true);
            Post.setTitle(etArticleTitle.getText().toString());
            Post.setContent(etArticleContent.getText().toString());
            Post.setDate(new SimpleDateFormat("yyyy/MM/dd").format(new Date(System.currentTimeMillis())));
            Post.setTime(new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis())));
            Post.setUserId("joyyir");*/

            /*FreeBoard.Data data = new FreeBoard.Data();
            data.setWhen(new SimpleDateFormat("MM.dd HH:mm").format(new Date(System.currentTimeMillis())));
            data.setBody(etArticleContent.getText().toString());
            data.setTitle(etArticleTitle.getText().toString());
            data.setAccount_no(921111);

            CommunityFragment.getInstance().getData().add(0, data);*/

            // 키보드 내리기
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etArticleTitle.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(etArticleContent.getWindowToken(), 0);

            initEditText();
            EventBus.getDefault().post(new ChangeFragEvent(CommunityFragment.class, "커뮤니티"));
        }
    }

    public WritePostFragment(){
        super(R.layout.fragment_write_article, "WritePostFragment");
        singleton = this;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = super.onCreateView(inflater, container, savedInstanceState);

        if (layoutId > 0) {
            EventBus.getDefault().post(new BackKeyEvent("WritePostFragment"));

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
