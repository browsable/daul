package com.daemin.timetable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.common.CurrentTime;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.main.SubMainActivity;

@SuppressLint("DefaultLocale")
public class InitWeekThread extends InitThread {
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

	public InitWeekThread(SurfaceHolder holder, Context context) {
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

	public void makeTimePos(int xth, int yth, String touchType) {
		TimePos ETP = TimePos.valueOf(Convert.getxyMerge(xth, yth));
		switch(DrawMode.CURRENT.getMode()) {
			case 0: case 3://일반
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
				if (Common.checkTableStateIsNothing&& Common.isLlIncludeDepIn()) {
					Toast.makeText(context,"과목을 선택하세요",Toast.LENGTH_SHORT).show();
				}else {
					Common.stateFilter(Common.getTempTimePos());
				}
				break;
			case 2: //추천
				if (ETP.getPosState() == PosState.NO_PAINT) {
					Common.stateFilter(Common.getTempTimePos());
					ETP.setPosState(PosState.RECOMMEND);
					Common.getTempTimePos().add(ETP.name());
					SubMainActivity.getInstance().setupRecommendDatas(ETP.name());
				}else{
					Common.stateFilter(Common.getTempTimePos());
				}
				break;
		}
		return;
	}

	public void initScreen(int day) {

		float[] hp_hour = {
				// 가로선 : 1시간 간격
				26, height / 32 + 6, width, height / 32 + 6 , 26, height * 3 / 32 + 6, width,
				height * 3 / 32 + 6, 26, height * 5 / 32 + 6, width, height * 5 / 32 + 6, 26,
				height * 7 / 32 + 6, width, height * 7 / 32 + 6, 26, height * 9 / 32 + 6, width,
				height * 9 / 32 + 6, 26, height * 11 / 32 + 6, width, height * 11 / 32 + 6, 26,
				height * 13 / 32 + 6, width, height * 13 / 32 + 6, 26, height * 15 / 32 + 6, width,
				height * 15 / 32 + 6, 26, height * 17 / 32 + 6, width, height * 17 / 32 + 6, 26,
				height * 19 / 32 + 6, width, height * 19 / 32 + 6, 26, height * 21 / 32 + 6, width,
				height * 21 / 32 + 6, 26, height * 23 / 32 + 6, width, height * 23 / 32 + 6, 26,
				height * 25 / 32 + 6, width, height * 25 / 32 + 6, 26, height * 27 / 32 + 6, width,
				height * 27 / 32 + 6, 26, height * 29 / 32 + 6, width, height * 29 / 32 + 6, 26,
				height * 31 / 32 + 6, width, height * 31 / 32 + 6 };
		float[] hp_half = {
				// 가로선 : 30분 간격
				0, height * 2 / 32+ 6, width, height * 2 / 32+ 6, 0, height * 4 / 32 + 6, width,
				height * 4 / 32 + 6, 0, height * 6 / 32 + 6, width, height * 6 / 32 + 6, 0,
				height * 8 / 32 + 6, width, height * 8 / 32+ 6, 0, height * 10 / 32 + 6, width,
				height * 10 / 32 + 6, 0, height * 12 / 32 + 6, width, height * 12 / 32 + 6, 0,
				height * 14 / 32 + 6, width, height * 14 / 32, 0, height * 16 / 32, width,
				height * 16 / 32 + 6, 0, height * 18 / 32 + 6, width, height * 18 / 32 + 6, 0,
				height * 20 / 32 + 6, width, height * 20 / 32, 0, height * 22 / 32, width,
				height * 22 / 32 + 6, 0, height * 24 / 32 + 6, width, height * 24 / 32 + 6, 0,
				height * 26 / 32 + 6, width, height * 26 / 32, 0, height * 28 / 32, width,
				height * 28 / 32 + 6, 0, height * 30 / 32 + 6, width, height * 30 / 32 + 6 };

		float[] vp = {
				// 세로 선
				width / 15, height / 32 + 6, width / 15, height * 31 / 32 + 6, width * 3 / 15,
				height / 32 + 6, width * 3 / 15, height * 31 / 32 + 6, width * 5 / 15,
				height / 32 + 6, width * 5 / 15, height * 31 / 32 + 6, width * 7 / 15,
				height / 32 + 6, width * 7 / 15, height * 31 / 32 + 6, width * 9 / 15,
				height / 32 + 6, width * 9 / 15, height * 31 / 32 + 6, width * 11 / 15,
				height / 32 + 6, width * 11 / 15, height * 31 / 32 + 6, width * 13 / 15,
				height / 32 + 6, width * 13 / 15, height * 31 / 32 + 6 };

		canvas.drawColor(Color.WHITE);
		canvas.drawLines(hp_hour, hp);
		canvas.drawLines(hp_half, hpvp);
		canvas.drawLines(vp, hpvp);
		canvas.drawText("8", 12, height * 1 / 32 + 9, np);
		canvas.drawText("9", 12, height * 3 / 32 + 9, np);
		for (int i = 2; i < 16; i++) {
			canvas.drawText(String.valueOf(i + 8), 2,
					((2 * i + 1) * height / 32) + 9, np);
		}
		canvas.drawText(sun, width * 2 / 15, height / 62, tpred);
		canvas.drawText(mon, width * 4 / 15, height / 62, tp);
		canvas.drawText(tue, width * 6 / 15, height / 62, tp);
		canvas.drawText(wed, width * 8 / 15, height / 62, tp);
		canvas.drawText(thr, width * 10 / 15, height / 62, tp);
		canvas.drawText(fri, width * 12 / 15, height / 62, tp);
		canvas.drawText(sat, width * 14 / 15, height / 62, tpblue);

		canvas.drawText("SUN", width * 2 / 15, height * 2/ 62 - 1, tpred);
		canvas.drawText("MON", width * 4 / 15, height * 2/ 62 - 1, tp);
		canvas.drawText("TUE", width * 6 / 15, height * 2/ 62 - 1, tp);
		canvas.drawText("WED", width * 8 / 15, height * 2/ 62 - 1, tp);
		canvas.drawText("THU", width * 10 / 15, height * 2/ 62 - 1, tp);
		canvas.drawText("FRI", width * 12 / 15, height * 2/ 62 - 1, tp);
		canvas.drawText("SAT", width * 14 / 15, height * 2/ 62 - 1, tpblue);

		switch (day) {
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
		}

	}
}
