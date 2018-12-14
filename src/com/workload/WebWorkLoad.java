/**
 * 
 */
package com.workload;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @author PeanoLuca
 *
 */
public class WebWorkLoad {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		asyncLogger LOG=asyncLogger.getInstance();
		LOG.put(new logMessage(new Timestamp(System.currentTimeMillis()), "prova"));
		LOG.put(new logMessage(new Timestamp(System.currentTimeMillis()), "prova"));
		LOG.put(new logMessage(new Timestamp(System.currentTimeMillis()), "prova"));
		@SuppressWarnings("unused")
		ArrayList<logMessage> al=LOG.getQueued();
		
	}

}
