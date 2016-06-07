package com.daemin.common;

import android.annotation.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("ValidFragment")
public class BasicFragment extends Fragment {
	private final String KEY_LAYOUT_ID = "Basic:LayoutId";
	public String TAG = "BasicFragment";
	
	protected int layoutId = 0;
	protected Typeface normalTypeface;
	protected Typeface boldTypeface;
	
	public BasicFragment() {
		
	}
	
    public BasicFragment(int id, String tag) {    	
		this.layoutId = id;    
		this.TAG = tag;
		this.normalTypeface = Typeface.DEFAULT;
		this.boldTypeface = Typeface.DEFAULT_BOLD;
    }
    
    public BasicFragment(int id, String tag, Typeface normalTypeface, Typeface boldTypeface) {    	
		this.layoutId = id;    
		this.TAG = tag;
		this.normalTypeface = normalTypeface;
		this.boldTypeface = boldTypeface;
    }
    
    
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_LAYOUT_ID)) {
        	layoutId = savedInstanceState.getInt(KEY_LAYOUT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
    	if(layoutId > 0) {
    		View root = inflater.inflate(layoutId, container, false);
    		
    		return root;
    	}    	
    	else 
    	{
	        TextView text = new TextView(getActivity());
	        text.setGravity(Gravity.CENTER);
	        text.setText("No Contents");
	        text.setTextColor(Color.GRAY);
	        text.setTextSize(20 * getResources().getDisplayMetrics().density);
	        text.setPadding(20, 20, 20, 20);
	
	        LinearLayout layout = new LinearLayout(getActivity());
	        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	        layout.setGravity(Gravity.CENTER);
	        layout.addView(text);
	        return layout;
    	}
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_LAYOUT_ID, layoutId);
    }
    
    @Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}