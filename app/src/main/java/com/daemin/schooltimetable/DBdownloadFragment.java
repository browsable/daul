package com.daemin.schooltimetable;

import java.io.*;
import java.net.*;
import java.util.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.daemin.timetable.*;

public class DBdownloadFragment extends Fragment {
	
	LinearLayout searchlayout;
	Button btnShowProgress;
	Button search_dbfile;
	TextView univ_name;
	String searchunivname; 
	AutoCompleteTextView actv;
	 String[] univname ={"가톨릭대학교","감리교신학대학교", "건국대학교", "경기대학교", "경희대학교", "고려대학교", "광운대학교","국민대학교", "그리스도대학교", "덕성여자대학교", "동국대학교", "동덕여자대학교", "명지대학교", "삼육대학교", "상명대학교", "서강대학교", "서경대학교", "서울교육대학교", "서울기독대학교", "서울대학교", "서울산업대학교", "서울시립대학교", "서울여자대학교", "성공회대학교", "성균관대학교", "성신여자대학교", "세종대학교", "숙명여자대학교", "숭실대학교", "연세대학교", "육군사관학교", "이화여자대학교", "장로회신학대학교", "중앙대학교", "중앙승가대학교", "총신대학교", "추계예술대학교", "한국방송통신대학교", "한국성서대학교", "한국예술종합학교", "한국외국어대학교", "한국체육대학교", "한성대학교", "한양대학교", "한영신학대학교", "홍익대학교","한국기술교육대학교",
			 "경성대학교", "고신대학교", "동명대학교", "동서대학교", "동아대학교", "동의대학교", "부경대학교", "부산가톨릭대학교", "부산교육대학교", "부산대학교", "부산외국어대학교", "신라대학교", "한국해양대학교", "가천의과학대학교", "경인교육대학교", "안양대학교", "인천가톨릭대학교", "인천대학교", "인하대학교", "경북대학교", "경북외국어대학교", "계명대학교", "대구교육대학교", "대구한의대학교", "광신대학교", "광주과학기술원", "광주교육대학교", "광주대학교", "광주여자대학교", "남부대학교", "전남대학교", "조선대학교", "호남대학교", "호남신학대학교", "울산대학교","강남대학교", "경원대학교", "경찰대학교", "단국대학교", "대진대학교", "루터대학교", "서울신학대학교", "서울장신대학교", "성결대학교", "수원가톨릭대학교", "수원대학교", "신경대학교", "아세아연합신학대학교", 
			 "아주대학교", "용인대학교", "을지대학교", "칼빈대학교", "평택대학교", "포천중문의과대학교", "한경대학교", "한국산업기술대학교", "한국항공대학교", "한북대학교", "한세대학교", "한신대학교", "협성대학교","국군간호사관학교", "대전대학교", "목원대학교", "배재대학교", "우송대학교", "을지의과대학교", "충남대학교", "침례신학대학교", "한국정보통신대학교", "가야대학교", "경운대학교", "경일대학교", "경주대학교", "금오공과대학교", "대구가톨릭대학교", "대구대학교", "대구예술대학교", "대구외국어대학교", "대신대학교",  "동양대학교", "안동대학교", "영남대학교", "영남신학대학교", "위덕대학교", "포항공과대학교", "한동대학교", "경남대학교", "경상대학교", "부산장신대학교", "영산대학교", "인제대학교", 
			 "진주교육대학교", "진주산업대학교", "창원대학교", "한국국제대학교", "해군사관학교","군산대학교", "서남대학교", "예수대학교", "예원예술대학교", "우석대학교", "원광대학교", "전북대학교", "전주교육대학교", "전주대학교", "한일장신대학교", "광주가톨릭대학교", "대불대학교", "동신대학교", "명신대학교", "목포가톨릭대학교", "목포대학교", "목포해양대학교", "순천대학교", "영산선학대학교", "초당대학교", "한려대학교", "공군사관학교", "극동대학교", "꽃동네현도사회복지대학교", "서원대학교", "세명대학교", "영동대학교", "청주교육대학교", "청주대학교", "충북대학교", "충주대학교", "한국교원대학교", "건양대학교", "공주교육대학교", "공주대학교", "금강대학교", "나사렛대학교", "남서울대학교", "대전가톨릭대학교", "선문대학교",
			 "성민대학교", "순천향대학교", "중부대학교", "백석대학교", "청운대학교", "한국전통문화학교", "한서대학교", "호서대학교", "강릉대학교", "강원대학교", "경동대학교", "관동대학교", "상지대학교", "춘천교육대학교", "한라대학교", "한림대학교", "한중대학교", "제주교육대학교", "제주대학교", "탐라대학교", "한밭대학교", "호원대학교", "울산과학기술대학교", 
			 "한국과학기술원"};  
	 
	 int totalcredit;
	 ArrayList<String> selectedcredit;
	 
	 // Progress Dialog
	    private ProgressDialog pDialog;
	 
	    // Progress dialog type (0 - for Horizontal progress bar)
	    public static final int progress_bar_type = 0; 
	 
	    // File url to download
	    private static String file_url = "http://browsable.cafe24.com/timetable/schedule/";
	 
	    View rootView;
	    
	    OnDownstateCommandListener DownstateCommandListener;
		
	    public interface OnDownstateCommandListener {
	        public void OnDownstateCommandListener();
		}
		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
			
				//SchoolTimetableFragment fragment = new SchoolTimetableFragment();
				//fragment.setisPageOpen(false); 
				 try {
					 DownstateCommandListener = (OnDownstateCommandListener) activity;
			           
			        } catch (ClassCastException e) {
			            throw new ClassCastException(activity.toString() + " must implement OnTimesSelectedListener");
			        }
				
		}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.fragment_dbdownload, container, false);
         
        totalcredit = getArguments().getInt("totalcredit");
        selectedcredit = getArguments().getStringArrayList("selectedcredit");
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>  
        (getActivity(),R.layout.dropdown_search, univname);
    //Getting the instance of AutoCompleteTextView  
       actv= (AutoCompleteTextView)rootView.findViewById(R.id.searchuniv);  
       actv.setThreshold(1);//will start working from first character  
       actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView  
       actv.setTextColor(Color.DKGRAY);
	   actv.setTextSize(16);
       
       
       actv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
			
				searchunivname = actv.getText().toString();
				
     		    actv.setText("");
				
				// 열려있는 키패드 닫기
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(actv.getWindowToken(), 0);
				
				
				 if(searchunivname.equals("한국기술교육대학교")){
	     	  			searchunivname= "koreatech";
	     	  			
	     	  			SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE); 
	     	  	   		SharedPreferences.Editor editor = pref.edit();
	     	  	   		editor.putString("searchunivname", searchunivname);
	     	  	        editor.putBoolean("downstate", true);
	     	  	        editor.commit(); 
	     	  			  
	     	  	    
	     	       		new DownloadFileFromURL(searchunivname).execute(file_url+searchunivname+".sqlite");
	     	       		
	     	       		Bundle args = new Bundle();
	     	       		args.putInt("totalcredit", totalcredit);
	     	       		args.putStringArrayList("selectedcredit",selectedcredit);
	     	       		 
	     	       		Fragment fragment = new SearchSubFragment();
	     	       		fragment.setArguments(args);
	     	       		
	     	       		if (fragment != null) {
	     	    			FragmentManager fragmentManager = getFragmentManager();
	     	    			fragmentManager.beginTransaction()
	     	    					.replace(R.id.frame_container_sub, fragment).commit();
	     	    		}
	     	      
	           		}
	         	  else{
	         		 final Dialog dialog = new Dialog(getActivity());
	 				 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
	                 dialog.setContentView(R.layout.dialog_main);
	                 dialog.setCancelable(true);
	                 //there are a lot of settings, for dialog, check them all out!
	                
	                 TextView texttitle = (TextView) dialog.findViewById(R.id.TextViewTitle);
	                 texttitle.setText(" DB Error");
	                 TextView text = (TextView) dialog.findViewById(R.id.TextView01);
	                 text.setText(R.string.Dbwarning);
	  
	                 //set up image view
	                 //ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
	                 //img.setImageResource(R.drawable.differsize2);
	  
	                 //set up button
	                 Button button = (Button) dialog.findViewById(R.id.Button01);
	                 
	                 button.setOnClickListener(new View.OnClickListener() {

	         			@Override
	         			public void onClick(View view) {
	             		    actv.setText("");
	         				dialog.cancel();
	         			}
	         		});
	                 Button button2 = (Button) dialog.findViewById(R.id.Button02);
	                 button2.setOnClickListener(new View.OnClickListener() {

	         			@Override
	         			public void onClick(View view) {
	             		    actv.setText("");
	         				dialog.cancel();
	         			}
	         		});
	                 //now that the dialog is set up, it's time to show it    
	                 dialog.show();
	 				// starting new Async Task
	 			}
	         	 
			}
      });

        return rootView;
    }

	/**
	 * Showing Dialog
	 * */
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case progress_bar_type:
	        pDialog = new ProgressDialog(getActivity());
	        pDialog.setMessage("Downloading file. Please wait...");
	        pDialog.setIndeterminate(false);
	        pDialog.setMax(100);
	        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        pDialog.setCancelable(true);
	        pDialog.show();
	        return pDialog;
	    default:
	        return null;
	    }
	}
	
	class DownloadFileFromURL extends AsyncTask<String, String, String> {
		 
			String searchuniv;
			
	    public DownloadFileFromURL(String searchunivname) {
			// TODO Auto-generated constructor stub
	    	this.searchuniv = searchunivname;
	    	
		}


		/**
	     * Before starting background thread
	     * Show Progress Bar Dialog
	     * */
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        onCreateDialog(progress_bar_type);
	        
	    }
	 
	
	    /**
	     * Downloading file in background thread
	     * */
		@Override
	    protected String doInBackground(String... f_url) {
	        int count;
	        try {
	            URL url = new URL(f_url[0]);
	            URLConnection conection = url.openConnection();
	            conection.connect();
	            // getting file length
	            int lenghtOfFile = conection.getContentLength();
	 
	            // input stream to read file - with 8k buffer
	            InputStream input = new BufferedInputStream(url.openStream(), 8192);

		    	
	            OutputStream output = new FileOutputStream("/sdcard/"+searchuniv+".sqlite");
	            
	         ///data/data/com.daemin.timetable/databases
	            byte data[] = new byte[2048];
	 
	            long total = 0;
	 
	            while ((count = input.read(data)) != -1) {
	                total += count;
	                // publishing the progress....
	                // After this onProgressUpdate will be called
	                publishProgress(""+(int)((total*100)/lenghtOfFile));
	  
	                // writing data to file
	                output.write(data, 0, count);
	            }
	 
	            // flushing output
	            output.flush();
	 
	            // closing streams
	            output.close();
	            input.close();
	 
	        } catch (Exception e) {
	            Log.e("Error: ", e.getMessage());
	        }
	        
	        return null;
	    }
	 
	    /**
	     * Updating progress bar
	     * */
	    protected void onProgressUpdate(String... progress) {
	        // setting progress percentage
	        pDialog.setProgress(Integer.parseInt(progress[0]));
	        
	    	
	   }
	 
	    /**
	     * After completing background task
	     * Dismiss the progress dialog
	     * **/
	    @Override
	    protected void onPostExecute(String file_url) {
	        // dismiss the dialog after the file was downloaded
	    	//getActivity().dismissDialog(progress_bar_type);
	    	pDialog.dismiss();
	    	DownstateCommandListener.OnDownstateCommandListener();
	    }
 
	}
	
}
