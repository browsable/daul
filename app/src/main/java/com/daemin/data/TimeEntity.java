package com.daemin.data;

public class TimeEntity {

	// private variables
	private int id;
	private int share; // 공유여부 설정 (0 : 공유안함, 1 : 친구공개, 2 : 전체공개, 3 : 주변공개)
	private String xyth; // xyth timecode.xls 참조
	private String place; // 장소
	private String content; // 스케쥴 제목 , 대학은 수강과목명이 이 곳에 들어감
	private String subcontent; // 대학 일반의 팝업의 메모부분에 들어가는 부가적 정보 (교수님성함, 학점, 기타
								// 스케쥴관련 부가내용)
	private String contentcode;
	private String daycode; // xyth+날짜 0101+150120 : ex) 0101150120 의 의미는 월요일
							// 8시(xyth:0101) 15년 01월 20일을 의미

	public TimeEntity(int _id, int share, String xyth, String place,
			String content, String subcontent, String contentcode,
			String daycode) {
		super();
		this.id = _id;
		this.share = share;
		this.xyth = xyth;
		this.place = place;
		this.content = content;
		this.subcontent = subcontent;
		this.contentcode = contentcode;
		this.daycode = daycode;
	}

	public TimeEntity() {
	}

	public int getid() {
		return id;
	}

	public void setid(int id) {
		this.id =id;
	}

	public String getXyth() {
		return xyth;
	}

	public void setXyth(String xyth) {
		this.xyth = xyth;
	}

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubcontent() {
		return subcontent;
	}

	public void setSubcontent(String subcontent) {
		this.subcontent = subcontent;
	}

	public String getContentcode() {
		return contentcode;
	}

	public void setContentcode(String contentcode) {
		/*
		 * 단편화와 조립화 contentcode가 같은 시간들은 공통 속성의 시간계획으로 묶여져있음을 의미 contentcode
		 * mon0800 s1 mon0830 s1 : contentcode가 같으므로 월 8시와 8시 30분간에 같은 스케쥴로
		 * 묶여있음을 알 수 있음 contentcode 부여방법 : 수강과목의 콘텐트 코드같은 경우는 학교+교과목코드+분반 학교마다
		 * 인덱스를 줌 이를테면 한기대의(koreatech가 대학코드로 1을 부여받았다면) 재료역학(MEB201)의 1분반의 콘텐트
		 * 코드는 1+MEB201+1 = 1MEB2011 고유 코드 부여
		 * 
		 * 일반 스케쥴의 콘텐트 코드를 유니크하게 부여하려면 폰번호+timecode(1~210) ex) 01052998236의 경우 앞
		 * 010을 timecode로 교체 월요일0800(부여된 timecode: 1)이면 1+52998236 = 152998236이
		 * 됨
		 */

		this.contentcode = contentcode;
	}

	public String getDaycode() {
		return daycode;
	}

	public void setDaycode(String daycode) {
		this.daycode = daycode;
	}

}
