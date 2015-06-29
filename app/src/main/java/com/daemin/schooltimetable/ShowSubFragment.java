package com.daemin.schooltimetable;


import java.util.*;
import java.util.concurrent.*;

import com.daemin.timetable.*;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.database.*;
import android.graphics.*;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import android.widget.AdapterView.*;

//공학부 검색시 목록을 리스트에 뿌려주는 프래그먼트
public class ShowSubFragment extends Fragment implements
LoaderManager.LoaderCallbacks<Cursor> {
	
	OnTimesSelectedListener mListener;
	OnViewCleanListener ViewCleanListener;
	OnViewUpdateListener ViewUpdateListener;
	OnGetSametimestateListener SametimestateListener;
	
	public interface OnTimesSelectedListener {
        public void onTimesSelected(String[] times, String[] oldtimes, boolean clickstate, int _qid, String subtitle,  String prof);
	}
	public interface OnViewCleanListener {
        public void OnViewClean(boolean undoclickstate, String[] oldtimes); // oldtimes 가 null이면 화면 전부 다지움, times이면 최근것 지움
	}
	public interface OnViewUpdateListener {
        public void OnViewUpdate();
	}
	public interface OnGetSametimestateListener {
        public void GetSametimestateListener();
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
			//SchoolTimetableFragment fragment = new SchoolTimetableFragment();
			//fragment.setisPageOpen(false); 
			 try {
		            mListener = (OnTimesSelectedListener) activity;
		            ViewCleanListener = (OnViewCleanListener) activity;
		            ViewUpdateListener = (OnViewUpdateListener) activity;
		            SametimestateListener = (OnGetSametimestateListener) activity;
		        } catch (ClassCastException e) {
		            throw new ClassCastException(activity.toString() + " must implement OnTimesSelectedListener");
		        }
			
	}


	View rootView;
	Button btnUndo1;
	Button btnUndo2;
	Button btnSave;
	Button btnCancel;
	Button btnRefresh;
	Button btnAdd;
	
	TextView setOverlap;
	TextView showdepname;
	TextView setCredit;
	
	// DB part
	DBAdapter myDb;

	SimpleCursorAdapter mAdapter;

	static String olddetaildep;
	static String detaildep;
	static String kordepname;
	static String detailsubtitle;
	static String depgradenum;

	private MyCursorLoader cursorLoader;
	static ArrayList<String> items; // 학부검색시 과목리스트
	static String[] times; // 과목선택시 과목 배정시간
	static String[] oldtimes;
	//static int _qid;
	ArrayAdapter<CharSequence> spinneradapter;
	
	// Loader
	private static final int getAllDate = 0;
	private static final int getSubtitle = 1;
	private static final int searchSubtitle = 2;
	private static final int searchDepgradenum = 3;
	ArrayAdapter<String> adapter;
	public static LoaderManager mLoaderManager;
	
	
	AutoCompleteTextView actv;
	Fragment fragment=null;
	static String _id;
	static String subtitle;
	static String prof;
	static String selectcredit="0";
	int totalcredit;
	int samestatenum = 0; //리스트에서 중복되는 시간을 연속으로 두번 클릭하는 경우 시간표가 지워지는 것을 방지 
	
	static boolean clickstate=false;// 과목 리스트를 클릭한 상태인지 아닌지 
	static boolean samestate = true; //겹치는 과목 상태
	ArrayList<String> selectedcredit;
	
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_showsub, null);
		
		return rootView;
	}


	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onActivityCreated(savedInstanceState);
		
		
		selectedcredit=new ArrayList<String>();
		selectedcredit = getArguments().getStringArrayList("selectedcredit");
		olddetaildep = getArguments().getString("olddetaildep");
		detaildep = getArguments().getString("detaildep");
		kordepname = getArguments().getString("kordepname");
		totalcredit = getArguments().getInt("totalcredit");
		
		showdepname = (TextView) rootView.findViewById(R.id.showdepname);
		setCredit = (TextView) rootView.findViewById(R.id.setCredit);
		setCredit.setText(String.valueOf(totalcredit));
		setOverlap = (TextView) rootView.findViewById(R.id.setOverlap);
		
		showdepname.setText(olddetaildep); 
		btnUndo1 = (Button) rootView.findViewById(R.id.btnUndo1);
		btnUndo2 = (Button) rootView.findViewById(R.id.btnUndo2);
		btnSave = (Button) rootView.findViewById(R.id.btnSave);
		btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
		btnRefresh = (Button) rootView.findViewById(R.id.btnRefresh);
		btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
		
		
		
		openDB();
		
		SimpleAdapterSet(null);
		
		
		//getLoaderManager().initLoader(2,null,this);
		SubTitleCursorLoader SubTitleCursorLoader = new SubTitleCursorLoader(getActivity(),myDb);
		SubTitleCursorLoader.loadInBackground();
		//mLoaderManager.restartLoader(getSubtitle,null, this);
		
		adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.dropdown_search, items);
		// Getting the instance of AutoCompleteTextView
		actv = (AutoCompleteTextView) rootView
				.findViewById(R.id.autoCompleteTextView);
		actv.setThreshold(1);// will start working from first character
		actv.setAdapter(adapter);// setting the adapter data into the
									// AutoCompleteTextView
		actv.setTextColor(Color.DKGRAY);
		actv.setTextSize(16);
		
		actv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				detailsubtitle = actv.getText().toString();
				
				SearchCursorLoader SearchCursorLoader = new SearchCursorLoader(getActivity(),myDb);
				SimpleAdapterSet(SearchCursorLoader.loadInBackground());

				actv.setText("");
				actv.setHint("과목을 검색하세요");
				// 열려있는 키패드 닫기
				btnUndo2.setVisibility(View.VISIBLE);
				
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);

				imm.hideSoftInputFromWindow(actv.getWindowToken(), 0);

			}

		});
		
		
	
		btnUndo1.setOnClickListener(new View.OnClickListener() {

			
			@Override
			public void onClick(View v) {
				// starting new Async Task
				ViewCleanListener.OnViewClean(true,times); 
				ViewUpdateListener.OnViewUpdate();
				
				Bundle args = new Bundle();
				args.putInt("totalcredit", totalcredit);
				args.putStringArrayList("selectedcredit",selectedcredit);
				fragment = new SearchSubFragment();
				fragment.setArguments(args);
				
				if (fragment != null) {
					
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
							.replace(R.id.frame_container_sub, fragment).commit();
					fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				}

			}
		});
		
		btnUndo2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// starting new Async Task
				
				MyCursorLoader MyCursorLoader = new MyCursorLoader(getActivity(),myDb);
				SimpleAdapterSet(MyCursorLoader.loadInBackground());
				btnUndo2.setVisibility(View.GONE);

			}
		});
		
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// starting new Async Task
				
				

			}
		});
		
		btnAdd.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// starting new Async Task
				if(!samestate){
					if(clickstate){
						
							totalcredit += Integer.parseInt(selectcredit);
			 				setCredit.setText(String.valueOf(totalcredit));
			 				
							myDb.AddTimes(subtitle, prof, selectcredit, times);
							selectedcredit.add(selectcredit); //ArrayList 스텍에 학점 넣음
							ViewUpdateListener.OnViewUpdate();
							clickstate = false;
						} 
					}else{
						final Dialog dialog = new Dialog(getActivity());
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		                dialog.setContentView(R.layout.dialog_main);
		                dialog.setCancelable(true);
		                //there are a lot of settings, for dialog, check them all out!
		               
		                TextView texttitle = (TextView) dialog.findViewById(R.id.TextViewTitle);
		                texttitle.setText(" Overlap");
		                TextView text = (TextView) dialog.findViewById(R.id.TextView01);
		                text.setTextSize(18);
		                text.setText(R.string.Overlap);
		 
		                //set up image view
		                //ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
		                //img.setImageResource(R.drawable.differsize2);
		 
		                //set up button
		                Button button = (Button) dialog.findViewById(R.id.Button01);
		                Button button2 = (Button) dialog.findViewById(R.id.Button02);
		                button2.setVisibility(View.GONE);	
		                button.setOnClickListener(new View.OnClickListener() {

		        			@Override
		        			public void onClick(View view) {
		        				
		        				dialog.cancel(); 
		        			}
		        		});
		             
		                //now that the dialog is set up, it's time to show it    
		                dialog.show();
						// starting new Async Task
					}
				}
			});
		
		
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// starting new Async Task
				
					if(clickstate){
						ViewCleanListener.OnViewClean(true,times);
						ViewUpdateListener.OnViewUpdate();
						clickstate = false;
					}else{
						if(!selectedcredit.isEmpty()){
							totalcredit -= Integer.parseInt(selectedcredit.get(selectedcredit.size()-1)); //스텍에서 최근 학점부터 꺼내기 
							if(totalcredit<0){
								setCredit.setText("0");
								totalcredit=0;
				 				
							}else{
								setCredit.setText(String.valueOf(totalcredit)); 
							}
						}
						myDb.SubTimes();
						ViewCleanListener.OnViewClean(true,null); //null은 모든 화면 지우기
						ViewCleanListener.OnViewClean(true,times);
						ViewUpdateListener.OnViewUpdate();
					}
					setOverlap.setText("");
			}
		});
		
		btnRefresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				final Dialog dialog = new Dialog(getActivity());
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
                dialog.setContentView(R.layout.dialog_main);
                dialog.setCancelable(true);
                //there are a lot of settings, for dialog, check them all out!
               
                TextView texttitle = (TextView) dialog.findViewById(R.id.TextViewTitle);
                texttitle.setText(" Initialization");
                TextView text = (TextView) dialog.findViewById(R.id.TextView01);
                text.setText(R.string.Initialization);
 
                //set up image view
                //ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
                //img.setImageResource(R.drawable.differsize2);
 
                //set up button
                Button button = (Button) dialog.findViewById(R.id.Button01);
                
                button.setOnClickListener(new View.OnClickListener() {

        			@Override
        			public void onClick(View view) {
        				setCredit.setText("0");
        				totalcredit=0;
        				ViewCleanListener.OnViewClean(true,times);
        				ViewCleanListener.OnViewClean(true,null);
        				myDb.RefreshTimes();
        				dialog.cancel();
        			}
        		});
                Button button2 = (Button) dialog.findViewById(R.id.Button02);
                button2.setOnClickListener(new View.OnClickListener() {

        			@Override
        			public void onClick(View view) {
        				dialog.cancel();
        			}
        		});
                //now that the dialog is set up, it's time to show it    
                dialog.show();
				// starting new Async Task
				
                setOverlap.setText("");
			}
		});
		
		 
		
		
		Spinner spinner = (Spinner) rootView.findViewById(R.id.depgradespinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		spinneradapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.depgrade, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		
		spinner.setAdapter(spinneradapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // TODO Auto-generated method stub
                //((TextView) parent.getChildAt(0)).setTextSize(10);
            	
            	depgradenum = spinneradapter.getItem(position).toString();
            	
            	if(depgradenum.equals("전체")){
            		depgradenum = kordepname;
            	}
            	else if(depgradenum.equals("1학년")){
            		depgradenum = "1";
            	}
            	else if(depgradenum.equals("2학년")){
            		depgradenum = "2";
            	}
            	else if(depgradenum.equals("3학년")){
            		depgradenum = "3";
            	}
            	else if(depgradenum.equals("4학년")){
            		depgradenum = "4";
            	}
            
            	DepgradeCursorLoader DepgradeCursorLoader = new DepgradeCursorLoader(getActivity(),myDb);
				SimpleAdapterSet(DepgradeCursorLoader.loadInBackground());
				
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
		
		


		getLoaderManager().initLoader(getAllDate, null, this);

	}
	    

	
	public void SimpleAdapterSet(Cursor cursor){
		
		
		String[] columns = new String[] { "_id", "subtitle", "prof", "credit",
				"classnum", "limitnum", detaildep, "dep_grade" };
		int[] to = new int[] { R.id._id, R.id.subtitle_entry, R.id.prof_entry,
				R.id.credit_entry, R.id.classnum_entry, R.id.limitnum_entry,
				R.id.depname_entry, R.id.dep_grade_entry };

		//mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.listitem,
		//		cursor, columns, to, 0);
		mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.listitem,
				cursor, columns, to, 0);
	
		
		ListView list = (ListView) rootView.findViewById(R.id.list);
		list.setAdapter(mAdapter);

		registerForContextMenu(list);
		
		list.setOnItemClickListener(new OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            		
            	 _id = ((TextView) view.findViewById(R.id._id)).getText()
                         .toString();
            	
            	
            	 if(clickstate){
            		
            			oldtimes=null;
            		
            			oldtimes = times;

            			
            			
            	}
            	 GetTimeCursorLoader GetTimeCursorLoader = new GetTimeCursorLoader(getActivity(),myDb);
 				 GetTimeCursorLoader.loadInBackground();
 				
 					 ViewUpdateListener.OnViewUpdate(); 
 					 mListener.onTimesSelected(times,oldtimes,clickstate,-1, subtitle, prof);
 					
 					 SametimestateListener.GetSametimestateListener();
 					 
					 if(!samestate){
						 ViewUpdateListener.OnViewUpdate();
						 samestatenum=0;
		 				
					 }else{
						 samestatenum++;
						 if(samestatenum>1){
							 ViewUpdateListener.OnViewUpdate(); 
							 mListener.onTimesSelected(times,oldtimes,false,-1, subtitle, prof);
						 }
					 }

				clickstate = true;
 				//SchoolTimetableFragment SchoolTimetableFragment = new SchoolTimetableFragment();
 				//SchoolTimetableFragment.setTimesArray(times);
 				//Toast.makeText(getActivity(),times[1],Toast.LENGTH_SHORT).show();
            	
            }
		});
	
	}
	private void openDB() {
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

		//SchoolTimetableFragment SchoolTimetableFragment = new SchoolTimetableFragment();
		//SchoolTimetableFragment.setisPageOpen(true);
	
		
		 closeDB();
	}

	/*
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == getAllDate) {
			AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
					.getMenuInfo();

			
			getLoaderManager().getLoader(0).forceLoad();
		}

		return true;
	}*/

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// TODO Auto-generated method stub
		mAdapter.swapCursor(cursor);
		
		if (loader.getId() == 0){
			 mAdapter.swapCursor(cursor);
		 }else if (loader.getId() == 1)
		 {
			 mAdapter.swapCursor(cursor);
		 }
		 else if (loader.getId() == 2)
		 {
			 mAdapter.swapCursor(cursor);
		 }
		 else if (loader.getId() == 3)
		 {
			 mAdapter.swapCursor(cursor);
		 }
		 else if (loader.getId() == 4)
		 {
			 mAdapter.swapCursor(cursor);
		 }
		
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
			 getLoaderManager().restartLoader(getAllDate, null, this);
			 mAdapter.swapCursor(null);
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		
		CursorLoader cursorloader= null; 
		
		switch(id){
		case 0:
			cursorloader = new MyCursorLoader(getActivity(), myDb);
			break;
		case 1:
			cursorloader = new SubTitleCursorLoader(getActivity(),myDb);
			break;
		case 2:
			cursorloader = new SearchCursorLoader(getActivity(),myDb);
			break;
		case 3:
			cursorloader = new DepgradeCursorLoader(getActivity(),myDb);
			break;
		case 4:
			cursorloader = new GetTimeCursorLoader(getActivity(),myDb);
			break;
	
		}
		
		
		return cursorloader;
		
	}

	static class MyCursorLoader extends CursorLoader {
		DBAdapter myDb;

		public MyCursorLoader(Context context, DBAdapter myDb) {
			super(context);
			this.myDb = myDb;
		}

		public MyCursorLoader(Context context) {
			// TODO Auto-generated constructor stub
			super(context);

		}

		public Cursor loadInBackground() {
			
			Cursor cursor = myDb.getDepName(detaildep, kordepname);
			
			try {

				TimeUnit.SECONDS.sleep(0);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return cursor;
		}
		
			
			
		
	}
	
	//자동완성검색 밑 리스트항목을 설정,  과목 이름만 추출해서 items에 담아서 리턴
	static class SubTitleCursorLoader extends CursorLoader {
		DBAdapter myDb;

		public SubTitleCursorLoader(Context context, DBAdapter myDb) {
			super(context);
			this.myDb = myDb;
		}

		public SubTitleCursorLoader(Context context) {
			// TODO Auto-generated constructor stub
			super(context);

		}

		public Cursor loadInBackground() {
			Cursor cursor = myDb.getSubtitle(kordepname);
		
			
			items = new ArrayList<String>();

		   	if (cursor != null) {
		   		cursor.moveToFirst();
		        while (!cursor.isAfterLast()){
		            items.add(cursor.getString(cursor.getColumnIndex("subtitle")));
		            cursor.moveToNext();
		        }
		    }
		 
			try{
				
				TimeUnit.SECONDS.sleep(0);
				
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			return cursor;
		}
	}
	
	static class SearchCursorLoader extends CursorLoader {
		DBAdapter myDb;

		public SearchCursorLoader(Context context, DBAdapter myDb) {
			super(context);
			this.myDb = myDb;
		}

		public SearchCursorLoader(Context context) {
			// TODO Auto-generated constructor stub
			super(context);

		}

		public Cursor loadInBackground() {
			Cursor cursor = myDb.SearchSubtitle(detailsubtitle, detaildep);

			try {

				TimeUnit.SECONDS.sleep(0);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return cursor;
		}
	}
	
	
	static class DepgradeCursorLoader extends CursorLoader {
		DBAdapter myDb;

		public DepgradeCursorLoader(Context context, DBAdapter myDb) {
			super(context);
			this.myDb = myDb;
		}

		public DepgradeCursorLoader(Context context) {
			// TODO Auto-generated constructor stub
			super(context);

		}

		public Cursor loadInBackground() {
			Cursor cursor = myDb.SearchDepgrade(depgradenum,detaildep,kordepname);

			try {

				TimeUnit.SECONDS.sleep(0);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return cursor;
		}
	}
	
	static class GetTimeCursorLoader extends CursorLoader {
		DBAdapter myDb;
	
	
		public GetTimeCursorLoader(Context context, DBAdapter myDb) {
			super(context);
			this.myDb = myDb;
		}

		public GetTimeCursorLoader(Context context) {
			// TODO Auto-generated constructor stub
			super(context);

		}

		public Cursor loadInBackground() {
			Cursor cursor = myDb.SearchTime(_id);

			//times = new ArrayList<String>();
			times = new String[16];
			
		 	if (cursor != null) {
		   		cursor.moveToFirst();
		        while (!cursor.isAfterLast()){
		        	//times.add(cursor.getString(cursor.getColumnIndex("time1")));
		        	
		        	//_qid = cursor.getInt(cursor.getColumnIndex("_id"));
		        	selectcredit = cursor.getString(cursor.getColumnIndex("credit"));
		        	subtitle = cursor.getString(cursor.getColumnIndex("subtitle"));
		        	prof = cursor.getString(cursor.getColumnIndex("prof"));
		        	
		        	for(int i =0; i<16; i++){
		        		
		        		int a=i+1;
		        		times[i] = cursor.getString(cursor.getColumnIndex("time"+String.valueOf(a)));
		        	}
		        	

		        	cursor.moveToNext();
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
	
	public void Setsamestate(boolean sametime){
		samestate = sametime;
	
		if(sametime){
			setOverlap.setText("시간이 겹칩니다");
		}
		else{
			setOverlap.setText("");
		}
	}
}
