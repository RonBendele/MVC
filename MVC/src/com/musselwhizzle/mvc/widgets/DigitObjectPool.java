package com.musselwhizzle.mvc.widgets;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class DigitObjectPool {
	private static final String TAG = DigitObjectPool.class.getSimpleName();
	private static final boolean DEBUG = false;
	private Context context;
	private int size;
	private List<TextView> txtPool;
	private List<Animation> aniPool;
	
	public DigitObjectPool(Context context, int size) {
		this.context = context;
		this.size = size;
		txtPool = new ArrayList<TextView>(size);
		aniPool = new ArrayList<Animation>(size);
	}
	
	public TextView getTextView() {
		if (!txtPool.isEmpty()) {
			if (DEBUG) Log.i(TAG, "Got object from pool");
			return txtPool.remove(0);
		} else {
			if (DEBUG) Log.i(TAG, "creating new object");
			return createTextView();
		}
	}
	
	public Animation getOutAnimation() {
		if (!aniPool.isEmpty()) {
			if (DEBUG) Log.i(TAG, "Got object from pool");
			return aniPool.remove(0);
		} else {
			if (DEBUG) Log.i(TAG, "creating new object");
			return createOutAnimation();
		}
	}
	
	public void recycle(TextView textView) {
		if (txtPool.size() < size) {
			if (DEBUG) Log.i(TAG, "recycling object");
			txtPool.add(textView);
		} else {
			if (DEBUG) Log.i(TAG, "object thrown away");
		}
	}
	
	public void recycle(Animation ani) {
		if (aniPool.size() < size) {
			if (DEBUG) Log.i(TAG, "recycling object");
			aniPool.add(ani);
		} else {
			if (DEBUG) Log.i(TAG, "object thrown away");
		}
	}
	
	private TextView createTextView() {
		TextView t = new TextView(context);
		t.setGravity( Gravity.CENTER );
		t.setTextColor(0xFFFFFFFF);
		t.setPadding(3, 0, 3, 0);
		t.setTextSize(25);
		t.setPaintFlags( TextPaint.ANTI_ALIAS_FLAG | TextPaint.SUBPIXEL_TEXT_FLAG | t.getPaintFlags() );
		return t;
	}
	
	private Animation createOutAnimation() {
		Animation outAni = new TranslateAnimation(
        		Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, 
        		Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f
        );
        outAni.setDuration(100);
        outAni.setFillEnabled(true);
        outAni.setFillAfter(true);
        return outAni;
	}
}