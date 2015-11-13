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
import com.daemin.enumclass.User;
import com.daemin.event.ExcuteMethodEvent;
import com.daemin.main.MainActivity;

import de.greenrobot.event.EventBus;

@SuppressLint("DefaultLocale")
public class InitMonthThread extends InitThread {
	SurfaceHolder mholder;
	private boolean isLoop = true, isToday;
	private int todayIndex;
	private int width;
	private int height;
	private int dayOfWeekOfLastMonth;
	private int dayNumOfMonth;
	private int dayOfMonth;
	private int tmp;
	private int tx;//이전달의 마지막날 요일
	private String[] monthData;
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
		this.monthData = CurrentTime.getDayOfLastMonth();
		this.dayOfWeekOfLastMonth = CurrentTime.getDayOfWeekOfLastMonth();
		this.dayNumOfMonth = CurrentTime.getDayNumOfMonth();
		this.dayOfMonth = CurrentTime.getDayOfMonth();
		isToday  = true;
		todayIndex=0;
		tempxth = 0;
		tempyth = 0;
		hp = new Paint(Paint.ANTI_ALIAS_FLAG);
		hp.setColor(context.getResources().getColor(R.color.maincolor));
		hpvp = new Paint(Paint.ANTI_ALIAS_FLAG);
		hpvp.setAlpha(70);
		tp = new Paint(Paint.ANTI_ALIAS_FLAG);
		tp.setTextSize(38);
		tp.setTextAlign(Paint.Align.CENTER);
		tpred = new Paint(Paint.ANTI_ALIAS_FLAG);
		tpred.setTextSize(38);
		tpred.setTextAlign(Paint.Align.CENTER);
		tpred.setColor(context.getResources().getColor(R.color.red));
		tpblue = new Paint(Paint.ANTI_ALIAS_FLAG);
		tpblue.setTextSize(38);
		tpblue.setTextAlign(Paint.Align.CENTER);
		tpblue.setColor(context.getResources().getColor(R.color.blue));
		tpgray = new Paint(Paint.ANTI_ALIAS_FLAG);
		tpgray.setTextSize(38);
		tpgray.setTextAlign(Paint.Align.CENTER);
		tpgray.setColor(context.getResources().getColor(R.color.middlegray));
		tmp = dayOfWeekOfLastMonth+dayOfMonth+1;
		tx = tmp%7;
		if(tx==0){
			--tmp;
			tx = 7;
		}
		int cnt =0;
		for(int i = dayOfWeekOfLastMonth+1; i<dayOfWeekOfLastMonth+dayNumOfMonth+1; i++){
			++cnt;
		}
		String[] mData = new String[cnt];
		int j =0;
		for(int i = dayOfWeekOfLastMonth+1; i<dayOfWeekOfLastMonth+dayNumOfMonth+1; i++){
			String str = CurrentTime.getTitleYearMonth(context);
			mData[j] = str.substring(6,str.length()-1)+"/"+monthData[i];
			j++;
		}
		User.INFO.setDayOfWeekOfLastMonth(dayOfWeekOfLastMonth);
		User.INFO.setmData(mData);
	}
	public int getDayOfWeekOfLastMonth() {
		return dayOfWeekOfLastMonth;
	}
	public void setTodayIndex(int todayIndex) {
		this.todayIndex = todayIndex;
	}
	public void setCurrentTime(String[] monthData, int dayOfWeekOfLastMonth, int dayNumOfMonth){
		this.monthData = monthData;
		this.dayOfWeekOfLastMonth = dayOfWeekOfLastMonth;
		this.dayNumOfMonth = dayNumOfMonth;
		if(todayIndex==0) isToday= true;
		else isToday=false;
		postMonthData();
	}
	public void postMonthData(){
		int cnt =0;
		for(int i = dayOfWeekOfLastMonth+1; i<dayOfWeekOfLastMonth+dayNumOfMonth+1; i++){
			++cnt;
		}
		String[] mData = new String[cnt];
		int j =0;
		for(int i = dayOfWeekOfLastMonth+1; i<dayOfWeekOfLastMonth+dayNumOfMonth+1; i++){
			String str = MainActivity.getInstance().getBarText();
			mData[j] = str.substring(6,str.length()-1)+"/"+monthData[i];
			j++;
		}
		User.INFO.setDayOfWeekOfLastMonth(dayOfWeekOfLastMonth);
		User.INFO.setmData(mData);
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
	public String getMonthAndDay(int... index) {
		return monthData[index[0]+index[1]];
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
		if(!(xth>0&&xth+7*(yth-1)<dayOfWeekOfLastMonth+2 || xth+7*(yth-1)>dayOfWeekOfLastMonth + dayNumOfMonth + 1)) {
			makeTimePos(xth, yth);
			tempxth = xth;
			tempyth = yth;
		}
	}

	public void getMoveXY(int xth, int yth) {
		if (tempxth != xth || tempyth != yth) {
			if(!(xth>0&&xth+7*(yth-1)<dayOfWeekOfLastMonth+2 || xth+7*(yth-1)>dayOfWeekOfLastMonth + dayNumOfMonth + 1)) {
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
		if(isToday)canvas.drawRect(width * (tx - 1) / 7, height * ((tmp / 7) * 10 + 2) / 64 + 6,width * tx / 7, height * ((tmp / 7 + 1) * 10 + 2) / 64 + 6, hp);
		hp.setAlpha(100);
		canvas.drawText("SUN", width * 1 / 14, height * 2 / 62 - 1, tpred);
		canvas.drawText("MON", width * 3 / 14, height * 2/ 62 - 1, tp);
		canvas.drawText("TUE", width * 5 / 14, height * 2/ 62 - 1, tp);
		canvas.drawText("WED", width * 7 / 14, height * 2/ 62 - 1, tp);
		canvas.drawText("THU", width * 9 / 14, height * 2 / 62 - 1, tp);
		canvas.drawText("FRI", width * 11 / 14, height * 2 / 62 - 1, tp);
		canvas.drawText("SAT", width * 13 / 14, height * 2 / 62 - 1, tpblue);

		for(int i = 0; i<dayOfWeekOfLastMonth+1; i++){
			canvas.drawText(monthData[i], width * (4 * (i % 7) + 1) / 28 - 6, height * ((10 * (i / 7)) + 4) / 64, tpgray);
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
			canvas.drawText(monthData[i], width * (4 * (i%7)+1) / 28-6, height * ((10 * (i/7)) + 4) / 64, tpgray);
		}

	}
	/*public Bitmap captureImg() {
		isLoop=false;
		Bitmap bm = null;
		try{
			Canvas canvas = mholder.lockCanvas();
			bm = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
			canvas.setBitmap(bm);
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
		isLoop=true;
		return bm;
	}*/
}
