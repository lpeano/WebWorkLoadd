package com.workload;

//import java.sql.Timestamp;

import org.apache.http.Header;

public class logMessage {
	private String Message="";
	private long Start=0;
	private long End=0;
	private long EndRead=0;
	private boolean isOk=true;
	private Header[] Headers;
	private String Body="";
	private String Tags="";
	
	public logMessage(long start,long EndRead,long end,String Message,Header[] Headers,String Body,boolean isOk) {
		// TODO Auto-generated constructor stub
		this.setMessage(Message);
		this.setStart(start);
		this.setEnd(end);
		this.setEndRead(EndRead);
		this.setHeaders(Headers);
		this.Body=Body;
		this.isOk=isOk;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public long getStart() {
		return Start;
	}
	public void setStart(long start) {
		Start = start;
	}
	public long getEnd() {
		return End;
	}
	public void setEnd(long end) {
		this.End = end;
	}
	public long getEndRead() {
		return EndRead;
	}
	public void setEndRead(long endRead) {
		EndRead = endRead;
	}
	public Header[] getHeaders() {
		return Headers;
	}
	public void setHeaders(Header[] headers) {
		Headers = headers;
	}
	public String getBody() {
		return Body;
	}
	public void setBody(String body) {
		Body = body;
	}
	public boolean isOk() {
		return isOk;
	}
	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}
	/**
	 * @return the tags
	 */
	public String getTags() {
		return Tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public logMessage setTags(String tags) {
		Tags = tags;
		return this;
	}

}
