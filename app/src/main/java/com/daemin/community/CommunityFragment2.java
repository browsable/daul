package com.daemin.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.daemin.adapter.CommunityListAdapter2;
import com.daemin.common.BasicFragment;
import com.daemin.data.CommentData;
import com.daemin.data.PostData;
import com.daemin.event.SetExpandableEvent;
import com.daemin.timetable.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class CommunityFragment2 extends BasicFragment {
    CommunityListAdapter2 listAdapter;
    ExpandableListView expListView;
    List<PostData.Data> listPostData;
    HashMap<String, List<CommentData>> listCommentData;
    List comment1,comment2;
    public CommunityFragment2() {
        super(R.layout.fragment_community2, "CommunityFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        EventBus.getDefault().register(this);
        // get the listview
        expListView = (ExpandableListView) root.findViewById(R.id.lvExp);
        //expListView.setGroupIndicator(null);
        // preparing list data
        prepareListData();
        listAdapter = new CommunityListAdapter2(getActivity(), listPostData, listCommentData);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setGroupIndicator(null);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getActivity(),
                        listPostData.get(groupPosition)
                                + " : "
                                + listCommentData.get(
                                listPostData.get(groupPosition).getNickname()).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
        return root;
    }
    private void prepareListData() {
        listPostData = new ArrayList<>();
        listPostData.add(new PostData.Data(
                0, 0, "nick", "null", "하하1", 0, 0, 0, "2015.10.22", "11:20", 0, "내용test", ""));
        listPostData.add(new PostData.Data(
                0, 0, "daul", "null", "하하2", 0, 0, 0, "2015.10.22", "11:24", 0, "내용adsfasdf", ""));
        listCommentData = new HashMap<>();
        comment1= new ArrayList<>();
        comment1.add(new CommentData(0, "내용1", "2015.10.22", "daul"));
        comment1.add(new CommentData(1, "내용2", "2015.10.22", "koreatech"));
        comment2= new ArrayList<>();
        comment2.add(new CommentData(0, "댓글3", "2015.10.22", "dl"));
        comment2.add(new CommentData(1, "댓글4", "2015.10.22", "ktech"));
        comment2.add(new CommentData(1, "댓글4", "2015.10.22", "ktech"));

        listCommentData.put(listPostData.get(0).getNickname(), comment1);
        listCommentData.put(listPostData.get(1).getNickname(), comment2);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(SetExpandableEvent e){
        if(expListView.isGroupExpanded(1))
            expListView.collapseGroup(1);
        else
            expListView.expandGroup(1);
    }
}