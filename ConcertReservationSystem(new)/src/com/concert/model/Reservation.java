package com.concert.model;

public class Reservation {

	/** 회원id : 식별키 */
	public int infoCode;
	
	/** 비밀번호 : 필수 */
	public String cusName;
	
	/** 이름 : 필수 */
	public int stGrade;
	
	/** 휴대폰 : 필수 */
	public int stNum;

	public Reservation() {
		super();
	}

	public Reservation(int infoCode, String cusName, int stGrade, int stNum) {
		super();
		this.infoCode = infoCode;
		this.cusName = cusName;
		this.stGrade = stGrade;
		this.stNum = stNum;
	}

	public int getInfoCode() {
		return infoCode;
	}

	public void setInfoCode(int infoCode) {
		this.infoCode = infoCode;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public int getStGrade() {
		return stGrade;
	}

	public void setStGrade(int stGrade) {
		this.stGrade = stGrade;
	}

	public int getStNum() {
		return stNum;
	}

	public void setStNum(int stNum) {
		this.stNum = stNum;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + infoCode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reservation other = (Reservation) obj;
		if (infoCode != other.infoCode)
			return false;
		return true;
	}
	
	
	

	
	
}
