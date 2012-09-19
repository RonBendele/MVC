package com.musselwhizzle.mvc.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.musselwhizzle.mvc.R;
import com.musselwhizzle.mvc.models.AppModel;
import com.musselwhizzle.mvc.views.MainView;

public class MainActivity extends Activity {

    private AppModel model;
    private MainView view;
    private Handler handler;
    private boolean isTimerRunning = true;
    private long initTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTime = System.currentTimeMillis();
        model = AppModel.getInstance();
        view = (MainView)View.inflate(this, R.layout.main, null);
        view.setViewListener(viewListener);
        setContentView(view);
        handler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerRun.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(timerRun);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        view.destroy();
    }

    /**
     * Simple runnable to update our current time in the  model
     */
    private Runnable timerRun = new Runnable() {
        @Override
        public void run() {
            if (isTimerRunning) {
                long change = System.currentTimeMillis() - initTime;
                initTime = System.currentTimeMillis();
                model.setElapsedTime(model.getElapsedTime() + change); // controller is responsible for updating the model
                handler.postDelayed(timerRun, 100);
            }
        }
    };

    /**
     * This is how we receive events from the view.
     * The view takes user actions
     * The controller/activity responds to user actions
     */
    private MainView.ViewListener viewListener = new MainView.ViewListener() {
        @Override
        public void onToggleTimer() {
            isTimerRunning = !isTimerRunning;
            view.setPausedState(isTimerRunning); // controller can call method directly on the view
            if (isTimerRunning) timerRun.run();
        }

        @Override
        public void onAddTime(long amountToAdd) {
            model.setElapsedTime(model.getElapsedTime() + amountToAdd);
        }
    };
}
