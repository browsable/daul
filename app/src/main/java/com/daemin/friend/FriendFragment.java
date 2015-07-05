package com.daemin.friend;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.daemin.common.BasicFragment;
import com.daemin.timetable.R;

import java.util.ArrayList;
public class FriendFragment extends BasicFragment {
    
	
	public FriendFragment() {
		super(R.layout.fragment_friend, "AreaFragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = super.onCreateView(inflater, container, savedInstanceState);
		if (layoutId > 0) {
			 
	        // * 데이터 원본 준비
	        ArrayList<ContactEntity> arContactList = new ArrayList<ContactEntity>();        
	        arContactList = getContactList();
	        ArrayList<String> arGeneral = new ArrayList<String>();
	        for( int i = 0 ; i < arContactList.size() ; i++ )
	        {
	            arGeneral.add( arContactList.get(i).getName());
	            //arGeneral.add( arContactList.get(i).phonenum);
	        }
	        // */
	 
	        /*
	         * 배열로 준비 String[] arGeneral = {"김유신", "이순신", "강감찬", "을지문덕"}; //
	         */
	 
	        // 어댑터 준비
	        ArrayAdapter<String> Adapter;
	        Adapter = new ArrayAdapter<String>(getActivity(),
	                android.R.layout.simple_list_item_1, arGeneral);
	 
	        // 어댑터 연결
	        ListView list = (ListView) root.findViewById(R.id.list);
	        list.setAdapter(Adapter);
		}
		return root;
	   
    }
 
    private ArrayList<ContactEntity> getContactList() {
 
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
 
        String[] projection = new String[] {
 
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID, // 연락처 ID -> 사진 정보
                                                            // 가져오는데 사용
 
                ContactsContract.CommonDataKinds.Phone.NUMBER, // 연락처
 
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME }; // 연락처
                                                                        // 이름.
 
        String[] selectionArgs = null;
 
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
 
        + " COLLATE LOCALIZED ASC";
 
        @SuppressWarnings("deprecation")
		Cursor contactCursor = getActivity().managedQuery(uri, projection, null,
 
        selectionArgs, sortOrder);
 
        ArrayList<ContactEntity> contactlist = new ArrayList<ContactEntity>();
 
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
 
                ContactEntity acontact = new ContactEntity();
 
                acontact.setPhotoid(contactCursor.getLong(0));
 
                acontact.setPhonenum(phonenumber);
 
                acontact.setName(contactCursor.getString(2));
 
                contactlist.add(acontact);
 
            } while (contactCursor.moveToNext());
 
        }
 
        return contactlist;
 
    }
 
}