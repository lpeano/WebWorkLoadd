/**
 * 
 */
package com.workload.api;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

/**
 * @author PeanoLuca
 *
 */
public interface WorkerInterface extends Runnable {
	/**
	 * @param Tags
	 */
	public void setTags(String Tags);
	//public <T> void setPostParameters(ArrayList<T> postParameters);
	/**
	 * @param postParameters
	 */
	void setPostParameters(ArrayList<BasicNameValuePair> postParameters);
}
