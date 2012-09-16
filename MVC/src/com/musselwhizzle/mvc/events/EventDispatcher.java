package com.musselwhizzle.mvc.events;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import android.util.Log;


public class EventDispatcher implements Dispatcher {
	
	private static final String TAG = EventDispatcher.class.getSimpleName();
	
	private HashMap<String, CopyOnWriteArrayList<EventListener>> listenerMap;
	private Dispatcher target;
	
	public EventDispatcher() {
		this(null);
	}
	public EventDispatcher(Dispatcher target) {
		listenerMap = new HashMap<String, CopyOnWriteArrayList<EventListener>>();
		this.target = (target != null) ? target : this;
	}
	
	@Override
	public void addListener(String type, EventListener listener) {
		synchronized (listenerMap) {
			CopyOnWriteArrayList<EventListener> list = listenerMap.get(type);
			if (list == null) {
				list = new CopyOnWriteArrayList<EventListener>();
				listenerMap.put(type, list);
			}
			list.add(listener);
		}
	}
	
	@Override
	public void removeListener(String type, EventListener listener) {
		synchronized (listenerMap) {
			CopyOnWriteArrayList<EventListener> list = listenerMap.get(type);
			if (list == null) return;
			list.remove(listener);
			if (list.size() == 0) {
				listenerMap.remove(type);
			}
		}
	}
	
	@Override
	public boolean hasListener(String type, EventListener listener) {
		synchronized (listenerMap) {
			CopyOnWriteArrayList<EventListener> list = listenerMap.get(type);
			if (list == null) return false;
			return list.contains(listener);
		}
	}
	
	@Override
	public void dispatchEvent(Event event) {
		if (event == null) {
			Log.e(TAG, "can not dispatch null event");
			return;
		}
		String type = event.getType();
		event.setSource(target);
		CopyOnWriteArrayList<EventListener> list;
		synchronized (listenerMap) {
			list = listenerMap.get(type);
		}
		if (list == null) return;
		for (EventListener l : list) {
			l.onEvent(event);
		}
	}
	
	public void dispose() {
		synchronized (listenerMap) {
			listenerMap.clear();
		}
		target = null;
	}
}
