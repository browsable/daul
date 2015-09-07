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
	private InitThread initThread;
	float txPos, tyPos;
	Context context;
	private int xth, yth;
	private boolean outOfTouchArea;
	private String mode;

	public InitSurfaceView(Context context, String mode) {
		super(context);
		this.context = context;
		this.mode = mode;
		holder = getHolder();
		holder.addCallback(this);
		switch (mode){
			case "week":
				initThread = new InitWeekThread(holder, context);
				break;
			case "month":
				initThread = new InitMonthThread(holder, context);
				break;
		}
	}
	public InitThread getInitThread() {
		return initThread;
	}
	//week or month mode
	public void setMode(String mode) {
		this.mode = mode;
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!initThread.isAlive()) {
			switch (mode){
				case "week":
					initThread = new InitWeekThread(holder, context);
					break;
				case "month":
					initThread = new InitMonthThread(holder, context);
					break;
			}
		}
		initThread.setRunning(true);
		initThread.start();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean done = true;
		initThread.setRunning(false);
		while (done) {
			try {
				initThread.join();
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
		switch (mode) {
			case "week":
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						calXthYth(event);
						if (xth > 0 && yth > 0 && yth < 30) {
							initThread.getDownXY(xth, yth);
							outOfTouchArea = false;
						} else {
							outOfTouchArea = true;
						}
						break;
					case MotionEvent.ACTION_MOVE:
						if (!outOfTouchArea) {
							calXthYth(event);
							if (xth > 0 && yth > 0 && yth < 30) {
								initThread.getMoveXY(xth, yth);
							}
						}
						break;
					case MotionEvent.ACTION_UP:
						if (!outOfTouchArea) {
							calXthYth(event);
							if (xth > 0 && yth > 0 && yth < 30) {
								initThread.getActionUp();
							}
						}
						break;
				}
				break;
			case "month":
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						calXthYth(event);
						if (xth > 0 && yth > 0 && yth < 7) {
							initThread.getDownXY(xth, yth);
							outOfTouchArea = false;
						} else {
							outOfTouchArea = true;
						}
						break;
					case MotionEvent.ACTION_MOVE:
						if (!outOfTouchArea) {
							calXthYth(event);
							if (xth > 0 && yth > 0 && yth < 7) {
								initThread.getMoveXY(xth, yth);
							}
						}
						break;
					case MotionEvent.ACTION_UP:
						if (!outOfTouchArea) {
							calXthYth(event);
							if (xth > 0 && yth > 0 && yth < 7) {
								initThread.getActionUp();
							}
						}
						break;
				}
				break;
		}
		return true;
	}
	public void calXthYth(MotionEvent event) {
		switch (mode) {
			case "week":
					//화면에 x축으로 15등분 중 몇번째에 위치하는지
					xth = (Integer.parseInt(String.format("%.0f", event.getX()))) * 15 / initThread.getWidth();
					if (xth % 2 == 0) {
						xth -= 1;
					}
					//화면에 y축으로 32등분 중 몇번째에 위치하는지
					yth = (Integer.parseInt(String.format("%.0f", event.getY()))) * 32 / initThread.getHeight();
					if (yth % 2 == 0) {
						if (DrawMode.CURRENT.getMode() == 0 || DrawMode.CURRENT.getMode() == 3)
							yth -= 1;
					}
				break;
			case "month":
				//화면에 x축으로 7등분 중 몇번째에 위치하는지
				xth = (Integer.parseInt(String.format("%.0f", event.getX()))) * 7 / initThread.getWidth()+1;
				//화면에 y축으로 6등분 중 몇번째에 위치하는지
				yth = (Integer.parseInt(String.format("%.0f", event.getY()))) * 6 / initThread.getHeight()+1;
				break;
			}
		return;
	}
}
