/**
 * 
 */
package com.workload;

import java.util.concurrent.ThreadFactory;

/**
 * @author PeanoLuca
 *
 */
public class CustomThreadFactory implements ThreadFactory {

	/**
	 * 
	 */
	private String PoolName=null;
	
	public CustomThreadFactory(String PoolName) {
		// TODO Auto-generated constructor stub
		this.PoolName=PoolName;
	}

	@Override
	public Thread newThread(Runnable r) {
		// TODO Auto-generated method stub
		Thread thread = new Thread(r);
		thread.setName(this.PoolName+"-"+thread.getId());
		return thread;
	}

	

}
