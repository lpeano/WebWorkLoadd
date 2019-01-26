/**
 * 
 */
package com.workload;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.message.BasicNameValuePair;

import com.workload.api.WorkerInterface;

/**
 * @author PeanoLuca
 *
 */
public class LogWriter implements WorkerInterface {

	static volatile asyncLogger LOG=asyncLogger.getInstance();
	/**
	 * 
	 */
	public LogWriter() {
		// TODO Auto-generated constructor stub
	}
	
	public void run() {
		long count=1;
		@SuppressWarnings("unused")
		String head="";
		System.out.println("Singleton HASH: "+LOG.hashCode());
	while(true) {
			try {
				Thread.sleep(0,0);
				ArrayList<logMessage> al=LOG.getQueued();
				
				for(logMessage lm : al){
					if(lm.isOk()) {
					Header[] headers=lm.getHeaders();
				
					head="[";
					for(int i=0;i<headers.length;i++) {
						head+="{"+headers[i].getName()+"="+headers[i].getValue()+"}\n";
					}
					head+="]";
					
					}
					
					System.out.println(count+" "+new Timestamp(lm.getStart())+";"+new Timestamp(lm.getEnd())+";"+lm.getMessage()+";"+(lm.getEndRead()-lm.getStart())+";"+(lm.getEnd()-lm.getEndRead())+";"+";"+(lm.getEnd()-lm.getStart())+";");
					count++;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void setTags(String Tags) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPostParameters(ArrayList<BasicNameValuePair> postParameters) {
		// TODO Auto-generated method stub
		
	}


}
