package com.daemin.community.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.toolbox.ImageLoader;
import com.daemin.timetable.common.MyVolley;
import com.daemin.timetable.R;
import com.navercorp.volleyextensions.view.ZoomableNetworkImageView;
import com.navercorp.volleyextensions.view.ZoomableNetworkImageView.OnImageChangedListener;

/**
 * Created by hernia on 2015-06-20.
 */
public class DoubleTapZoomNetworkImageViewActivity extends Activity {
    private static final String SAMPLE_IMAGE_URL = "http://hernia.cafe24.com/android/image/test2.png";
    private ZoomableNetworkImageView zoomableImageView;
    private ImageLoader imageLoader;
    private ProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_tap_zoom_network_image_view);
        imageLoader = MyVolley.getImageLoader();
        initializeZoomableImageView();
    }

    private void initializeZoomableImageView() {
        zoomableImageView = (ZoomableNetworkImageView) findViewById(R.id.zoom_imageview);
        circleProgressBar = (ProgressBar) findViewById(R.id.circleProgressBar);
        OnImageChangedListener listener = new OnImageChangedListener(){

            @Override
            public void onImageChanged(ZoomableNetworkImageView zoomableImageView) {
                if ( zoomableImageView.getDrawable() == null) {
                    return;
                }
                circleProgressBar.setVisibility(View.GONE);
                zoomableImageView.removeOnImageChangedListener();
            }};
        zoomableImageView.setOnImageChangedListener(listener);
        zoomableImageView.setImageUrl(SAMPLE_IMAGE_URL, imageLoader);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}