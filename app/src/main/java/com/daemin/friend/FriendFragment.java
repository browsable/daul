package com.daemin.friend;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.daemin.adapter.FriendListAdapter;
import com.daemin.common.BasicFragment;
import com.daemin.data.FriendData;
import com.daemin.event.SetExpandableEvent;
import com.daemin.timetable.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendFragment extends BasicFragment {

    FriendListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<FriendData>> listDataChild;
    ArrayList<FriendData> favoriteList,groupList,friendList;
    public FriendFragment() {
        super(R.layout.fragment_friend, "FriendFragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!EventBus.getDefault().isRegistered(this))EventBus.getDefault().register(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        // get the listview
        expListView = (ExpandableListView) root.findViewById(R.id.lvExp);
        expListView.setGroupIndicator(null);
        // preparing list data
        prepareListData();
        listAdapter = new FriendListAdapter(getActivity(), listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(0);
        expListView.expandGroup(1);
        expListView.expandGroup(2);

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getActivity(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
        return root;
    }
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        // Adding child data
        listDataHeader.add("즐겨찾기");
        listDataHeader.add("그룹");
        listDataHeader.add("친구");
        favoriteList = new ArrayList<>();
        // Adding child data
        groupList = new ArrayList<>();
        groupList.add(new FriendData(0, "01052998236", "DAUL"));
        groupList.add(new FriendData(0, "01052998236", "고밥"));
        groupList.add(new FriendData(0,"01052998236","LINC사업단"));
        friendList = new ArrayList<>();
        friendList = getContactList();
        listDataChild.put(listDataHeader.get(0), favoriteList); // Header, Child data
        listDataChild.put(listDataHeader.get(1), groupList);
        listDataChild.put(listDataHeader.get(2), friendList);
    }
    private ArrayList<FriendData> getContactList() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID, // 연락처 ID -> 사진 정보
                // 가져오는데 사용
                ContactsContract.CommonDataKinds.Phone.NUMBER, // 연락처
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}; // 연락처
        // 이름.
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME

                + " COLLATE LOCALIZED ASC";
        @SuppressWarnings("deprecation")
        Cursor contactCursor = getActivity().managedQuery(uri, projection, null,
                selectionArgs, sortOrder);
        ArrayList<FriendData> contactlist = new ArrayList<FriendData>();
        if (contactCursor.moveToFirst()) {
            do {
                String phonenumber = contactCursor.getString(1).replaceAll("-",
                        "");
                if (phonenumber.length() == 10) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 6) + "-"
                            + phonenumber.substring(6);
                } else if (phonenumber.length() > 8) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 7) + "-"
                            + phonenumber.substring(7);
                }
                FriendData acontact = new FriendData();
                acontact.setPhotoid(contactCursor.getLong(0));
                acontact.setPhonenum(phonenumber);
                acontact.setName(contactCursor.getString(2));
                contactlist.add(acontact);
            } while (contactCursor.moveToNext());
        }
        return contactlist;
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