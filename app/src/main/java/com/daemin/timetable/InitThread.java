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
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;

@SuppressLint("DefaultLocale")
public class InitThread extends Thread {
	SurfaceHolder mholder;
	private boolean isLoop = true;
	private int width, height; //화면의 전체 너비, 높이
	Context context;
	private Paint hp; // 1시간 간격 수평선
	private Paint hpvp; // 30분 간격 수평선, 수직선
	private Paint tp; // 시간 텍스트
	private Paint rp; // 사각형
	private Paint datep;
	static int tempxth;
	static int tempyth;
	Canvas canvas;
	CurrentTime ct;

	public InitThread(SurfaceHolder holder, Context context) {
		this.mholder = holder;
		this.context = context;
		tempxth = 0;
		tempyth = 0;
		hp = new Paint(Paint.ANTI_ALIAS_FLAG);
		hp.setColor(context.getResources().getColor(R.color.maincolor));

		hpvp = new Paint(Paint.ANTI_ALIAS_FLAG);
		hpvp.setAlpha(70);

		tp = new Paint(Paint.ANTI_ALIAS_FLAG);
		tp.setTextSize(18);


		datep = new Paint(Paint.ANTI_ALIAS_FLAG);
		datep.setColor(Color.parseColor("#104EC7B3"));
		ct = new CurrentTime();
	}

	public Canvas getCanvas() {
		return canvas;
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
					initScreen(ct.getDayOfWeekIndex());
					Common.checkTableStateIsNothing = true;

					// 사각형 그리기
					for (TimePos ETP : TimePos.values()) {
						ETP.drawTimePos(canvas, width, height);
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
		makeTimePos(xth, yth, "down");
		tempxth = xth;
		tempyth = yth;
	}

	public void getMoveXY(int xth, int yth) {
		if (tempxth != xth || tempyth != yth) {
			makeTimePos(xth, yth, "move");
			tempxth = xth;
			tempyth = yth;
		}
	}

	public void ActionUp() {
		// EnumTimePos.valueOf(Convert.getxyMerge(xth,
		// yth)).setPosState(PosState.END);
		/*
		 * for (EnumTimePos ETP : EnumTimePos.values()) { if (ETP.getPosState()
		 * != PosState.NO_PAINT) { Log.i(ETP.name(),
		 * String.valueOf(ETP.getGroupNumber())); } }
		 */
	}

	public void makeTimePos(int xth, int yth, String touchType) {
		TimePos ETP = TimePos.valueOf(Convert.getxyMerge(xth, yth));
		switch(DrawMode.CURRENT.getMode()) {
			case 0: //일반
				if (ETP.getPosState() == PosState.NO_PAINT) {
					if (touchType.equals("down")) {
						ETP.setPosState(PosState.START);
						Common.getTempTimePos().add(ETP.name());
					} else {
						ETP.setPosState(PosState.INTERMEDIATE);
						Common.getTempTimePos().add(ETP.name());
					}
				} else {
					ETP.setPosState(PosState.NO_PAINT);
				}
				break;
			case 1: //대학
				//대학선택시에 그리는 것은 막고 선택한 과목은 함께 지워져야함
					Common.stateFilter(Common.getTempTimePos());
				break;
			case 2: //추천
				if (ETP.getPosState() == PosState.NO_PAINT) {
					Common.stateFilter(Common.getTempTimePos());
					ETP.setPosState(PosState.RECOMMEND);
					Common.getTempTimePos().add(ETP.name());
				}else{
					ETP.setPosState(PosState.NO_PAINT);
				}
				break;
		}
		return;
	}

	public void initScreen(int day) {

		float[] hp_hour = {
				// 가로선 : 1시간 간격
				26, height / 32, width, height / 32, 26, height * 3 / 32, width,
				height * 3 / 32, 26, height * 5 / 32, width, height * 5 / 32, 26,
				height * 7 / 32, width, height * 7 / 32, 26, height * 9 / 32, width,
				height * 9 / 32, 26, height * 11 / 32, width, height * 11 / 32, 26,
				height * 13 / 32, width, height * 13 / 32, 26, height * 15 / 32, width,
				height * 15 / 32, 26, height * 17 / 32, width, height * 17 / 32, 26,
				height * 19 / 32, width, height * 19 / 32, 26, height * 21 / 32, width,
				height * 21 / 32, 26, height * 23 / 32, width, height * 23 / 32, 26,
				height * 25 / 32, width, height * 25 / 32, 26, height * 27 / 32, width,
				height * 27 / 32, 26, height * 29 / 32, width, height * 29 / 32, 26,
				height * 31 / 32, width, height * 31 / 32 };
		float[] hp_half = {
				// 가로선 : 30분 간격
				0, height * 2 / 32, width, height * 2 / 32, 0, height * 4 / 32, width,
				height * 4 / 32, 0, height * 6 / 32, width, height * 6 / 32, 0,
				height * 8 / 32, width, height * 8 / 32, 0, height * 10 / 32, width,
				height * 10 / 32, 0, height * 12 / 32, width, height * 12 / 32, 0,
				height * 14 / 32, width, height * 14 / 32, 0, height * 16 / 32, width,
				height * 16 / 32, 0, height * 18 / 32, width, height * 18 / 32, 0,
				height * 20 / 32, width, height * 20 / 32, 0, height * 22 / 32, width,
				height * 22 / 32, 0, height * 24 / 32, width, height * 24 / 32, 0,
				height * 26 / 32, width, height * 26 / 32, 0, height * 28 / 32, width,
				height * 28 / 32, 0, height * 30 / 32, width, height * 30 / 32 };

		float[] vp = {
				// 세로 선
				width / 15, height / 32, width / 15, height * 31 / 32, width * 3 / 15,
				height / 32, width * 3 / 15, height * 31 / 32, width * 5 / 15,
				height / 32, width * 5 / 15, height * 31 / 32, width * 7 / 15,
				height / 32, width * 7 / 15, height * 31 / 32, width * 9 / 15,
				height / 32, width * 9 / 15, height * 31 / 32, width * 11 / 15,
				height / 32, width * 11 / 15, height * 31 / 32, width * 13 / 15,
				height / 32, width * 13 / 15, height * 31 / 32, };

		canvas.drawColor(Color.WHITE);
		canvas.drawLines(hp_hour, hp);
		canvas.drawLines(hp_half, hpvp);
		canvas.drawLines(vp, hpvp);
		canvas.drawText("8", 12, height * 1 / 32 + 7, tp);
		canvas.drawText("9", 12, height * 3 / 32 + 7, tp);
		for (int i = 2; i < 16; i++) {
			canvas.drawText(String.valueOf(i + 8), 2,
					((2 * i + 1) * height / 32) + 7, tp);
		}
		canvas.drawText("MON", width * 2 / 15 - 21, height / 62 + 4, tp);
		canvas.drawText("TUE", width * 4 / 15 - 16, height / 62 + 4, tp);
		canvas.drawText("WED", width * 6 / 15 - 20, height / 62 + 4, tp);
		canvas.drawText("THU", width * 8 / 15 - 17, height / 62 + 4, tp);
		canvas.drawText("FRI", width * 10 / 15 - 15, height / 62 + 4, tp);
		canvas.drawText("SAT", width * 12 / 15 - 17, height / 62 + 4, tp);
		canvas.drawText("SUN", width * 14 / 15 - 17, height / 62 + 4, tp);

		switch (day) {
		case 1: // 일요일
			canvas.drawRect(width * 13 / 15, ((height * 2) - 10) / 64,
					width * 15 / 15, height * 62 / 64, datep);
			break;
		case 2: // 월요일
			canvas.drawRect(width * 1 / 15, ((height * 2) - 10) / 64,
					width * 3 / 15, height * 62 / 64, datep);
			break;
		case 3: // 화요일
			canvas.drawRect(width * 3 / 15, ((height * 2) - 10) / 64,
					width * 5 / 15, height * 62 / 64, datep);
			break;
		case 4: // 수요일
			canvas.drawRect(width * 5 / 15, ((height * 2) - 10) / 64,
					width * 7 / 15, height * 62 / 64, datep);
			break;
		case 5: // 목요일
			canvas.drawRect(width * 7 / 15, ((height * 2) - 10) / 64,
					width * 9 / 15, height * 62 / 64, datep);
			break;
		case 6: // 금요일
			canvas.drawRect(width * 9 / 15, ((height * 2) - 10) / 64,
					width * 11 / 15, height * 62 / 64, datep);
			break;
		case 7: // 토요일
			canvas.drawRect(width * 11 / 15, ((height * 2) - 10) / 64,
					width * 13 / 15, height * 62 / 64, datep);
			break;
		}

	}

}
