package com.daemin.timetable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.common.CurrentTime;
import com.daemin.enumclass.DayOfMonthPos;
import com.daemin.enumclass.DayOfMonthPosState;

@SuppressLint("DefaultLocale")
public class InitMonthThread extends InitThread {
	SurfaceHolder mholder;
	private boolean isLoop = true;
	private int width, height,dayOfWeekOfLastMonth,dayNumOfMonth,dayOfWeek,weekNum;//이전달의 마지막날 요일
	private String[] monthData;
	Context context;
	private Paint hp; // 1시간 간격 수평선
	private Paint hpvp; // 30분 간격 수평선, 수직선
	private Paint tp,tpred,tpblue,tpgray,cp; // 시간 텍스트
	static int tempxth;
	static int tempyth;
	Canvas canvas;

	public InitMonthThread(SurfaceHolder holder, Context context) {
		this.mholder = holder;
		this.context = context;
		this.monthData = CurrentTime.getDayOfLastMonth();
		this.dayOfWeekOfLastMonth = CurrentTime.getDayOfWeekOfLastMonth();
		this.dayNumOfMonth = CurrentTime.getDayNumOfMonth();
		this.dayOfWeek = CurrentTime.getDayOfWeek();
		this.weekNum = CurrentTime.getWeekNum();
		tempxth = 0;
		tempyth = 0;
		hp = new Paint(Paint.ANTI_ALIAS_FLAG);
		hp.setColor(context.getResources().getColor(R.color.maincolor));

		hpvp = new Paint(Paint.ANTI_ALIAS_FLAG);
		hpvp.setAlpha(70);
		tp = new Paint(Paint.ANTI_ALIAS_FLAG);
		tp.setTextSize(22);
		tp.setTextAlign(Paint.Align.CENTER);
		tpred = new Paint(Paint.ANTI_ALIAS_FLAG);
		tpred.setTextSize(22);
		tpred.setTextAlign(Paint.Align.CENTER);
		tpred.setColor(context.getResources().getColor(R.color.red));
		tpblue = new Paint(Paint.ANTI_ALIAS_FLAG);
		tpblue.setTextSize(22);
		tpblue.setTextAlign(Paint.Align.CENTER);
		tpblue.setColor(context.getResources().getColor(R.color.blue));
		tpgray = new Paint(Paint.ANTI_ALIAS_FLAG);
		tpgray.setTextSize(22);
		tpgray.setTextAlign(Paint.Align.CENTER);
		tpgray.setColor(context.getResources().getColor(R.color.middlegray));

		cp = new Paint(Paint.ANTI_ALIAS_FLAG);
		cp.setStyle(Paint.Style.STROKE);
		cp.setStrokeWidth(2);
		cp.setColor(Color.RED);
	}
	public void setCurrentTime(String[] monthData, int dayOfWeekOfLastMonth, int dayNumOfMonth, int dayOfWeek, int weekNum){
		this.monthData = monthData;
		this.dayOfWeekOfLastMonth = dayOfWeekOfLastMonth;
		this.dayNumOfMonth = dayNumOfMonth;
		this.dayOfWeek = dayOfWeek;
		this.weekNum = weekNum;
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
					Common.checkTableStateIsNothing = true;

					// 사각형 그리기
					for (DayOfMonthPos DOMP : DayOfMonthPos.values()) {
						DOMP.drawTimePos(canvas, width, height);
					}
				}
			} catch (Exception e) {

			} finally {
				if (canvas != null)
					mholder.unlockCanvasAndPost(canvas);
			}
		}
	}

	public void getDownXY(int xth, int yth) {
		makeTimePos(xth, yth);
		tempxth = xth;
		tempyth = yth;
	}

	public void getMoveXY(int xth, int yth) {
		if (tempxth != xth || tempyth != yth) {
			makeTimePos(xth, yth);
			tempxth = xth;
			tempyth = yth;
		}
	}

	public void makeTimePos(int xth, int yth) {
		DayOfMonthPos DOMP = DayOfMonthPos.valueOf(Convert.getxyMergeForMonth(xth, yth));
		if (DOMP.getPosState() == DayOfMonthPosState.NO_PAINT) {
			DOMP.setPosState(DayOfMonthPosState.PAINT);
			Common.getTempTimePos().add(DOMP.name());
		} else {
			DOMP.setPosState(DayOfMonthPosState.NO_PAINT);
		}
		return;
	}

	public void initScreen() {
		float[] hp_hour = {
				// 가로선 : 1시간 간격
				0, height / 32 + 6, width, height / 32 + 6 , 0, height * 12 / 64 + 6, width,
				height * 12 / 64 + 6, 0, height * 22 / 64 + 6, width, height * 22 / 64 + 6, 0,
				height * 32 / 64 + 6, width, height * 32 / 64 + 6, 0, height * 42 / 64 + 6, width,
				height * 42 / 64 + 6, 0, height * 52 / 64 + 6, width, height * 52 / 64 + 6, 0,
				height * 62 / 64 + 6, width, height * 62 / 64 + 6};

		float[] vp = {
				// 세로 선
				width / 7, height / 32 + 6, width / 7, height * 62 / 64+ 6, width * 2 / 7,
				height / 32 + 6, width * 2 / 7, height * 62 / 64+ 6, width * 3 / 7,
				height / 32 + 6, width * 3 / 7, height * 62 / 64+ 6, width * 4 / 7,
				height / 32 + 6, width * 4 / 7, height * 62 / 64+ 6, width * 5 / 7,
				height / 32 + 6, width * 5 / 7, height * 62 / 64+ 6, width * 6 / 7,
				height / 32 + 6, width * 6 / 7, height * 62 / 64+ 6};

		canvas.drawColor(Color.WHITE);
		canvas.drawLines(hp_hour, hp);
		canvas.drawLines(vp, hpvp);

		canvas.drawText("SUN", width * 1 / 14, height * 2 / 62 - 1, tpred);
		canvas.drawText("MON", width * 3 / 14, height * 2/ 62 - 1, tp);
		canvas.drawText("TUE", width * 5 / 14, height * 2/ 62 - 1, tp);
		canvas.drawText("WED", width * 7 / 14, height * 2/ 62 - 1, tp);
		canvas.drawText("THU", width * 9 / 14, height * 2 / 62 - 1, tp);
		canvas.drawText("FRI", width * 11 / 14, height * 2 / 62 - 1, tp);
		canvas.drawText("SAT", width * 13 / 14, height * 2 / 62 - 1, tpblue);

		for(int i = 0; i<dayOfWeekOfLastMonth+1; i++){
			canvas.drawText(monthData[i], width * (4*(i%7)+1) / 28-6, height* (( 10 * (i/7)) + 4)/ 64, tpgray);
		}
		for(int i = dayOfWeekOfLastMonth+1; i<dayOfWeekOfLastMonth+dayNumOfMonth+1; i++){
			int j = i%7;
			switch(j){
				case 0:
					canvas.drawText(monthData[i], width * 1 / 28-6, height* (( 10 * (i/7)) + 4) / 64, tpred);
					break;
				case 6:
					canvas.drawText(monthData[i], width * 25 / 28-6, height * ((10 * (i/7)) + 4) / 64, tpblue);
					break;
				default:
					canvas.drawText(monthData[i], width * (4*j+1) / 28-6, height * ((10 * (i/7)) + 4) / 64, tp);
					break;
			}
		}
		for(int i = dayOfWeekOfLastMonth + dayNumOfMonth + 1; i < 42; i++) {
			canvas.drawText(monthData[i], width * (4*(i%7)+1) / 28-6, height * ((10 * (i/7)) + 4) / 64, tpgray);
		}
		canvas.drawCircle(width * (4*dayOfWeek+1) / 28-6, height * (10 * (weekNum-1) + 3) / 63+1,18, cp);
	}

}
