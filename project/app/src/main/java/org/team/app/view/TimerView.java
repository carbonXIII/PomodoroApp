package org.team.app.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import org.team.app.contract.TimerContract;
import org.team.app.model.TimerType;

import java.util.Locale;

/// The fragment for the Timer screen
public class TimerView extends FragmentView implements TimerContract.View, Timer.Listener {
    protected TimerContract.Presenter mPresenter;
    protected TextView titleText;
    protected TextView taskNameText;
    protected TextView timerText;
    protected Button pauseButton;
    protected Button skipButton;

    protected final Timer timer;
    protected long timerDuration;

    public static final int TICK_RATE_MS = 500;

    protected String workTimeText;
    protected String breakTimeText;
    protected String pauseText;
    protected String resumeText;

    static Boolean timerChange;
    Activity activity;

    public TimerView() {
        super(R.layout.screen_timer);
        this.timer = new Timer(this, TICK_RATE_MS);
    }

    private void setTimerDisplay(long minutes, long seconds) {
        timerText.setText(String.format(Locale.US, "%02d:%02d", minutes, seconds));
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
        case WORK: titleText.setText(workTimeText); break;
        case BREAK: titleText.setText(breakTimeText); break;
        }
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
    public void onTimerResume() {
        pauseButton.setText(pauseText);
    }

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

       /* if(mPresenter.isTimerDone()) {
            Notification(activity);
        } */
    }

    @Override
    public void onTimerPause(long timeElapsed) {
        pauseButton.setText(resumeText);
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
    public void onViewCreated(View view, Bundle savedInstanceState)  {
        super.onViewCreated(view, savedInstanceState);
        this.workTimeText = view.getResources().getString(R.string.work_timer_text);
        this.breakTimeText = view.getResources().getString(R.string.break_timer_text);
        this.pauseText = view.getResources().getString(R.string.pause);
        this.resumeText = view.getResources().getString(R.string.resume);

        titleText = view.findViewById(R.id.text_task_title);
        taskNameText = view.findViewById(R.id.text_task_name);
        timerText = view.findViewById(R.id.text_timer);

        pauseButton = view.findViewById(R.id.button_pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if(!timer.running()) {
                        mPresenter.onPlayButton();
                        Toast.makeText(getActivity(), "Task Resumed", Toast.LENGTH_SHORT).show();

                    } else {
                        mPresenter.onPauseButton();
                        Toast.makeText(getActivity(), "Task Paused", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        skipButton = view.findViewById(R.id.button_skip);
        skipButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    timer.pause();
                    mPresenter.onTimerComplete();
                    Toast.makeText(getActivity(), "Task Skipped", Toast.LENGTH_SHORT).show();
                    Notification(getActivity());
                }
        });

        if(mPresenter.isTimerDone()) {
            Notification(getActivity());
        }
    }

    public void Notification(Activity activity) {
        if ( activity instanceof MainActivity) {
            ((MainActivity)activity).Notification();
        }
    }
}
