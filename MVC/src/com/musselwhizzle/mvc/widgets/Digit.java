package com.musselwhizzle.mvc.widgets;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Digit extends RelativeLayout {
	
	private String currentTxt;
	private Animation inAnim;
	private TextView currentTextView;
	private Handler handler;
	
	private DigitObjectPool pool;
	public void setPool(DigitObjectPool pool) {
		this.pool = pool;
	}
	
	public Digit(Context context) {
		this(context, null);
	}
	
	public Digit(Context context, AttributeSet attrs) {
		super(context, attrs);
		handler = new Handler();		
        inAnim = new TranslateAnimation(
        		Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, 
        		Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        		);
        inAnim.setDuration(100);
	}
	
	private class OutAnimListener implements AnimationListener {
		private TextView target;
		private OutAnimListener(TextView target) {
			this.target = target;
		}
		
		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationEnd(final Animation animation) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					removeView(target);
					pool.recycle(target);
					pool.recycle(animation);
				}
			});
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		
	}
	
	private Animation getOutAnimation(TextView target) {
		Animation outAni = pool.getOutAnimation();
		outAni.setAnimationListener(new OutAnimListener(target));
		return outAni;
		// NOT PRODUCTION CODE!!!!!
		// you should use an object pool for this
//		Animation outAni = new TranslateAnimation(
//        		Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, 
//        		Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f
//        );
//        outAni.setDuration(100);
//        outAni.setFillEnabled(true);
//        outAni.setFillAfter(true);
//        outAni.setAnimationListener(new OutAnimListener(target));
//        return outAni;
	}
	
	public void showTime(int digit) {
		showTime(Integer.toString(digit));
	}
	
	public void showTime(String digit) {
		if (digit.equals(currentTxt)) return;
		currentTxt = digit;
		if (currentTextView != null) {
			currentTextView.startAnimation(getOutAnimation(currentTextView));
		}
		currentTextView = getTextView();
		currentTextView.setText(digit);
		currentTextView.startAnimation(inAnim);
		addView(currentTextView);
	}
	
	private TextView getTextView() {
		return pool.getTextView();
	}	
}