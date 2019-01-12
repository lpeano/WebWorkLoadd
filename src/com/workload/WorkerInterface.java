/**
 * 
 */
package com.workload;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

/**
 * @author PeanoLuca
 *
 */
interface WorkerInterface extends Runnable {
	public void setTags(String Tags);
	//public <T> void setPostParameters(ArrayList<T> postParameters);
	void setPostParameters(ArrayList<BasicNameValuePair> postParameters);
}
