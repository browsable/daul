package com.daemin.timetable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.SerialNumberGenerator;
import com.daemin.enumclass.TimePos;


public class InitSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder holder;
	private InitThread i_Thread;
	float txPos, tyPos;
	Context context;
	private int xth, yth;
	private boolean outOfTouchArea;

	public InitSurfaceView(Context context) {
		super(context);
		this.context = context;
		holder = getHolder();
		holder.addCallback(this);
		i_Thread = new InitThread(holder, context);
	}
	public InitThread getI_Thread() {
		return i_Thread;
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!i_Thread.isAlive()) {
			i_Thread = new InitThread(holder, context);
		}
		i_Thread.setRunning(true);
		i_Thread.start();
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean done = true;
		i_Thread.setRunning(false);
		while (done) {
			try {
				i_Thread.join();
				done = false;
				SerialNumberGenerator.COUNT.initCount();
				for (TimePos ETP : TimePos.values()) {
					//시간표 사각형 그려진 영역 초기화
					if(ETP.getPosState()!= PosState.NO_PAINT){
						ETP.setPosState(PosState.NO_PAINT);
					}
				}
			} catch (InterruptedException e) {

			}
		}
	} 

	@SuppressLint({ "ClickableViewAccessibility", "DefaultLocale" })
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
				calXthYth(event);
				if (xth > 0 && yth > 0 && yth < 30) {
					i_Thread.getDownXY(xth, yth);
					outOfTouchArea = false;
				}else{
					outOfTouchArea = true;
				}
			break;
		case MotionEvent.ACTION_MOVE:
			if(!outOfTouchArea){
				calXthYth(event);
				if (xth > 0 && yth > 0 && yth < 30) {
					i_Thread.getMoveXY(xth, yth);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
				i_Thread.ActionUp();
			break;
		}

		return true;
	}
	public void calXthYth(MotionEvent event) {
		//화면에 x축으로 15등분 중 몇번째에 위치하는지
		xth = (Integer.parseInt(String.format("%.0f", event.getX()))) * 15 / i_Thread.getWidth();
		if (xth % 2 == 0) {
			xth -= 1;
		}
		//화면에 y축으로 32등분 중 몇번째에 위치하는지
		yth = (Integer.parseInt(String.format("%.0f", event.getY()))) * 32 / i_Thread.getHeight();
		if (yth % 2 == 0) {
			if(DrawMode.CURRENT.getMode()==0 || DrawMode.CURRENT.getMode()==3) yth -= 1;
		}
		return;
	}
}
