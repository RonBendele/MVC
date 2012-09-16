package com.musselwhizzle.mvc.models;

import com.musselwhizzle.mvc.events.EventDispatcher;
import com.musselwhizzle.mvc.events.SimpleEvent;

public class AppModel extends EventDispatcher {
	
	public static class ChangeEvent extends SimpleEvent {
		public static final String ELAPSED_TIME_CHANGED = "elapsedTimeChanged";
		
		public ChangeEvent(String type) {
			super(type);
		}
	}
	
	private static AppModel instance;
	
	private long elapsedTime = 0;
	public long getElapsedTime() { return elapsedTime; }
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
		notifyChange(ChangeEvent.ELAPSED_TIME_CHANGED);
	}
	
	private AppModel() {
		super();
	}
	
	public static AppModel getInstance() {
		if (instance == null) instance = new AppModel();
		return instance;
	}
	
	private void notifyChange(String type) {
		dispatchEvent(new ChangeEvent(type));
	}
}