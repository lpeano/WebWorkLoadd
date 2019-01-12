/**
 * 
 */
package com.workload;


import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author PeanoLuca
 *
 */

public class worker extends ThreadPoolExecutor implements Runnable {

	/**
	 * frequency : n/1000 Milliseconds
	 */
	private double frequency;		// Frequency
	private double wavelength;		// WaveLength
	private long HowMany; 			// How Many Tests
	PollingAlgorit pollingAlgo;		// Polling Alogrithm
	WaitAlgorithm Wait_Algorithm;   // Wait Alogrithm
	WorkerInterface targetWork;            // TargetWork
	private String ThreadPoolName;
	private boolean POSITIV=true;
	private double MAX_FREQ=100;
	private String MultiWavePattern="";
	private MultiPattern MultiPatterns=null;
	private Properties properties;
	asyncLogger LOG=asyncLogger.getInstance(); //Logger
	/*
	 * Geometric acceleration parameters
	 */
	private long Reason;		/*Parameter r*/
	private long maxEleapsed;	/*How many seconds (in millis)*/
	private long Stepping;		/* Stepping */
	private long last=0;
	private long Start_Time=0;	/* Start Acceleration time */
	private long N=0;           /* Exponent of Geo Acceleretor */
	private long Moltiplicator=1;
	public worker(double frequency,WorkerInterface targetWork,int CorePoolSize,int MaxPoolSize,long keepaliveTime,PollingAlgorit PA,WaitAlgorithm WA,String ThreadPoolName) {
		super(CorePoolSize,MaxPoolSize,keepaliveTime,TimeUnit.MILLISECONDS,new LinkedTransferQueue<Runnable>(),new CustomThreadFactory(ThreadPoolName+"-worker"));
		super.prestartAllCoreThreads();
		this.pollingAlgo=PA;
		this.Wait_Algorithm=WA;
		this.frequency=frequency;
		this.wavelength=(double)((double)1000/this.frequency);
		this.targetWork=targetWork;
	}
   
	public worker(double frequency,WorkerInterface targetWork,int CorePoolSize,int MaxPoolSize,long keepaliveTime,PollingAlgorit PA,long HowMany,WaitAlgorithm WA,String ThreadPoolName) {
		super(CorePoolSize,MaxPoolSize,keepaliveTime,TimeUnit.MILLISECONDS,new LinkedTransferQueue<Runnable>(),new CustomThreadFactory(ThreadPoolName+"-worker"));
		super.prestartAllCoreThreads();
		this.pollingAlgo=PA;
		this.Wait_Algorithm=WA;
		this.frequency=frequency;
		this.wavelength=(double)((double)1000/this.frequency);
		this.targetWork=targetWork;
		this.HowMany=HowMany;
	}
	
	public worker(Builder builder,String ThreadPoolName) {
		super(builder.CorePoolSize,builder.MaxPoolSize,builder.keepaliveTime,TimeUnit.MILLISECONDS,new LinkedTransferQueue<Runnable>(),new CustomThreadFactory(ThreadPoolName+"-worker"));
		super.prestartAllCoreThreads();
		this.ThreadPoolName=builder.getThreadPoolName();
		this.pollingAlgo=builder.pollingAlgo;
		this.Wait_Algorithm=builder.Wait_Algorithm;
		this.frequency=builder.frequency;
		this.wavelength=(double)((double)1000/builder.frequency);
		this.targetWork=builder.targetWork;
		this.HowMany=builder.HowMany;
		this.Reason=builder.Reason;				/*Parameter r*/
		this.maxEleapsed=builder.maxEleapsed;	/*How many seconds (in millis)*/
		this.Stepping=builder.Stepping;			/* Stepping */
		this.POSITIV=builder.POSITIV;
		this.MAX_FREQ=builder.MAX_FREQ;
		this.MultiWavePattern=builder.MultiWavePattern;
		this.properties=builder.properties;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Init_Worker();
		switch(this.pollingAlgo) {
			case POLLING_FIXED_NUMBER:
				doPolling(this.HowMany);
			break;
			case ONE_SHOT:	
				this.targetWork.setTags("ONE_SHOT");
				doWork_async(targetWork);
			break;
			case POLLING:		
				doPolling();
			break;
			case MULTIWAVE_PATTERN:
				do_MULTIWAVE_PATTERN();
			break;
			default: break;
			}
			
	}
	
	private void do_MULTIWAVE_PATTERN() {
		// TODO Auto-generated method stub
		//boolean isCiclic=this.MultiPatterns.isCiclic();
		this.MultiPatterns.Pattern_Specs.forEach((PatternSpecs  PatterSpec)->{
			long MaxElapsed =(long) PatterSpec.getElapsed();
			long elapsed=0;
			long StartTime=System.currentTimeMillis();
			SetPatternParameters(PatterSpec);
			while(elapsed<=MaxElapsed) {
				this.targetWork.setTags(this.pollingAlgo+"_"+this.Wait_Algorithm+"-"+this.POSITIV+"-"+(1000/this.wavelength));
				doWork_async(targetWork);
				Waint_algorithm();
				elapsed=System.currentTimeMillis()-StartTime;
		}}
		);
	}

	private void SetPatternParameters(PatternSpecs pSpec) {
		switch(WaitAlgorithm.valueOf(pSpec.getPatternName())){
		case GEO_ACCELERATE:
			this.Wait_Algorithm=WaitAlgorithm.GEO_ACCELERATE;
			this.Start_Time=0;
			if(!pSpec.getPatternVariable().get("frequency").equals("continue")) {
				this.frequency=Double.parseDouble(pSpec.getPatternVariable().get("frequency"));
				this.wavelength=1000/this.frequency;
			}
			this.Reason=Long.parseLong(pSpec.getPatternVariable().get("Reason"));
			this.Stepping=Long.parseLong(pSpec.getPatternVariable().get("Stepping"));
			this.POSITIV=Boolean.parseBoolean((pSpec.getPatternVariable().get("POSITIV")));
			this.MAX_FREQ=Double.parseDouble(pSpec.getPatternVariable().get("MaxFrequency"));
			this.maxEleapsed=Long.parseLong(pSpec.getPatternVariable().get("MaxElapsed"));
			break;
		case COSTANT_SPEED:
			this.Start_Time=0;
			this.Wait_Algorithm=WaitAlgorithm.COSTANT_SPEED;
			if(!pSpec.getPatternVariable().get("frequency").equals("continue")) {
				this.frequency=Double.parseDouble(pSpec.getPatternVariable().get("frequency"));
				this.wavelength=1000/this.frequency;
			}
			break;
		default:
			break;
			
		}
	}
	private void doPolling() {
		while(true) {
			this.targetWork.setTags(this.pollingAlgo+"_"+this.Wait_Algorithm+"-"+(1000/this.wavelength));
			doWork_async(targetWork);
			Waint_algorithm();
		}
	}
	private void doPolling(long HowMany) {
		for(long c=0;c<HowMany;c++)
		{
			this.targetWork.setTags(this.pollingAlgo+"_"+this.Wait_Algorithm+"-"+(1000/this.wavelength));
			doWork_async(targetWork);
			Waint_algorithm();
		}
	}
	private void Init_Worker() {
		// TODO Auto-generated method stub
		
		if(this.pollingAlgo==PollingAlgorit.MULTIWAVE_PATTERN) {
			this.MultiPatterns=new MultiPattern();
			this.MultiPatterns.setCiclic(true);
			//String[] multiPatterSequqnce=this.properties.getProperty("MULTIPATTERN_PATTERNS_SEQUENCE").toString().split(" ");
			List<String> multiPatterSequqnce=Arrays.asList(this.properties.getProperty("MULTIPATTERN_PATTERNS_SEQUENCE").toString().split(" "));
			AtomicInteger  counter =new AtomicInteger(0);
			multiPatterSequqnce.forEach((String pattern)->{
				int i=counter.incrementAndGet();
				PatternSpecs pspec=null;
					switch( WaitAlgorithm.valueOf(pattern)) {
					case COSTANT_SPEED:
						pspec=new PatternSpecs.Builder()
								.setElapsed(Double.parseDouble(this.properties.getProperty("MULTIPATTERN_PATTERNS.COSTANT_SPEED."+(i)+".Elapsed")))
								.setPatternName("COSTANT_SPEED")
								.setPatternVariable("frequency", this.properties.getProperty("MULTIPATTERN_PATTERNS.COSTANT_SPEED."+(i)+".frequency"))
								.build();
						this.MultiPatterns.addPatternSpec(pspec);
						break;
					case GEO_ACCELERATE:
						pspec=new PatternSpecs.Builder()
								.setElapsed(Double.parseDouble(this.properties.getProperty("MULTIPATTERN_PATTERNS.GEO_ACCELERATE."+(i)+".Elapsed")))
								.setPatternName("GEO_ACCELERATE")
								.setPatternVariable("frequency", this.properties.getProperty("MULTIPATTERN_PATTERNS.GEO_ACCELERATE."+(i)+".frequency"))
								.setPatternVariable("Reason", this.properties.getProperty("MULTIPATTERN_PATTERNS.GEO_ACCELERATE."+(i)+".Reason"))
								.setPatternVariable("Stepping", this.properties.getProperty("MULTIPATTERN_PATTERNS.GEO_ACCELERATE."+(i)+".Stepping"))
								.setPatternVariable("POSITIV", this.properties.getProperty("MULTIPATTERN_PATTERNS.GEO_ACCELERATE."+(i)+".POSITIV"))
								.setPatternVariable("MaxFrequency", this.properties.getProperty("MULTIPATTERN_PATTERNS.GEO_ACCELERATE."+(i)+".MaxFrequency"))
								.setPatternVariable("MaxElapsed", this.properties.getProperty("MULTIPATTERN_PATTERNS.GEO_ACCELERATE."+(i)+".MaxElapsed"))
								.build();
						this.MultiPatterns.addPatternSpec(pspec);		
						break;
					default:
						break;
						
						
					}
					});
				
			
		}
		
	}

	private void Waint_algorithm() {
		
			try {
				//LOG.put(new logMessage(0, 0, "Queued :  CompletedTaskCount"+this.getCompletedTaskCount()+" ")));
				switch(this.Wait_Algorithm) {
				case COSTANT_SPEED:
					Thread.sleep((long) this.wavelength);
					break;
				case GEO_ACCELERATE:
					/*
					 * Implementation of Vn=V_0r^(n-1)
					 * 
					 * n: stepping in seconds.
					 * 
					 */
					if(this.Start_Time==0) {
						this.Start_Time=System.currentTimeMillis();
						this.last=this.Start_Time;
					} else {
						if(this.maxEleapsed>(System.currentTimeMillis()-this.Start_Time)){
							if(System.currentTimeMillis()-this.last>this.Stepping) {
								this.last=System.currentTimeMillis();
								this.N++;	
								this.Moltiplicator=(long)(Math.pow((double) this.Reason,(double)(this.N-1)));
							
								if(this.isPOSITIV()) {
									if(this.frequency*this.Moltiplicator<this.MAX_FREQ) {
										this.wavelength=(1000/(double)((this.frequency*this.Moltiplicator)));
									} else {
										this.wavelength=(1000/(double)((this.MAX_FREQ)));
									}
									
								} else {
									if((this.frequency/this.Moltiplicator)>this.MAX_FREQ) {
										this.wavelength=(1000/(double)((this.frequency/this.Moltiplicator)));
									}else {
										this.wavelength=(1000/(double)((this.MAX_FREQ)));
									}
								}
								
								
							}
						}
					}
					Thread.sleep((long) this.wavelength);
					break;
				default:
					break;
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	
	private void doWork_async(WorkerInterface targetWork) {
		//Thread doW=new Thread(targetWork);
		this.submit(targetWork);
		//doW.start();
	}
	public String getThreadPoolName() {
		return ThreadPoolName;
	}

	public void setThreadPoolName(String threadPoolName) {
		ThreadPoolName = threadPoolName;
	}
	/**
	 * @return the reason
	 */
	public long getReason() {
		return Reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(long reason) {
		Reason = reason;
	}
	/**
	 * @return the maxEleapsed
	 */
	public long getMaxEleapsed() {
		return maxEleapsed;
	}

	/**
	 * @param maxEleapsed the maxEleapsed to set
	 */
	public void setMaxEleapsed(long maxEleapsed) {
		this.maxEleapsed = maxEleapsed;
	}
	/**
	 * @return the stepping
	 */
	public long getStepping() {
		return Stepping;
	}

	/**
	 * @param stepping the stepping to set
	 */
	public void setStepping(long stepping) {
		Stepping = stepping;
	}
	/**
	 * @return the start_Time
	 */
	public long getStart_Time() {
		return Start_Time;
	}

	/**
	 * @param start_Time the start_Time to set
	 */
	public void setStart_Time(long start_Time) {
		Start_Time = start_Time;
	}
	/**
	 * @return the pOSITIV
	 */
	public boolean isPOSITIV() {
		return POSITIV;
	}

	/**
	 * @param pOSITIV the pOSITIV to set
	 */
	public void setPOSITIV(boolean pOSITIV) {
		POSITIV = pOSITIV;
	}
	/**
	 * @return the mAX_FREQ
	 */
	public double getMAX_FREQ() {
		return MAX_FREQ;
	}

	/**
	 * @param mAX_FREQ the mAX_FREQ to set
	 */
	public void setMAX_FREQ(double mAX_FREQ) {
		MAX_FREQ = mAX_FREQ;
	}
	/**
	 * @return the multiWavePattern
	 */
	public String getMultiWavePattern() {
		return MultiWavePattern;
	}

	/**
	 * @param multiWavePattern the multiWavePattern to set
	 */
	public void setMultiWavePattern(String multiWavePattern) {
		MultiWavePattern = multiWavePattern;
	}
	// Polling Algoritm.
	public enum PollingAlgorit {
		ONE_SHOT("ONESHOT"),
		POLLING("POLLING"),
		POLLING_FIXED_NUMBER("POLLING_FIXED_NUMBER"),
		MULTIWAVE_PATTERN("MULTIWAVE_PATTERN");
		private String ALGORITHM;
		PollingAlgorit(String ALGORITHM){
			this.ALGORITHM=ALGORITHM;
		}
		public String Algorithm() {
			return ALGORITHM;
		}
		public PollingAlgorit getValueOf(String algo) {
			return PollingAlgorit.valueOf(algo);
		}
	}
	// Wait Algoritmh
	public enum WaitAlgorithm {
		COSTANT_SPEED("COSTANT_SPEED"), 
		COSTANT_ACCELERATE("COSTANT_ACCELERATE"),
		GEO_ACCELERATE("GEO_ACCELERATE");
		
		private String WaitAlgo;
		WaitAlgorithm(String Walgo){
			this.WaitAlgo=Walgo;
		}
		public String Algorithm(){
			return WaitAlgo;
		}
		public WaitAlgorithm getValueOf( String WaitAlgo) {
			return WaitAlgorithm.valueOf(WaitAlgo);
		}
	}
	
	/*
	 * Builder Class
	 */
	public static class Builder {
		private double frequency;		// Frequency
		private double wavelength;		// WaveLength
		private long HowMany; 			// How Many Tests
		private PollingAlgorit pollingAlgo;		// Polling Alogrithm
		private WaitAlgorithm Wait_Algorithm;   // Wait Alogrithm
		private WorkerInterface targetWork;            // TargetWork
		private String ThreadPoolName;
		private int CorePoolSize;
		private int MaxPoolSize;
		private long keepaliveTime;
		private long Reason;		/*Parameter r*/
		private long maxEleapsed;	/*How many seconds (in millis)*/
		private long Stepping;		/* Stepping */		
		private boolean POSITIV=true;
		private double MAX_FREQ=40;
		private String MultiWavePattern="";
		private Properties properties;
		public Builder setCorePoolSize(int CorePoolSize) {
			this.CorePoolSize = CorePoolSize;
			return this;
		}
		public Builder setMaxPoolSize(int MaxPoolSize) {
			this.MaxPoolSize = MaxPoolSize;
			return this;
		}
		public Builder setkeepaliveTime(long keepaliveTime) {
			this.keepaliveTime = keepaliveTime;
			return this;
		}
		public double getFrequency() {
			return frequency;
		}
		public Builder setFrequency(double frequency) {
			this.frequency = frequency;
			return this;
		}
		/**
		 * @return the wavelength
		 */
		public double getWavelength() {
			return wavelength;
		}
		/**
		 * @param wavelength the wavelength to set
		 */
		public Builder setWavelength(double wavelength) {
			this.wavelength = wavelength;
			return this;
		}
		/**
		 * @return the howMany
		 */
		public long getHowMany() {
			return HowMany;
		}
		/**
		 * @param howMany the howMany to set
		 */
		public Builder setHowMany(long howMany) {
			HowMany = howMany;
			return this;
		}
		/**
		 * @return the threadPoolName
		 */
		public String getThreadPoolName() {
			return ThreadPoolName;
		}
		/**
		 * @param threadPoolName the threadPoolName to set
		 */
		public Builder setThreadPoolName(String threadPoolName) {
			ThreadPoolName = threadPoolName;
			return this;
		}
		/**
		 * @return the pollingAlgo
		 */
		public PollingAlgorit getPollingAlgo() {
			return pollingAlgo;
		}
		/**
		 * @param pollingAlgo the pollingAlgo to set
		 */
		public Builder setPollingAlgo(PollingAlgorit pollingAlgo) {
			this.pollingAlgo = pollingAlgo;
			return this;
		}
		/**
		 * @return the wait_Algorithm
		 */
		public WaitAlgorithm getWait_Algorithm() {
			return Wait_Algorithm;
		}
		/**
		 * @param wait_Algorithm the wait_Algorithm to set
		 */
		public Builder setWait_Algorithm(WaitAlgorithm wait_Algorithm) {
			Wait_Algorithm = wait_Algorithm;
			return this;
		}
		/**
		 * @return the targetWork
		 */
		public WorkerInterface getTargetWork() {
			return this.targetWork;
		}
		/**
		 * @param targetWork the targetWork to set
		 */
		public Builder setTargetWork(WorkerInterface targetWork) {
			this.targetWork = targetWork;
			return this;
		}
		public worker build() {
			worker Worker = new worker(this,this.ThreadPoolName);
			return Worker;
		}
		/**
		 * @return the reason
		 */
		public long getReason() {
			return Reason;
		}
		/**
		 * @param reason the reason to set
		 */
		public Builder setReason(long reason) {
			Reason = reason;
			return this;
		}
		/**
		 * @return the maxEleapsed
		 */
		public long getMaxEleapsed() {
			return maxEleapsed;
		}
		/**
		 * @param maxEleapsed the maxEleapsed to set
		 */
		public Builder setMaxEleapsed(long maxEleapsed) {
			this.maxEleapsed = maxEleapsed;
			return this;
		}
		/**
		 * @return the stepping
		 */
		public long getStepping() {
			return Stepping;
		}
		/**
		 * @param stepping the stepping to set
		 */
		public Builder setStepping(long stepping) {
			Stepping = stepping;
			return this;
		}
		/**
		 * @return the pOSITIV
		 */
		public boolean isPOSITIV() {
			return POSITIV;
		}
		/**
		 * @param pOSITIV the pOSITIV to set
		 */
		public Builder setPOSITIV(boolean pOSITIV) {
			POSITIV = pOSITIV;
			return this;
		}
		/**
		 * @return the mAX_FREQ
		 */
		public double getMAX_FREQ() {
			return MAX_FREQ;
		}
		/**
		 * @param mAX_FREQ the mAX_FREQ to set
		 */
		public Builder setMAX_FREQ(double mAX_FREQ) {
			MAX_FREQ = mAX_FREQ;
			return this;
		}
		/**
		 * @return the multiWavePattern
		 */
		public String getMultiWavePattern() {
			return MultiWavePattern;
		}
		/**
		 * @param multiWavePattern the multiWavePattern to set
		 */
		public Builder setMultiWavePattern(String multiWavePattern) {
			MultiWavePattern = multiWavePattern;
			return this;
		}
		/**
		 * @return the properties
		 */
		public Properties getProperties() {
			return properties;
		}
		/**
		 * @param properties the properties to set
		 */
		public Builder setProperties(Properties properties) {
			this.properties = properties;
			return this;
		}
	}
	
}
