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
	private int width, height, dayOfWeek; //화면의 전체 너비, 높이
	private String sun,mon,tue,wed,thr,fri,sat;
	Context context;
	private Paint hp; // 1시간 간격 수평선
	private Paint hpvp; // 30분 간격 수평선, 수직선
	private Paint tp,tpred,tpblue,np; // 시간 텍스트
	private Paint datep;
	static int tempxth;
	static int tempyth;
	Canvas canvas;

	public InitMonthThread(SurfaceHolder holder, Context context) {
		DayOfWeekData dowd = CurrentTime.getDateOfWeek();
		this.mholder = holder;
		this.context = context;
		this.sun = dowd.getSun();
		this.mon = dowd.getMon();
		this.tue = dowd.getTue();
		this.wed = dowd.getWed();
		this.thr = dowd.getThr();
		this.fri = dowd.getFri();
		this.sat = dowd.getSat();
		tempxth = 0;
		tempyth = 0;
		hp = new Paint(Paint.ANTI_ALIAS_FLAG);
		hp.setColor(context.getResources().getColor(R.color.maincolor));

		hpvp = new Paint(Paint.ANTI_ALIAS_FLAG);
		hpvp.setAlpha(70);
		np = new Paint(Paint.ANTI_ALIAS_FLAG);
		np.setTextSize(18);
		tp = new Paint(Paint.ANTI_ALIAS_FLAG);
		tp.setTextSize(18);
		tp.setTextAlign(Paint.Align.CENTER);
		tpred = new Paint(Paint.ANTI_ALIAS_FLAG);
		tpred.setTextSize(18);
		tpred.setTextAlign(Paint.Align.CENTER);
		tpred.setColor(context.getResources().getColor(R.color.red));
		tpblue = new Paint(Paint.ANTI_ALIAS_FLAG);
		tpblue.setTextSize(18);
		tpblue.setTextAlign(Paint.Align.CENTER);
		tpblue.setColor(context.getResources().getColor(R.color.blue));

		datep = new Paint(Paint.ANTI_ALIAS_FLAG);
		datep.setColor(Color.parseColor("#104EC7B3"));
		dayOfWeek = CurrentTime.getDayOfWeek();
	}

	public Canvas getCanvas() {
		return canvas;
	}
	public void setCurrentTime(DayOfWeekData dowd){
		this.sun = dowd.getSun();
		this.mon = dowd.getMon();
		this.tue = dowd.getTue();
		this.wed = dowd.getWed();
		this.thr = dowd.getThr();
		this.fri = dowd.getFri();
		this.sat = dowd.getSat();
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
					initScreen(dayOfWeek);
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

	public void initScreen(int day) {

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

		/*canvas.drawText(sun, width * 2 / 15, height / 62, tpred);
		canvas.drawText(mon, width * 4 / 15, height / 62, tp);
		canvas.drawText(tue, width * 6 / 15, height / 62, tp);
		canvas.drawText(wed, width * 8 / 15, height / 62, tp);
		canvas.drawText(thr, width * 10 / 15, height / 62, tp);
		canvas.drawText(fri, width * 12 / 15, height / 62, tp);
		canvas.drawText(sat, width * 14 / 15, height / 62, tpblue);*/

		canvas.drawText("SUN", width * 1 / 14, height * 2/ 62 - 1, tpred);
		canvas.drawText("MON", width * 3 / 14, height * 2/ 62 - 1, tp);
		canvas.drawText("TUE", width * 5 / 14, height * 2/ 62 - 1, tp);
		canvas.drawText("WED", width * 7 / 14, height * 2/ 62 - 1, tp);
		canvas.drawText("THU", width * 9 / 14, height * 2/ 62 - 1, tp);
		canvas.drawText("FRI", width * 11 / 14, height * 2/ 62 - 1, tp);
		canvas.drawText("SAT", width * 13 / 14, height * 2/ 62 - 1, tpblue);

		/*switch (day) {
		case 1: // 월요일
			canvas.drawRect(width * 3 / 15, ((height * 2) - 10) / 64 + 6,
					width * 5 / 15, height * 62 / 64 + 6, datep);
			break;
		case 2: // 화요일
			canvas.drawRect(width * 5 / 15, ((height * 2) - 10) / 64 + 6,
					width * 7 / 15, height * 62 / 64 + 6, datep);
			break;
		case 3: // 수요일
			canvas.drawRect(width * 7 / 15, ((height * 2) - 10) / 64 + 6,
					width * 9 / 15, height * 62 / 64 + 6, datep);
			break;
		case 4: // 목요일
			canvas.drawRect(width * 9 / 15, ((height * 2) - 10) / 64 + 6,
					width * 11 / 15, height * 62 / 64 + 6, datep);
			break;
		case 5: // 금요일
			canvas.drawRect(width * 11 / 15, ((height * 2) - 10) / 64 + 6,
					width * 13 / 15, height * 62 / 64 + 6, datep);
			break;
		case 6: // 토요일
			canvas.drawRect(width * 13 / 15, ((height * 2) - 10) / 64 + 6,
					width * 15 / 15, height * 62 / 64 + 6, datep);
			break;
		case 7: // 일요일
			canvas.drawRect(width * 1 / 15, ((height * 2) - 10) / 64 + 6,
					width * 3 / 15, height * 62 / 64 + 6, datep);
			break;
		}*/

	}

}
