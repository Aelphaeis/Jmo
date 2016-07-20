package jmo.util;

public class StopWatch {
	
	boolean running;
	boolean started;
	long startTime;
	long stopTime;

	String name;

	public StopWatch(){
		running = false;
		started = false;
		startTime = 0;
		stopTime = 0;
	}
	
	public StopWatch(String name){
		this();
		this.name = name;
	}
	
	public void start(){
		if(running){
			throw new IllegalStateException("Stopwatch already started");
		}
		
		startTime = System.currentTimeMillis();
		started = running = true;
	}
	
	public void stop(){
		if(!started){
			throw new IllegalStateException("Stopwatch not started");
		}
		stopTime = System.currentTimeMillis();
		running = false;
	}
	
	public long getTime(){
		if(!started){
			throw new IllegalStateException("Stopwatch not started");
		}
		
		if(running){
			return System.currentTimeMillis() - startTime;
		}
		else{
			return stopTime - startTime;
		}
	}
	
	public void reset(){
		if(running){
			stop();
		}
		running = false;
		started = false;
		startTime = 0;
		stopTime = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isRunning() {
		return running;
	}

	public boolean isStarted() {
		return started;
	}

	public long getStartTime() {
		if(isStarted()){
			return startTime;
		}
		throw new IllegalStateException("Stopwatch has not been started");
	}

	public long getStopTime() {
		if(isStarted() && !isRunning()){
			return stopTime;
		}
		throw new IllegalStateException("Stopwatch has not been stopped.");
	}
}
