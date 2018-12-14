package com.workload;

import java.sql.Timestamp;

public class logMessage {
	private String Message;
	private Timestamp timeStamp;
	public logMessage(Timestamp t,String message) {
		// TODO Auto-generated constructor stub
		this.setMessage(message);
		this.setTimeStamp(t);
	}
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}

}
