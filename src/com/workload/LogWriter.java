/**
 * 
 */
package com.workload;

import java.util.ArrayList;

/**
 * @author PeanoLuca
 *
 */
public class LogWriter implements Runnable {

	asyncLogger LOG=asyncLogger.getInstance();
	/**
	 * 
	 */
	public LogWriter() {
		// TODO Auto-generated constructor stub
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(0,1000);
				ArrayList<logMessage> al=LOG.getQueued();
				for(logMessage lm : al){
					System.out.println(lm.getMessage()+";"+lm.getTimeStamp().toString());
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
