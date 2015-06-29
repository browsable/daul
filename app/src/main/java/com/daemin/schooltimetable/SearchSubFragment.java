package com.daemin.schooltimetable;

import java.util.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import android.view.inputmethod.*;
import android.widget.*;
import android.widget.AdapterView.*;

import com.daemin.timetable.*;




public class SearchSubFragment extends Fragment {
	

	String searchunivname;
	String detaildep;
	String kordepname;
	ArrayAdapter<String> adapter;
	ArrayList<String> items;//과목명 검색시 자동검색기능 밑 리스트 뿌려주는 아이템
	AutoCompleteTextView actv;
	Fragment fragment = null;
	
	int totalcredit;
	ArrayList<String> selectedcredit;
	
	static boolean isPageOpen = true;
	Animation translateLeftAnim;
	Animation translateRightAnim;
	static LinearLayout slidingPage;
	
	String[] koreatech_depname = { "컴공 | HW", "컴공 | SW", "컴공 | 시스템응용",
			"기계 | 자동차 에너지", "기계 | 컴퓨터응용", "기계 | IT응용", "메카 | 생산시스템",
			"메카 | 디지털시스템", "메카 | 제어시스템", "전전통 | 전기공학", "전전통 | 전자공학",
			"전전통 | 정보통신공학", "디공 | 디자인공학", "건축 | 건축공학", "건축 | 건축학",
			"에신화 | 신소재공학", "에신화 | 응용화학공학", "에신화 | 에너지공학", "산경 | 인력경영",
			"산경 | 기술경영", "산경 | EBusiness" };
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_searchsub, container, false);
    
        selectedcredit=new ArrayList<String>();
        totalcredit = getArguments().getInt("totalcredit");
        selectedcredit = getArguments().getStringArrayList("selectedcredit");
      
        adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.dropdown_search, koreatech_depname);
		// Getting the instance of AutoCompleteTextView
		actv = (AutoCompleteTextView) rootView
				.findViewById(R.id.autoCompleteTextView);
		actv.requestFocus(); 
		actv.setThreshold(1);// will start working from first character
		actv.setAdapter(adapter);// setting the adapter data into the
									// AutoCompleteTextView
		actv.setTextColor(Color.DKGRAY);
		actv.setTextSize(16);

		actv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
			 
				
				String detaildep;

				detaildep = actv.getText().toString();
				
				actv.setText("");
				connectDBlist(detaildep);
				
				
				// 열려있는 키패드 닫기

				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);

				imm.hideSoftInputFromWindow(actv.getWindowToken(), 0);

			}

		});
		
        return rootView;
    }

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		//if(isPageOpen){
		//slidingPage.startAnimation(translateRightAnim);
		
		//}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		
		//SchoolTimetableFragment fragment = new SchoolTimetableFragment();
		//fragment.setisPageOpen(false);
		
		//slidingPage.setVisibility(View.INVISIBLE);
		
		
	}


	
	public void connectDBlist(String detaildep) {
		String olddetaildep; 
		
		if (detaildep.equals("컴공 | HW")) {
			olddetaildep = detaildep;
			detaildep = "com_hw";
			kordepname = "컴퓨";
			showlistView(olddetaildep, detaildep, kordepname);

		} else if (detaildep.equals("컴공 | SW")) {
			olddetaildep = detaildep;
			detaildep = "com_sw";
			kordepname = "컴퓨";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("컴공 | 시스템응용")) {
			olddetaildep = detaildep;
			detaildep = "com_sys";
			kordepname = "컴퓨";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("기계 | 자동차 에너지")) {
			olddetaildep = detaildep;
			detaildep = "me_car";
			kordepname = "기계";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("기계 | 컴퓨터응용")) {
			olddetaildep = detaildep;
			detaildep = "me_com";
			kordepname = "기계";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("기계 | IT응용")) {
			olddetaildep = detaildep;
			detaildep = "me_it";
			kordepname = "기계";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("메카 | 생산시스템")) {
			olddetaildep = detaildep;
			detaildep = "meca_product";
			kordepname = "메카";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("메카 | 디지털시스템")) {
			olddetaildep = detaildep;
			detaildep = "meca_digital";
			kordepname = "메카";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("메카 | 제어시스템")) {
			olddetaildep = detaildep;
			detaildep = "meca_control";
			kordepname = "메카";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("전전통 | 전기공학")) {
			olddetaildep = detaildep;
			detaildep = "elec_elec";
			kordepname = "전기";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("전전통 | 전자공학")) {
			olddetaildep = detaildep;
			detaildep = "ele_electron";
			kordepname = "전기";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("전전통 | 정보통신공학")) {
			olddetaildep = detaildep;
			detaildep = "elec_inform";
			kordepname = "전기";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("디공 | 디자인공학")) {
			olddetaildep = detaildep;
			detaildep = "design";
			kordepname = "디자";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("건축 | 건축공학")) {
			olddetaildep = detaildep;
			detaildep = "arch_engi";
			kordepname = "건축";
			showlistView(olddetaildep, detaildep, kordepname);

		} else if (detaildep.equals("건축 | 건축학")) {
			olddetaildep = detaildep;
			detaildep = "arch_arch";
			kordepname = "건축";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("에신화 | 신소재공학")) {
			olddetaildep = detaildep;
			detaildep = "emc_m";
			kordepname = "에너";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("에신화 | 응용화학공학")) {
			olddetaildep = detaildep;
			detaildep = "emc_c";
			kordepname = "에너";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("에신화 | 에너지공학")) {
			olddetaildep = detaildep;
			detaildep = "emc_e";
			kordepname = "에너";
			showlistView(olddetaildep, detaildep, kordepname);

		} else if (detaildep.equals("산경 | 인력경영")) {
			olddetaildep = detaildep;
			detaildep = "busi_human"; 
			kordepname = "산업";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("산경 | 기술경영")) {
			olddetaildep = detaildep;
			detaildep = "busi_tech";
			kordepname = "산업";
			showlistView(olddetaildep, detaildep, kordepname);
		} else if (detaildep.equals("산경 | EBusiness")) {
			olddetaildep = detaildep;
			detaildep = "busi_ebusi";
			kordepname = "산업";
			showlistView(olddetaildep, detaildep, kordepname);
		}
		

	}

	
	public void onListItemClick(ListView l, View v, int position, long id) {
        // Insert desired behavior here.
        Log.i("FragmentComplexList", "Item clicked: " + id);
    }

	
	
	// database demo
	public void showlistView(String olddetaildep,String detaildep, String kordepname) {
		
		isPageOpen = false;
		
		Bundle args = new Bundle();
		args.putString("olddetaildep", olddetaildep);
		args.putString("detaildep", detaildep);
		args.putString("kordepname", kordepname);
		args.putInt("totalcredit", totalcredit);
		args.putStringArrayList("selectedcredit",selectedcredit);
		
		ShowSubFragment fragment = new ShowSubFragment();
		fragment.setArguments(args);

		if (fragment != null) {
			
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container_sub, fragment).commit();
		}
		  
	}
	 



	/*
	private class SlidingPageAnimationListener implements AnimationListener {

		public void onAnimationEnd(Animation animation) {
			if (isPageOpen) {
				
				isPageOpen = false;
			} else {

				isPageOpen = true;
			}
		}

		public void onAnimationRepeat(Animation animation) {

		}

		public void onAnimationStart(Animation animation) {

		}

	}*/
}
