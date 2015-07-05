package com.daemin.schooltimetable;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.daemin.common.Common;
import com.daemin.schooltimetable.ShowSubFragment.MyCursorLoader;
import com.daemin.timetable.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SchoolTimetableFragment extends Fragment implements
LoaderManager.LoaderCallbacks<Cursor>{
	
	OnSendSametimestateListener SendSametimestateListener;
	
	
	public interface OnSendSametimestateListener {
        public void OnSendSametimestater(boolean sametime);
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
			//SchoolTimetableFragment fragment = new SchoolTimetableFragment();
			//fragment.setisPageOpen(false); 
			 try {
				 SendSametimestateListener = (OnSendSametimestateListener) activity;
		        } catch (ClassCastException e) {
		            throw new ClassCastException(activity.toString() + " must implement OnTimesSelectedListener");
		        }
			
	}


	View rootView;

	FrameLayout frame_sub;
	ScrollView scrollPage;
	
	Fragment fragment = null;
	boolean isPageOpen = false;
	boolean downstate = false;
	static boolean getUndo;
	static boolean sametime =false;
	int totalcredit;
	boolean clickstate;
	SharedPreferences pref;
	
	Animation translateLeftAnim;
	Animation translateRightAnim;
	
	
	//DB 
		DBAdapter myDb;
		static String[][] yourtimes;
		static String[] yoursubtitle;
		static String[] yourprof;
		static String[] yourcredit;
		static int rowcount; // yourschedule 테이블에서 쿼리의 결과로 얻어온 칼럼의 수
		static int[] _yid;
		ArrayList<String> selectedcredit;
		
	// Loader
		private MyCursorLoader cursorLoader;
		private static final int getAllDate = 0;
		private static final int getSubtitle = 1;
		private static final int searchSubtitle = 2;
		private static final int searchDepgradenum = 3;
		ArrayAdapter<String> adapter;
		public static LoaderManager mLoaderManager;
	
	// //capture layout
		LinearLayout schooltimetablelayout = null;
		Bitmap bm = null;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_schooltimetable,
				container, false);
		new MakeButton().execute();
		setHasOptionsMenu(true);//액션바 메뉴 허용
		
		 SharedPreferences pref = getActivity().getSharedPreferences("pref",Activity.MODE_PRIVATE); 
		 downstate = pref.getBoolean("downstate",false);
		 scrollPage = (ScrollView) rootView.findViewById(R.id.tab1);
		 frame_sub = (FrameLayout) rootView.findViewById(R.id.frame_container_sub);
		 totalcredit=0;
	     selectedcredit=new ArrayList<String>();
		
		if(downstate){
		
			openDB();
			ViewUpdate();
		}
		scrollPage.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
			@Override
			public void onSwipeLeft(){
					frame_sub.setVisibility(View.VISIBLE);
					isPageOpen= true;
			}
		});
		
		frame_sub.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
		
			@Override
			public void onSwipeRight() {
				frame_sub.setVisibility(View.GONE);
				isPageOpen= false;
			}
			
			
		});		
		
		
		if(!getUndo){ 
			frame_sub.setVisibility(View.GONE);
		}
		
		
		if(downstate){
			Bundle args = new Bundle();
			args.putInt("totalcredit", totalcredit);
			args.putStringArrayList("selectedcredit",selectedcredit);
			fragment = new SearchSubFragment();
			fragment.setArguments(args);

			if (fragment != null) {
				
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container_sub, fragment).commit();
				
			}
			getLoaderManager().initLoader(getAllDate, null, this);
			
		}else{
			Bundle args = new Bundle();
			args.putInt("totalcredit", totalcredit);
			args.putStringArrayList("selectedcredit",selectedcredit);
			fragment = new DBdownloadFragment();
			fragment.setArguments(args);

			if (fragment != null) {
				
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.frame_container_sub, fragment).commit();
				
			}
		}
		
	
		return rootView; 
	}

	public void GetSametimestate(){
		SendSametimestateListener.OnSendSametimestater(sametime); //ShowSub에서 list item 클릭시 겹침 상태인지를 체크해서 boolean 값인 sametime을 ShowSub로 보내줌  false 이면 먼저 DB를 화면에 업댓후 선택한 아이템을 위에 포개어 그림
	}

	public void ViewUpdate(){
		
		GetYourSheduleTimeCursorLoader GetYourSheduleTimeCursorLoader = new GetYourSheduleTimeCursorLoader(getActivity(),myDb);
		GetYourSheduleTimeCursorLoader.loadInBackground();
		
		for(int i = 0; i<rowcount; i++){
			selectedcredit.add(yourcredit[i]);
			totalcredit+=Integer.parseInt(yourcredit[i]);
			setTimes(yourtimes[i], null, false, _yid[i], yoursubtitle[i], yourprof[i]);
		}
		
	}
	

	//앱위젯에 사용할 캡처 함수
	private void screenshot() {
		frame_sub.setVisibility(View.GONE);
		scrollPage.scrollTo(0, 0);
//캡처	 
		schooltimetablelayout = (LinearLayout)rootView.findViewById(R.id.schooltimetablelayout);
		schooltimetablelayout.buildDrawingCache();
		schooltimetablelayout.setDrawingCacheEnabled(true);
		bm = schooltimetablelayout.getDrawingCache();
		
		try {
			File path = new File(Environment.getExternalStorageDirectory().toString() + "/timenuri/CaptureTime");

			if (!path.isDirectory()) {
				path.mkdirs();
			}

			FileOutputStream out = new FileOutputStream(
					Environment.getExternalStorageDirectory().toString() + "/timenuri/timetable.jpg");
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);

		} catch (FileNotFoundException e) {
			Log.d("FileNotFoundException:", e.getMessage());
		}
	}

	public void Downstate() {
		// TODO Auto-generated method stub
		openDB();
		ViewUpdate();
		getLoaderManager().initLoader(getAllDate, null, this);
	}
	public void openDB() {
		myDb = new DBAdapter(getActivity());
		myDb.open();
	}
	
	private void closeDB() {
	
		myDb.close();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(downstate) closeDB();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		screenshot();
		Intent intent = new Intent();
		//intent.setAction(android.appwidget.action.APPWIDGET_UPDATE);
		intent.setAction(Common.ACTION_UPDATE);
		getActivity().sendBroadcast(intent);
		
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		getLoaderManager().restartLoader(getAllDate, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		
		CursorLoader cursorloader= null; 
		
		switch(id){
		case 0:
			cursorloader = new GetYourSheduleTimeCursorLoader(getActivity(), myDb);
			break;
	
		}
		
			
		return cursorloader;
		
	}
	
	static class GetYourSheduleTimeCursorLoader extends CursorLoader {
		DBAdapter myDb;
	
	
		public GetYourSheduleTimeCursorLoader(Context context, DBAdapter myDb) {
			super(context);
			this.myDb = myDb;
		}

		public GetYourSheduleTimeCursorLoader(Context context) {
			// TODO Auto-generated constructor stub
			super(context);
			

		}

		public Cursor loadInBackground() {
			
			Cursor cursor = myDb.SearchYourScheduleTime();
			
			rowcount = 0;
			if (cursor != null) {
		   		cursor.moveToFirst();
		   		
		        while (!cursor.isAfterLast()){
		        
		        		rowcount++;
		        	
		        	  
		        	cursor.moveToNext();
		        } 
		     }
	
				_yid = new int[rowcount];
				yourcredit = new String[rowcount];
				yourtimes = new String[rowcount][16];
				yoursubtitle = new String[rowcount];
				yourprof = new String[rowcount];
				int i = 0;
			 	if (cursor != null) {
			   		cursor.moveToFirst();
			   		
			        while (!cursor.isAfterLast()){
			        	//times.add(cursor.getString(cursor.getColumnIndex("time1")));
			        	_yid[i] = cursor.getInt(cursor.getColumnIndex("_id"));
			        	yoursubtitle[i] = cursor.getString(cursor.getColumnIndex("subtitle"));
			        	yourprof[i] = cursor.getString(cursor.getColumnIndex("prof"));
			        	yourcredit[i] = cursor.getString(cursor.getColumnIndex("credit"));
			        	
			        	for(int j =0; j<16; j++){
			        		
				        	int a=j+1;
				        	//times[j] ="";
			        		yourtimes[i][j] = cursor.getString(cursor.getColumnIndex("time"+String.valueOf(a)));
			        	}
			        	   
			        	cursor.moveToNext();
			        	i++;
			        }
			 	}
			
			
			try {

				TimeUnit.SECONDS.sleep(0);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return cursor;
		}
		
	}
	  
	 
	//String[] color2 ={"#50FF0000","#807CFC00","#80FFA500","#8098FB89", "#80FFFF00","#8000FFFF", "#80008000","#80DDA0DD", "#800000FF","#99F0FFF0", "#8000008B","#80FFEBCD","#80800080","#99F5F5DC"};
	String[] color2 = {"#90FF3800","#F7E7CE","#FBCEB1","#FFE5B4","#CCCCFF","#AEC6CF","#ACE1AF","#8098FB89","#E5E4E2", "#DEB887"}; 
	//back key 눌렀을 때 초기화 	
	public void onBackPressed()
    {
		
        getUndo = false;
    }
	//과목 설정 프래그먼트에서 학부 Undo 버튼 클릭시 Schooltime에서 클린해줘야할 때 쓰는 함수 
	public void ViewClean(boolean undoclickstate , String[] oldtimes){ 
		
		getUndo = undoclickstate;
		if(oldtimes==null){ // 모든과목지우기
			for (int j = 0; j<rowcount; j++){
				oldtimes=yourtimes[j];
				int oldtimessize=0;
		 		for(int i = 0; i<16; i++){
		 			if(!oldtimes[i].equals(" ")){
		 				oldtimessize++;
		 			}
		 		}
		 		String[] oldtime = new String[oldtimessize];
		 		
				for(int i = 0; i < oldtimessize; i++){
			
					oldtime[i] = oldtimes[i];
				
				int resID = getResources().getIdentifier(oldtime[i],"id",getActivity().getPackageName());
	
				Button B  = (Button)rootView.findViewById(resID);	
				//B.setBackgroundColor(Color.parseColor(setColor));
				
				int index0 = oldtime[i].indexOf("00");
				int index1 = oldtime[i].indexOf("1300");
				int index2 = oldtime[i].indexOf("30");
	 			
				if(index0>-1){
					B.setBackgroundResource(R.drawable.xml_lower);
				}
				
		 		if(index2>-1)//30이 있을 경우
		 		{
		 			B.setBackgroundResource(R.drawable.xml_upper);
		 		}
		 		
		 		if(index1>-1){	//1300이 있는경우
	 				B.setBackgroundResource(R.drawable.xml_lower);
	 			}
	
	 			B.setText("");
				}
			}
		}
		else{ //특정 과목 지우기
			int oldtimessize=0;
		 		for(int i = 0; i<16; i++){
		 			if(!oldtimes[i].equals(" ")){
		 				oldtimessize++;
		 			}
		 		}
		 		String[] oldtime = new String[oldtimessize];
		 		
				for(int i = 0; i < oldtimessize; i++){
			
					oldtime[i] = oldtimes[i];
				
				int resID = getResources().getIdentifier(oldtime[i],"id",getActivity().getPackageName());
	
				Button B  = (Button)rootView.findViewById(resID);	
				//B.setBackgroundColor(Color.parseColor(setColor));
				
				int index0 = oldtime[i].indexOf("00");
				int index1 = oldtime[i].indexOf("1300");
				int index2 = oldtime[i].indexOf("30");
	 			
				if(index0>-1){
					B.setBackgroundResource(R.drawable.xml_lower);
				}
				
		 		if(index2>-1)//30이 있을 경우
		 		{
		 			B.setBackgroundResource(R.drawable.xml_upper);
		 		}
		 		
		 		if(index1>-1){	//1300이 있는경우
	 				B.setBackgroundResource(R.drawable.xml_lower);
	 			}
	
	 			B.setText("");
				}
			}
	}
	
	
	public void setTimes(String[] times, String[] oldtimes, boolean clickstate, int _qid, String subtitle, String prof) {
		
		if(sametime){ // 겹치는 시간 클릭했을 때 다른 과목 select 시 이벤트
			sametime=false;
		}
		
			int bre = 0;
			String setColor;
			String newsubtitle;
			//String newsubtitle2;
			int timessize=0;
	 		
			for(int i = 0; i<16; i++){
	 			if(!times[i].equals(" ")){
	 				timessize++;
	 			}
	 		} 
	 
			//과목 타임이 6개인 경우 (2,4), (4,2), (3,3), (6) 의 인덱스를 구하기 위한 변수 
			int time_4 =0;
			if(timessize%4==0){
		 		for(int j = 0; j<timessize; j++){
						if(times[0].substring(0, 3).equals(times[j].substring(0, 3))){
							time_4+=j;	 						
					}
				}
			}
			int time_6 =0;
			if(timessize%6==0){
		 		for(int j = 0; j<timessize; j++){
						if(times[0].substring(0, 3).equals(times[j].substring(0, 3))){
							time_6+=j;	 						
					}
				}
			}
			
			if(_qid==-1){
				setColor ="#00FF00";
				
				for(int k=0; k<rowcount; k++){ // yourtimes와 선택한 times를 비교하여 
	 				for(int j=0; j<16; j++){
	 					for(int z=0; z<timessize; z++){
	 						if(!times[z].equals(" ")){
	 							if(yourtimes[k][j].equals(times[z])){
	 								setColor ="#FF0000";
	 								int resID = getResources().getIdentifier(times[z],"id",getActivity().getPackageName());
	 							 	
	 				  				Button B  = (Button)rootView.findViewById(resID);	
	 				 				B.setBackgroundColor(Color.parseColor(setColor));
	 				 				sametime = true;
	 				 				bre=1;
	 				 				break;
	 							}
	 						}
	 					}
	 					if(bre==1) break;
	 				}
	 				if(bre==1) break;
	 			}
	 			
		
			}else{
				setColor = color2[_qid%10];
			}
			
			//과목 타이틀 글자수가 6 이하면 자르지 않고 6 이상이면 반으로 짤라서 디스플레이
			if(subtitle.length()<6)
	 		{
	 			newsubtitle = subtitle;
	 		}
	 		else
	 		{	
	 			//newsubtitle0 = subtitle.substring(0, subtitle.length()/2) + "/n"+subtitle.substring(subtitle.length()/2, subtitle.length());
	 			newsubtitle = subtitle.substring(0, 5);
	 			//newsubtitle2 = subtitle.substring(subtitle.length()/2, subtitle.length());
	 		}	 
			
			//subtitle.substring(5, subtitle.length());
		
	 	
	 		if(clickstate){
	 			if(oldtimes!=null){
	 				int oldtimessize=0;
		 	 		for(int i = 0; i<16; i++){
		 	 			if(!oldtimes[i].equals(" ")){
		 	 				oldtimessize++;
		 	 			}
		 	 		}
		 	 			
		 	 		//	String[] oldtime = new String[oldtimessize];
			 	 		
			 			for(int i = 0; i < oldtimessize; i++){
			 		
			 				//oldtime[i] = oldtimes[i];
			 			
							int resID = getResources().getIdentifier(oldtimes[i],"id",getActivity().getPackageName());
				
							Button B  = (Button)rootView.findViewById(resID);	
							//B.setBackgroundColor(Color.parseColor(setColor));
							
							int index0 = oldtimes[i].indexOf("00");
							int index1 = oldtimes[i].indexOf("1300");
							int index2 = oldtimes[i].indexOf("30");
				 			
							if(index0>-1){
								B.setBackgroundResource(R.drawable.xml_lower);
							}
							
					 		if(index2>-1)//30이 있을 경우
					 		{
					 			B.setBackgroundResource(R.drawable.xml_upper);
					 		}
					 		
					 		if(index1>-1){	//1300이 있는경우
				 				B.setBackgroundResource(R.drawable.xml_lower);
				 			}
			
				 			B.setText("");
			 			
			 			}
		 			}
		 		}
	 			 
	 		
	 		for(int i = 0; i<timessize; i++){
	 				 	
		  				//time[i] = times[i];
	 					
		  				//Toast.makeText(getActivity(),times[i],Toast.LENGTH_SHORT).show();
		  				int resID = getResources().getIdentifier(times[i],"id",getActivity().getPackageName());
		  				
		  				Button B  = (Button)rootView.findViewById(resID);	
		 				B.setBackgroundColor(Color.parseColor(setColor));
		 				B.setTextSize(9);
	 					B.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
		 				
		 				if(timessize%2==0){
			 				if(i%2==0){
			 					B.setText(newsubtitle); 					
			 				}else if(i%2==1){
			 					B.setText(prof);
			 				}
		 				}
		 				if(timessize%3==0){
			 				if(i%3==0){
			 					B.setText(newsubtitle); 					
			 				}else if(i%3==1){
			 					B.setTextSize(8);
			 					B.setText(prof);
			 				}else if(i%3==2){
			 					B.setText("");
			 				}
		 				}
		 				if(timessize%4==0){
		 					switch(time_4){
		 						case 1:
		 							if(i%4==0){
		 								B.setText(newsubtitle);
					 				}else if(i%4==1){
					 					B.setTextSize(8);
					 					B.setText(prof);
					 				}else if(i%4==2){
					 					B.setText(newsubtitle);
					 				}else if(i%4==3){
					 					B.setTextSize(8);
					 					B.setText(prof);
					 				}
		 							break;
		 						case 6:
		 							if(i%4==0){
					 					B.setText("");
					 				}else if(i%4==1){
					 					B.setText(newsubtitle);
					 				}else if(i%4==2){
					 					B.setTextSize(8);
					 					B.setText(prof);
					 				}else if(i%4==3){
					 					B.setText("");
					 				}
		 							break;
		 					}
		 				}
		 			
		 				if(timessize%6==0){
		 					switch(time_6){
		 						case 1:
		 							if(i%6==0){
					 					B.setText(newsubtitle);
					 				}
		 							else if(i%6==1){
		 								B.setTextSize(8);
					 					B.setText(prof);
					 				
					 				}else if(i%6==2){
					 					B.setText("");
					 				}else if(i%6==3){
					 					B.setText(newsubtitle);
					 					
					 				}else if(i%6==4){
					 					B.setTextSize(8);
					 					B.setText(prof);
					 				
					 				}else if(i%6==5){
					 					B.setText("");
					 				}
		 							break;
		 						case 6:
		 							if(i%6==0){
					 					B.setText("");
					 				}
		 							else if(i%6==1){
		 								B.setText(newsubtitle);
					 				
					 				}else if(i%6==2){
					 					B.setTextSize(8);
					 					B.setText(prof);
					 				}else if(i%6==3){
					 					B.setText("");
					 					
					 				}else if(i%6==4){
					 					B.setText(newsubtitle);
					 				
					 				}else if(i%6==5){
					 					B.setTextSize(8);
					 					B.setText(prof);
					 				}
		 							break;
		 						case 15:
		 							if(i%6==0){
					 					B.setText(newsubtitle);
					 				}
		 							else if(i%6==1){
		 								B.setTextSize(8);
					 					B.setText(prof);
					 				
					 				}else if(i%6==2){
					 					B.setText("");
					 				}else if(i%6==3){
					 					B.setText("");
					 					
					 				}else if(i%6==4){
					 					B.setText(newsubtitle);
					 				
					 				}else if(i%6==5){
					 					B.setTextSize(8);
					 					B.setText(prof);	
					 				}
		 							break;
		 					}
		 					
		 					
		 				}
		 				
		 				if(timessize%8==0){
			 				if(i%8==0){
			 					B.setText("");
			 				}else if(i%8==1){
			 					B.setText(newsubtitle);
			 				}else if(i%8==2){
			 					B.setTextSize(8);
			 					B.setText(prof);
			 				}else if(i%8==3){
			 					B.setText("");
			 				}else if(i%8==4){
			 					B.setText(""); 					
			 				}else if(i%8==5){
			 					B.setText(newsubtitle);
			 				}else if(i%8==6){
			 					B.setTextSize(8);
			 					B.setText(prof);
			 				}else if(i%8==7){
			 					B.setText("");
			 				}
		 				}
	 				}
	
	}

	/*//엑션바 설정
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		menu.clear();
		inflater.inflate(R.menu.schooltimetablefragment_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		 switch (item.getItemId()) {
		    // action with ID action_refresh was selected
		 	case R.id.action_menu:
		      return false;
		 	case R.id.schooltimetablefragment_menu:
					if (isPageOpen || getUndo) {
							frame_sub.setVisibility(View.GONE);
							isPageOpen = false;
							getUndo = false;
		
						} else {
							frame_sub.setVisibility(View.VISIBLE);
							isPageOpen = true;
						}
		 		
		 		return true; 
		    default:
		      break;
		    }
		    return false;
	}
*/
	
	
class MakeButton extends AsyncTask<String, String, String> implements OnClickListener {
		Button button;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		
		protected void onPreExecute() {
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			
			String[] timename = getResources().getStringArray(R.array.timename);
			for(int i=0; i<timename.length; i++){
				int resID = getResources().getIdentifier(timename[i],"id",getActivity().getPackageName());
				button = (Button) rootView.findViewById(resID);
				button.setOnClickListener(this);
			}
			
			return null;
		}

		protected void onPostExecute(String file_url) {
		
		}

		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.mon0800:
				button = (Button) rootView.findViewById(R.id.mon0800);
				button.setBackgroundColor(Color.parseColor("#FF0000"));
				break;
			default:
				/*Toast.makeText(getActivity(), "default", Toast.LENGTH_SHORT)
				.show();*/
				break;

			}
		}
		
	}

}
	

