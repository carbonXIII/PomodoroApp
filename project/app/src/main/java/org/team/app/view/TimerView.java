package org.team.app.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import org.team.app.contract.TimerContract;
import org.team.app.model.TimerType;

public class TimerView extends FragmentView implements TimerContract.View, Timer.Listener {
    protected TimerContract.Presenter mPresenter;
    protected TextView titleText;
    protected TextView taskNameText;
    protected TextView timerText;
    protected Button pauseButton;

    protected final Timer timer;
    protected long timerDuration;

    public static final int TICK_RATE_MS = 500;

    public TimerView() {
        super(R.layout.screen_timer);
        this.timer = new Timer(this, TICK_RATE_MS);
    }

    @Override
    public void setPresenter(TimerContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setTaskName(String taskName) {
        taskNameText.setText(taskName);
    }

    @Override
    public void setTimerType(TimerType type) {
        switch(type) {
        case WORK: titleText.setText("Work Time"); break;
        case BREAK: titleText.setText("Break Time"); break;
        }
    }

    @Override
    public void setTimerDisplay(long minutes, long seconds) {
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @Override
    public void startTimer(long duration) {
        this.timerDuration = duration;
        timer.resume();
    }

    @Override
    public long stopTimer() {
        return timer.pause();
    }

    @Override
    public void onTimerResume() {}

    @Override
    public void onTimerTick(long timeElapsed) {
        System.out.println("tick");
        long remaining = timerDuration - timeElapsed;

        long seconds = (remaining + 999) / 1000;
        long minutes = seconds / 60;
        seconds %= 60;

        if(remaining < 0) {
            setTimerDisplay(0, 0);
            timer.pause();

            mPresenter.onTimerComplete();
        } else {
            setTimerDisplay(minutes, seconds);
        }
    }

    @Override
    public void onTimerPause(long timeElapsed) {
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.pause();
        mPresenter.pause();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleText = view.findViewById(R.id.text_task_title);
        taskNameText = view.findViewById(R.id.text_task_name);
        timerText = view.findViewById(R.id.text_timer);

        pauseButton = view.findViewById(R.id.button_pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(!timer.running()) {
                        pauseButton.setText(v.getResources().getString(R.string.pause));
                        mPresenter.onPlayButton();
                    } else {
                        pauseButton.setText(v.getResources().getString(R.string.resume));
                        mPresenter.onPauseButton();
                    }
                }
            });
    }
}
