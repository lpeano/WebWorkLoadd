/**
 * 
 */
package com.workload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.LinkedTransferQueue;

/**
 * @author PeanoLuca
 *
 */

public class asyncLogger extends LinkedTransferQueue<logMessage>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static volatile asyncLogger instance;
	private asyncLogger() {
		super();
	}
	
	public static asyncLogger getInstance() {
		if(instance==null) {
			synchronized (asyncLogger.class){
				if(instance==null) {
					instance = new asyncLogger();
				}
			}
		}
		return instance;
	}
	public void put(logMessage LogMessage) {
			super.put(LogMessage);
	}
	public ArrayList<logMessage> getQueued(){
		ArrayList<logMessage> c = new ArrayList<logMessage>();
		super.drainTo(c);
		return c;
	}
	

}
