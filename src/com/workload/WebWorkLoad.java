/**
 * 
 */
package com.workload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import com.workload.worker;
/**
 * @author PeanoLuca
 *
 */

public class WebWorkLoad {

	/**
	 * @param args
	 * @throws IOException 
	 */

	public static void main(String[] args) {
		Properties Prop=null;
		int frequency=0;
		//Charge Properties.
		try {
			Prop=new WebWorkLoadGetProperties().getPropValues();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}
		System.out.println(Prop.getProperty("clientThreadPoolSize"));
		ThreadPool writerPool=new ThreadPool(1,"Writer");
		worker Worker =new worker(0.0000000000001,new LogWriter(),1,1,1000,worker.PollingAlgorit.ONE_SHOT,worker.WaitAlgorithm.COSTANT_SPEED,"Writer-Pool-1");
		writerPool.addWorker(Worker);
		ArrayList<worker> ClientThreadsPool=new ArrayList<worker>(Integer.parseInt(Prop.getProperty("clientThreadPoolSize")));
		ThreadPool ClientsPool=new ThreadPool(Integer.parseInt(Prop.getProperty("clientThreadPoolSize")),"CLient");
		for(int i=0;i<Integer.parseInt(Prop.getProperty("clientThreadPoolSize"));i++) {
			worker Client = null;
			frequency=Integer.parseInt(Prop.getProperty("frequency"));
			String URL=null;
			int setMillisConnTimeout=0;
			int setMillisSocketTiemout=0;
			int setConnectionRequestTimeout=0;
			int CorePoolSize=0;
			int MaxPoolSize=0;
			int HowMany=0;
			long Reason=0;
			long Stepping=0;
			boolean POSITIV=true;
			double MaxFrequency=0;
			String PollingAlgorit="";
			String WaitAlgorithm="";
			long MaxElapsed=20000;
			if(Prop.getProperty("URL."+i)!=null) {
				URL=Prop.getProperty("URL."+i);
			}else {
				URL=Prop.getProperty("URL");
			}
			if(Prop.getProperty("setMillisConnTimeout."+i)!=null) {
				setMillisConnTimeout=Integer.parseInt(Prop.getProperty("setMillisConnTimeout."+i));
			}else {
				setMillisConnTimeout=Integer.parseInt(Prop.getProperty("setMillisConnTimeout"));
			}
			if(Prop.getProperty("setMillisSocketTiemout."+i)!=null) {
				setMillisConnTimeout=Integer.parseInt(Prop.getProperty("setMillisSocketTiemout."+i));
			}else {
				setMillisConnTimeout=Integer.parseInt(Prop.getProperty("setMillisSocketTiemout"));
			}
			if(Prop.getProperty("setConnectionRequestTimeout."+i)!=null) {
				setConnectionRequestTimeout=Integer.parseInt(Prop.getProperty("setConnectionRequestTimeout."+i));
			}else {
				setConnectionRequestTimeout=Integer.parseInt(Prop.getProperty("setConnectionRequestTimeout"));
			}
			if(Prop.getProperty("CorePoolSize."+i)!=null) {
				CorePoolSize=Integer.parseInt(Prop.getProperty("CorePoolSize."+i));
			}else {
				CorePoolSize=Integer.parseInt(Prop.getProperty("CorePoolSize"));
			}
			if(Prop.getProperty("MaxPoolSize."+i)!=null) {
				MaxPoolSize=Integer.parseInt(Prop.getProperty("MaxPoolSize."+i));
			}else {
				MaxPoolSize=Integer.parseInt(Prop.getProperty("MaxPoolSize"));
			}
			if(Prop.getProperty("PollingAlgorit."+i)!=null) {
				PollingAlgorit=Prop.getProperty("PollingAlgorit."+i);
			}else {
				PollingAlgorit=Prop.getProperty("PollingAlgorit");
			}
			if(Prop.getProperty("HowMany."+i)!=null) {
				HowMany=Integer.parseInt(Prop.getProperty("HowMany."+i));
			}else {
				HowMany=Integer.parseInt(Prop.getProperty("HowMany"));
			}
			if(Prop.getProperty("WaitAlgorithm."+i)!=null) {
				WaitAlgorithm=Prop.getProperty("WaitAlgorithm."+i);
			}else {
				WaitAlgorithm=Prop.getProperty("WaitAlgorithm");
			}
			if(Prop.getProperty("Reason."+i)!=null) {
				Reason=Long.parseLong(Prop.getProperty("Reason."+i));
			}else {
				Reason=Long.parseLong(Prop.getProperty("Reason"));
			}
			if(Prop.getProperty("Stepping."+i)!=null) {
				Stepping=Long.parseLong(Prop.getProperty("Stepping."+i));
			}else {
				Stepping=Long.parseLong(Prop.getProperty("Stepping"));
			}
			if(Prop.getProperty("POSITIV."+i)!=null) {
				POSITIV=Boolean.parseBoolean(Prop.getProperty("POSITIV."+i));
			}else {
				POSITIV=Boolean.parseBoolean(Prop.getProperty("POSITIV"));
			}
			if(Prop.getProperty("MaxFrequency."+i)!=null) {
				MaxFrequency=Double.parseDouble(Prop.getProperty("MaxFrequency."+i));
			}else {
				MaxFrequency=Double.parseDouble(Prop.getProperty("MaxFrequency"));
			}
			/*if(Prop.getProperty("MaxElapsed."+i)!=null) {
				MaxElapsed=Long.parseLong(Prop.getProperty("MaxElapsed."+i));
			}else {
				MaxElapsed=Long.parseLong(Prop.getProperty("MaxElapsed"));
			}*/
			Client=new worker.Builder()
					.setFrequency(frequency)
					.setTargetWork(new ClientHTTP.Builder()
							.setPoolName("Pool-Client-"+i)
							.setURL(URL)
							.setMillisConnTimeout(setMillisConnTimeout)
							.setMillisSocketTiemout(setMillisSocketTiemout)
							.setConnectionRequestTimeout(setConnectionRequestTimeout)
							.setGETMethos(true)
							.build().init())
					.setCorePoolSize(CorePoolSize)
					.setMaxPoolSize(MaxPoolSize)
					.setPollingAlgo(worker.PollingAlgorit.valueOf(PollingAlgorit))
					.setHowMany(HowMany)
					.setWait_Algorithm(worker.WaitAlgorithm.valueOf(WaitAlgorithm))
					.setStepping(Stepping)
					.setReason(Reason)
					.setMaxEleapsed(MaxElapsed)
					.setThreadPoolName("Pool-Client-"+i)
					.setPOSITIV(POSITIV)
					.setMAX_FREQ(MaxFrequency)
					.setProperties(Prop)
					.build();
			ClientsPool.addWorker(Client);
			ClientThreadsPool.add(Client);
		}
		while(true) {
			try {
				
				System.out.println(""+ClientsPool.getexecutor().isTerminated());
				Thread.sleep(1000);
				for (worker cp: ClientThreadsPool) {
					System.out.println("Client                 ID: "+cp.getThreadPoolName());
					System.out.println("Pooled 		  ActiveCount: "+cp.getActiveCount());
					System.out.println("Pooled CompletedTaskCount: "+cp.getCompletedTaskCount());
					System.out.println("Pooled          TaskCount: "+cp.getTaskCount());
					System.out.println("Pooled           Enqueued: "+cp.getQueue().size());
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	

}
