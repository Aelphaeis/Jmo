package jmo.util;

import static org.junit.Assert.*;

import org.junit.Test;

import jmo.util.StopWatch;

public class StopwatchTest {

	@Test
	public void stopWatchSuccess() throws InterruptedException {
		StopWatch stopWatch = new StopWatch();
		
		//Check state before starting
		assertEquals(false, stopWatch.isRunning());
		assertEquals(false, stopWatch.isStarted());
		
		stopWatch.start();
		
		//Make sure state is correct after starting
		assertEquals(true, stopWatch.isRunning());
		assertEquals(true, stopWatch.isStarted());
		
		Thread.sleep(100);
		long split = stopWatch.getTime();

		//Check that stopwatch actually did start.
		assertTrue(split > 0);
		
		Thread.sleep(100);
		stopWatch.stop();
		
		//Make sure state is correct after stopping
		assertEquals( true, stopWatch.isStarted());
		assertEquals( false, stopWatch.isRunning());
		
		
		//Make sure time is a valid positive number
		assertTrue(stopWatch.getTime() > 0);
		assertTrue(stopWatch.getTime() > split);

		stopWatch.reset();
		
		//Make sure state is back to before starting.
		assertEquals(false, stopWatch.isRunning());
		assertEquals(false, stopWatch.isStarted());
	}
	
	@Test(expected=IllegalStateException.class)
	public void stopWatchStopFailure(){
		StopWatch stopWatch = new StopWatch();
		stopWatch.stop();
	}
	
	@Test(expected=IllegalStateException.class)
	public void stopWatchDoubleStartFailure(){
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		stopWatch.start();
	}
	
	@Test(expected=IllegalStateException.class)
	public void stopWatchGetTimeBeforeStartFailure1(){
		StopWatch stopWatch = new StopWatch();
		stopWatch.getTime();
	}
	
	@Test(expected=IllegalStateException.class)
	public void stopWatchGetTimeBeforeStartFailure2(){
		StopWatch stopWatch = new StopWatch();
		stopWatch.getStartTime();
	}
	
	@Test(expected=IllegalStateException.class)
	public void stopWatchGetTimeBeforeStartFailure3(){
		StopWatch stopWatch = new StopWatch();
		stopWatch.getStopTime();
	}
	
	@Test(expected=IllegalStateException.class)
	public void stopWatchGetStopTimeBeforeStopFailure3(){
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		stopWatch.getStopTime();
	}
}
