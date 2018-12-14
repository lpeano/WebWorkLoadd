package com.workload;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool extends ExecutorService{
	public ThreadPool(int Size) {
		this.executor=Executors.newFixedThreadPool(Size);
		// TODO Auto-generated constructor stub
	}
	public void addThread(Runnable th) {
		Runnable worker = new th();
		executor.
	}
}
