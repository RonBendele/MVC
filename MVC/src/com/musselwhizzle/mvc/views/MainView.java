package com.musselwhizzle.mvc.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.musselwhizzle.mvc.R;
import com.musselwhizzle.mvc.events.Event;
import com.musselwhizzle.mvc.events.EventListener;
import com.musselwhizzle.mvc.models.AppModel;
import com.musselwhizzle.mvc.widgets.Digit;
import com.musselwhizzle.mvc.widgets.DigitObjectPool;

public class MainView extends LinearLayout {
	
	/**
	 * The interface to send events from the view to the controller
	 */
	public static interface ViewListener {
		public void onToggleTimer();
		public void onAddTime(long amountToAdd);
	}
	
	private static boolean DEBUG = false;
	private static final String TAG = MainView.class.getSimpleName();
	private static final long AMOUNT_TO_ADD = 1234 * 60 * 2;
	private Digit d1, d2, d3, d4, d5;
	private Button toggleBtn, addBtn;
	private AppModel model;
	
	/**
	 * The listener reference for sending events
	 */
	private ViewListener viewListener;
	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}
	
	/**
	 * Constructor for xml layouts
	 */
	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);
		model = AppModel.getInstance();
	}
	
	/**
	 * Exposed method so the controller can set the button state. 
	 */
	public void setPausedState(boolean isTimerRunning) {
		String txt = (isTimerRunning) ? getContext().getString(R.string.stop) : getContext().getString(R.string.start);
		toggleBtn.setText(txt);
	}
	
	/**
	 * Remove the listener from the model
	 */
	public void destroy() {
		model.removeListener(AppModel.ChangeEvent.ELAPSED_TIME_CHANGED, elapsedTimeListener);
	}
	
	/**
	 * Does the work to update the view when the model changes. 
	 */
	private void bind() {
		int milli = (int)Math.floor((model.getElapsedTime() % 1000));
		int secs = (int)Math.floor((model.getElapsedTime() / 1000) % 60);
		int mins = (int)Math.floor((model.getElapsedTime() / 1000 / 60) % 60);
		
		if (DEBUG) {
			Log.i(TAG, "elapsed: " + model.getElapsedTime());
			Log.i(TAG, "secs: " + secs);
			Log.i(TAG, "mins: " + mins);
		}
		
		d1.showTime((int)Math.floor(mins/10));
		d2.showTime(mins % 10);
		d3.showTime((int)Math.floor(secs/10));
		d4.showTime(secs % 10);
		d5.showTime((int)Math.floor(milli/100));
	}
	
	/**
	 * Find our references to the objects in the xml layout
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		toggleBtn = (Button)findViewById(R.id.toggleBtn);
		addBtn = (Button)findViewById(R.id.addTimeBtn);
		d1 = (Digit)findViewById(R.id.digit1);
		d2 = (Digit)findViewById(R.id.digit2);
		d3 = (Digit)findViewById(R.id.digit3);
		d4 = (Digit)findViewById(R.id.digit4);
		d5 = (Digit)findViewById(R.id.digit5);
		DigitObjectPool pool = new DigitObjectPool(getContext(), 10);
		d1.setPool(pool);
		d2.setPool(pool);
		d3.setPool(pool);
		d4.setPool(pool);
		d5.setPool(pool);
		
		
		toggleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onToggleTimer();
			}
		});
		addBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewListener.onAddTime(AMOUNT_TO_ADD);
			}
		});
		model.addListener(AppModel.ChangeEvent.ELAPSED_TIME_CHANGED, elapsedTimeListener);
		bind();
	}
	
	/**
	 * The listener for when the elapsed time property changes on the model
	 */
	private EventListener elapsedTimeListener = new EventListener() {
		@Override
		public void onEvent(Event event) {
			bind();
		}
	};
}