package com.daemin.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by hernia on 2015-11-05.
 */
public class CaptureView extends ImageView {
    public CaptureView(Context context)
    {
        super(context);
        setBackgroundColor(0xFF0F50);
    }
    public CaptureView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public CaptureView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint  = new Paint(Paint.LINEAR_TEXT_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(12.0F);
        canvas.drawText("Hello World in custom view", 100, 100, paint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub
        Log.d("Hello Android", "Got a touch event: " + event.getAction());
        return super.onTouchEvent(event);

    }
    public float getDip(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = (int)getDip(10);
        int height = (int)getDip(10);
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED: // unspecified
                width = widthMeasureSpec;
                break;
            case MeasureSpec.AT_MOST:  // wrap_content
                break;
            case MeasureSpec.EXACTLY:  // match_parent
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED: // unspecified
                height = heightMeasureSpec;
                break;
            case MeasureSpec.AT_MOST:  // wrap_content
                break;
            case MeasureSpec.EXACTLY:  // match_parent
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }
        setMeasuredDimension(width, height);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
    }
}
