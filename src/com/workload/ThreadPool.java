package com.workload;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {
	ExecutorService executor;
	public AtomicInteger Ready=new AtomicInteger(0);
	public AtomicBoolean Start=new AtomicBoolean(false);
	private String PoolName;
	public ThreadPool(int Size,String PoolName) {
		this.executor=Executors.newFixedThreadPool(Size,new CustomThreadFactory(PoolName));
		this.PoolName=PoolName;
		// TODO Auto-generated constructor stub
	}
	public void addWorker(worker Worker) {
		executor.submit(Worker);
	}
	public ExecutorService getexecutor() {
		return executor;
	}
	public String getPoolName() {
		return PoolName;
	}
	public void setPoolName(String poolName) {
		PoolName = poolName;
	}
	
}
