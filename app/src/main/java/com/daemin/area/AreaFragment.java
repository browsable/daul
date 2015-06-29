package com.daemin.area;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daemin.timetable.R;
import com.daemin.timetable.common.AsyncCallback;
import com.daemin.timetable.common.AsyncExecutor;
import com.daemin.timetable.common.BasicFragment;
import com.daemin.timetable.common.Common;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

public class AreaFragment extends BasicFragment {
	//public static RequestQueue queue;
	public AreaFragment() {
		super(R.layout.fragment_phone, "AreaFragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = super.onCreateView(inflater, container, savedInstanceState);
		if (layoutId > 0) {

			//load();
		}
		return root;
	}
	public void load() {
		// 비동기로 실행될 코드
		Callable<Void> callable = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				int count;
				String searchuniv = "koreatech";
				try {
					URL url = new URL(Common.UNIVDB_URL+searchuniv+".sqlite");
					URLConnection conection = url.openConnection();
					conection.connect();
					// input stream to read file - with 8k buffer
					InputStream input = new BufferedInputStream(url.openStream(), 8192);
					OutputStream output = new FileOutputStream("/sdcard/" + searchuniv + ".sqlite");

					///data/data/com.daemin.timetable/databases
					byte data[] = new byte[2048];


					while ((count = input.read(data)) != -1) {
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

		};

		new AsyncExecutor<Void>()
				.setCallable(callable)
				.setCallback(callback)
				.execute();
	}

	// 비동기로 실행된 결과를 받아 처리하는 코드
	private AsyncCallback<Void> callback = new AsyncCallback<Void>() {
		@Override
		public void onResult(Void result) {
			//Toast.makeText(getActivity(), getActivity().getString(R.string.dataloading_error), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void exceptionOccured(Exception e) {
			Toast.makeText(getActivity(), getActivity().getString(R.string.dataloading_error), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void cancelled() {
			Toast.makeText(getActivity(), getActivity().getString(R.string.dataloading_error), Toast.LENGTH_SHORT).show();
		}
	};
}
