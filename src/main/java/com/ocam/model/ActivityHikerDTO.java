package com.ocam.model;

public class ActivityHikerDTO {

	private Hiker hiker;
	private Activity activity;
	private String requestUser;

	public Hiker getHiker() {
		return hiker;
	}

	public void setHiker(Hiker hiker) {
		this.hiker = hiker;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public String getRequestUser() {
		return requestUser;
	}

	public void setRequestUser(String requestUser) {
		this.requestUser = requestUser;
	}
}
