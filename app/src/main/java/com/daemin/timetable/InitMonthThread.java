package com.daemin.timetable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.DayOfMonthPos;
import com.daemin.enumclass.DayOfMonthPosState;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.User;
import com.daemin.event.ExcuteMethodEvent;
import com.daemin.main.MainActivity;

import de.greenrobot.event.EventBus;
import timedao.MyTime;

@SuppressLint("DefaultLocale")
public class InitMonthThread extends InitThread {
	SurfaceHolder mholder;
	private boolean isLoop = true;
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
	public InitMonthThread(SurfaceHolder holder, Context context) {
		this.mholder = holder;
		this.context = context;
		Common.fetchMonthData();
		tempxth = 0;
		tempyth = 0;
		int textSize = context.getResources().getDimensionPixelSize(R.dimen.textsize_l);
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
					initScreen();
					for (DayOfMonthPos DOMP : DayOfMonthPos.values()) {
						DOMP.drawTimePos(canvas, width, height);
					}
				}
			} catch (Exception e) {

			} finally {
				if (canvas != null)
					mholder.unlockCanvasAndPost(canvas);
			}
			//System.gc();
		}
	}
	public void getDownXY(int xth, int yth) {
		if(!(xth>0&&xth+7*(yth-1)<Dates.NOW.dayOfWeek+2 || xth+7*(yth-1)>Dates.NOW.dayOfWeek + Dates.NOW.dayNumOfMonth + 1)) {
			makeTimePos(xth, yth);
			tempxth = xth;
			tempyth = yth;
		}
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
		DayOfMonthPos DOMP = DayOfMonthPos.valueOf(Convert.getxyMergeForMonth(xth, yth));
		if (DOMP.getPosState() == DayOfMonthPosState.NO_PAINT) {
			DOMP.setPosState(DayOfMonthPosState.PAINT);
			if(!Common.getTempTimePos().contains(DOMP.name()))
			Common.getTempTimePos().add(DOMP.name());
		} else if(DOMP.getPosState() == DayOfMonthPosState.PAINT){
			DOMP.setPosState(DayOfMonthPosState.NO_PAINT);
		}else{
			//월에서 일정 등록한 영역 터치시
			Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show();
		}
		return;
	}
	/*public void fetchMonthData(){
		for(MyTime mt :  User.INFO.monthData){
			rp.setColor(Color.parseColor(mt.getColor()));
			rp.setAlpha(130);
			canvas.drawRect(width * mt.getDayofweek() / 15, (height * Convert.HourOfDayToYth(mt.getStarthour()) / 32 + intervalSize) + (2 * height / 32) * mt.getStartmin() / 60,
					width * (mt.getDayofweek() + 2) / 15, (height * Convert.HourOfDayToYth(mt.getEndhour()) / 32 + intervalSize) + (2 * height / 32) * mt.getEndmin() / 60, rp);
		}
	}*/
	public void initScreen() {
		float[] hp_hour = {
				// 가로선
				0, height / 32 + 6, width, height / 32 + 6 , 0, height * 12 / 64 + 6, width,
				height * 12 / 64 + 6, 0, height * 22 / 64 + 6, width, height * 22 / 64 + 6, 0,
				height * 32 / 64 + 6, width, height * 32 / 64 + 6, 0, height * 42 / 64 + 6, width,
				height * 42 / 64 + 6, 0, height * 52 / 64 + 6, width, height * 52 / 64 + 6, 0,
				height * 62 / 64 + 6, width, height * 62 / 64 + 6};

		float[] vp = {
				// 세로 선
				2, height / 32 + 6, 2, height * 62 / 64+ 6, width / 7,
				height / 32 + 6, width / 7, height * 62 / 64+ 6, width * 2 / 7,
				height / 32 + 6, width * 2 / 7, height * 62 / 64+ 6, width * 3 / 7,
				height / 32 + 6, width * 3 / 7, height * 62 / 64+ 6, width * 4 / 7,
				height / 32 + 6, width * 4 / 7, height * 62 / 64+ 6, width * 5 / 7,
				height / 32 + 6, width * 5 / 7, height * 62 / 64+ 6, width * 6 / 7,
				height / 32 + 6, width * 6 / 7, height * 62 / 64+ 6, width-2,
				height / 32 + 6, width-2, height * 62 / 64+ 6};

		canvas.drawColor(Color.WHITE);
		canvas.drawLines(hp_hour, hp);
		canvas.drawLines(vp, hpvp);
		hp.setAlpha(40);
		if(Dates.NOW.isToday)canvas.drawRect(width * (tx - 1) / 7, height * ((ty / 7) * 10 + 2) / 64 + 6,width * tx / 7, height * ((ty / 7 + 1) * 10 + 2) / 64 + 6, hp);
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
		canvas.drawText("SUN", width * 1 / 14, height * 2 / 62 - 1, tpred);
		canvas.drawText("MON", width * 3 / 14, height * 2 / 62 - 1, tp);
		canvas.drawText("TUE", width * 5 / 14, height * 2 / 62 - 1, tp);
		canvas.drawText("WED", width * 7 / 14, height * 2 / 62 - 1, tp);
		canvas.drawText("THU", width * 9 / 14, height * 2 / 62 - 1, tp);
		canvas.drawText("FRI", width * 11 / 14, height * 2 / 62 - 1, tp);
		canvas.drawText("SAT", width * 13 / 14, height * 2 / 62 - 1, tpblue);
		tp.setTextAlign(Paint.Align.LEFT);
		tpred.setTextAlign(Paint.Align.LEFT);
		tpblue.setTextAlign(Paint.Align.LEFT);
	}
}
