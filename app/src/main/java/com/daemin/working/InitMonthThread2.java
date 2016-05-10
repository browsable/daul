package com.daemin.working;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.dialog.DialEnroll;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.DayOfMonthPos;
import com.daemin.enumclass.DayOfMonthPosState;
import com.daemin.enumclass.User;
import com.daemin.event.CreateDialEvent;
import com.daemin.event.ExcuteMethodEvent;
import com.daemin.timetable.R;

import org.greenrobot.eventbus.EventBus;


@SuppressLint("DefaultLocale")
public class InitMonthThread2 extends InitThread2 {
	SurfaceHolder mholder;
	private boolean isLoop = true,downFlag=false, initFlag = true;
	private int width;
	private int height;
	private int ty;
	private int tx;//이전달의 마지막날 요일
	private int intervalSize;
	Context context;
	private Paint hp; // 1시간 간격 수평선
	private Paint hpvp; // 30분 간격 수평선, 수직선
	private Paint tp,tpred,tpblue,tpgray; // 시간 텍스트
	static int tempxth;
	static int tempyth;
	Canvas canvas;
	public String sun,mon,tue,wed,thr,fri,sat;
	float[] hp_hour, vp;
	public InitMonthThread2(SurfaceHolder holder, Context context) {
		this.mholder = holder;
		this.context = context;
		Common.fetchMonthData();
		sun=context.getResources().getString(R.string.day0);
		mon=context.getResources().getString(R.string.day1);
		tue=context.getResources().getString(R.string.day2);
		wed=context.getResources().getString(R.string.day3);
		thr=context.getResources().getString(R.string.day4);
		fri=context.getResources().getString(R.string.day5);
		sat=context.getResources().getString(R.string.day6);
		tempxth = 0;
		tempyth = 0;
		int textSize = context.getResources().getDimensionPixelSize(R.dimen.textsize_default);
		intervalSize = User.INFO.intervalSize;
		hp = new Paint(Paint.ANTI_ALIAS_FLAG);
		hp.setColor(context.getResources().getColor(R.color.maincolor));
		hpvp = new Paint(Paint.ANTI_ALIAS_FLAG);
		hpvp.setAlpha(70);
		tp = new Paint(Paint.ANTI_ALIAS_FLAG);
		tp.setTextSize(textSize);
		tp.setTextAlign(Paint.Align.LEFT);
		tpred = new Paint(Paint.ANTI_ALIAS_FLAG);
		tpred.setTextSize(textSize);
		tpred.setTextAlign(Paint.Align.LEFT);
		tpred.setColor(context.getResources().getColor(R.color.red));
		tpblue = new Paint(Paint.ANTI_ALIAS_FLAG);
		tpblue.setTextSize(textSize);
		tpblue.setTextAlign(Paint.Align.LEFT);
		tpblue.setColor(context.getResources().getColor(R.color.blue));
		tpgray = new Paint(Paint.ANTI_ALIAS_FLAG);
		tpgray.setTextSize(textSize);
		tpgray.setTextAlign(Paint.Align.LEFT);
		tpgray.setColor(context.getResources().getColor(R.color.middlegray));
		ty = Dates.NOW.dayOfWeek + Dates.NOW.dayOfMonth + 1;
		tx = ty % 7;
		if (tx == 0) {
			--ty;
			tx = 7;
		}
	}
	public void setRunning(boolean isLoop) {
		this.isLoop = isLoop;
	}

	@Override
	public void setDate() {

	}

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public void run() {
		while (isLoop) {
			canvas = null;
			try {
				canvas = mholder.lockCanvas();
				width = canvas.getWidth();
				height = canvas.getHeight();
				synchronized (mholder) {
					drawScreen();
					for (DayOfMonthPos DOMP : DayOfMonthPos.values()) {
						DOMP.drawTimePos(canvas, width, height);
					}
				}
			} catch (Exception e) {

			} finally {
				if (canvas != null)
					mholder.unlockCanvasAndPost(canvas);
			}
			try {
				sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void getDownXY(int xth, int yth) {
		downFlag=true;
		if(!(xth>0&&xth+7*(yth-1)<Dates.NOW.dayOfWeek+2 || xth+7*(yth-1)>Dates.NOW.dayOfWeek + Dates.NOW.dayNumOfMonth + 1)) {
			makeTimePos(xth, yth);
			tempxth = xth;
			tempyth = yth;
		}
		downFlag=false;
	}

	public void getMoveXY(int xth, int yth) {
		if (tempxth != xth || tempyth != yth) {
			if(!(xth>0&&xth+7*(yth-1)<Dates.NOW.dayOfWeek+2 || xth+7*(yth-1)>Dates.NOW.dayOfWeek + Dates.NOW.dayNumOfMonth + 1)) {
				makeTimePos(xth, yth);
				tempxth = xth;
				tempyth = yth;
			}
		}
	}
	public void getActionUp() {
		EventBus.getDefault().post(new ExcuteMethodEvent("updateMonthList"));
		System.gc();
	}

	public void makeTimePos(int xth, int yth) {
		try{
			DayOfMonthPos DOMP = DayOfMonthPos.valueOf(Convert.getxyMergeForMonth(xth, yth));
			if (DOMP.getPosState() == DayOfMonthPosState.NO_PAINT) {
				DOMP.setPosState(DayOfMonthPosState.PAINT);
				if(!Common.getTempTimePos().contains(DOMP.name()))
				Common.getTempTimePos().add(DOMP.name());
			} else if(DOMP.getPosState() == DayOfMonthPosState.PAINT){
				DOMP.setPosState(DayOfMonthPosState.NO_PAINT);
			}else{
				//월에서 일정 등록한 영역 터치시
				if(downFlag) {
					EventBus.getDefault().post(new CreateDialEvent(false));
					Intent i = new Intent(context, DialEnroll.class);
					i.putExtra("xth", xth);
					i.putExtra("yth", yth);
					i.putExtra("weekFlag", false);
					context.startActivity(i);
				}

			}
		}catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		return;
	}
	public void drawScreen() {
		if(initFlag) {
			hp_hour = new float[]{
					// 가로선
					0, height / 32 + intervalSize, width, height / 32 + intervalSize, 0, height * 12 / 64 + intervalSize, width,
					height * 12 / 64 + intervalSize, 0, height * 22 / 64 + intervalSize, width, height * 22 / 64 + intervalSize, 0,
					height * 32 / 64 + intervalSize, width, height * 32 / 64 + intervalSize, 0, height * 42 / 64 + intervalSize, width,
					height * 42 / 64 + intervalSize, 0, height * 52 / 64 + intervalSize, width, height * 52 / 64 + intervalSize, 0,
					height * 62 / 64 + intervalSize, width, height * 62 / 64 + intervalSize};

			vp = new float[]{
					// 세로 선
					2, height / 32 + intervalSize, 2, height * 62 / 64 + intervalSize, width / 7,
					height / 32 + intervalSize, width / 7, height * 62 / 64 + intervalSize, width * 2 / 7,
					height / 32 + intervalSize, width * 2 / 7, height * 62 / 64 + intervalSize, width * 3 / 7,
					height / 32 + intervalSize, width * 3 / 7, height * 62 / 64 + intervalSize, width * 4 / 7,
					height / 32 + intervalSize, width * 4 / 7, height * 62 / 64 + intervalSize, width * 5 / 7,
					height / 32 + intervalSize, width * 5 / 7, height * 62 / 64 + intervalSize, width * 6 / 7,
					height / 32 + intervalSize, width * 6 / 7, height * 62 / 64 + intervalSize, width - 2,
					height / 32 + intervalSize, width - 2, height * 62 / 64 + intervalSize};
			initFlag = false;
		}
		canvas.drawColor(Color.WHITE);
		canvas.drawLines(hp_hour, hp);
		canvas.drawLines(vp, hpvp);
		hp.setAlpha(40);
		if(Dates.NOW.isToday)canvas.drawRect(width * (tx - 1) / 7, height * ((ty / 7) * 10 + 2) / 64 + intervalSize,width * tx / 7, height * ((ty / 7 + 1) * 10 + 2) / 64 + intervalSize, hp);
		hp.setAlpha(100);
		for(int i = 0; i<Dates.NOW.dayOfWeek+1; i++){
			canvas.drawText(Dates.NOW.mData[i], width * (i % 7) / 7+intervalSize, height * ((10 * (i / 7)) + 4) / 64, tpgray);
		}
		for(int i = Dates.NOW.dayOfWeek+1; i<Dates.NOW.dayOfWeek+Dates.NOW.dayNumOfMonth+1; i++){
			int j = i%7;
			switch(j){
				case 0:
					canvas.drawText(Dates.NOW.mData[i], 2+intervalSize, height* (( 10 * (i/7)) + 4) / 64, tpred);
					break;
				case 6:
					canvas.drawText(Dates.NOW.mData[i], width * 6 / 7+intervalSize, height * ((10 * (i/7)) + 4) / 64, tpblue);
					break;
				default:
					canvas.drawText(Dates.NOW.mData[i], width * j / 7+intervalSize, height * ((10 * (i/7)) + 4) / 64, tp);
					break;
			}
		}
		for(int i = Dates.NOW.dayOfWeek + Dates.NOW.dayNumOfMonth + 1; i < 42; i++) {
			canvas.drawText(Dates.NOW.mData[i], width * (i % 7) / 7+intervalSize, height * ((10 * (i/7)) + 4) / 64, tpgray);
		}
		tp.setTextAlign(Paint.Align.CENTER);
		tpred.setTextAlign(Paint.Align.CENTER);
		tpblue.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(sun, width * 1 / 14, height * 2 / 62 - 1, tpred);
		canvas.drawText(mon, width * 3 / 14, height * 2 / 62 - 1, tp);
		canvas.drawText(tue, width * 5 / 14, height * 2 / 62 - 1, tp);
		canvas.drawText(wed, width * 7 / 14, height * 2 / 62 - 1, tp);
		canvas.drawText(thr, width * 9 / 14, height * 2 / 62 - 1, tp);
		canvas.drawText(fri, width * 11 / 14, height * 2 / 62 - 1, tp);
		canvas.drawText(sat, width * 13 / 14, height * 2 / 62 - 1, tpblue);
		tp.setTextAlign(Paint.Align.LEFT);
		tpred.setTextAlign(Paint.Align.LEFT);
		tpblue.setTextAlign(Paint.Align.LEFT);
	}
}
