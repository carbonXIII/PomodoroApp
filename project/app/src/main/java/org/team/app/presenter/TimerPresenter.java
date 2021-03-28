
package org.team.app.presenter;

import android.app.Activity;

import org.team.app.contract.TimerContract;

import org.team.app.model.TaskStore;
import org.team.app.model.Task;
import org.team.app.model.TimerType;
import org.team.app.view.MainActivity;

/// The presenter for the Timer screen
public class TimerPresenter
    implements TimerContract.Presenter, TaskStore.Listener, Task.Listener {
    protected final TimerContract.View mView;
    protected final TaskStore mTaskStore;

    Activity activity;

    protected Task mTask;

    protected TimerType timerType;
    protected long lastTimerDuration = -1;

    /// Construct a presenter, attaching it to a view and task store
    public TimerPresenter(TimerContract.View view, TaskStore taskStore) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mTaskStore = taskStore;
        mTaskStore.subscribe(this);
    }

    private void setTimerType(TimerType type) {
        this.timerType = type;
        mView.setTimerType(type);
    }

    @Override
    public void onCurrentTaskUpdate(Task newTask) {
        if (mTask != null) {
            mTask.unsubscribe(this);
            onPauseButton();
            this.lastTimerDuration = -1;
        }

        mTask = newTask;
        mTask.subscribe(this);

        mView.setTaskName(mTask.getName());
        timerType = null;
        onPlayButton();
    }

    @Override
    public void onTaskAdded(Task newTask) {
    }

    @Override
    public void onTaskNameUpdate(Task task, String newName) {
        if(task.getUUID() == mTask.getUUID())
            mView.setTaskName(newName);
    }

    @Override
    public void onTaskTimerDurationUpdate(Task task, TimerType type, long newDuration) {
        if(task == mTask && type == timerType) {
            boolean wasRunning = false;
            if(mView.running()) {
                wasRunning = true;
                mView.stopTimer();
            }
            this.lastTimerDuration = newDuration;

            onPlayButton();
            if(!wasRunning)
                mView.stopTimer();
        }
    }

    @Override
    public void start() {
        onCurrentTaskUpdate(mTaskStore.getCurrentTask());
        onPlayButton();
    }

    public void onTimerSkip() {
        if(this.timerType == TimerType.WORK) {
            setTimerType(TimerType.BREAK);
            //onPauseButton();
        } else {
            setTimerType(TimerType.WORK);
        }

        this.lastTimerDuration = mTask.getTimerDuration(timerType);
        mView.startTimer(this.lastTimerDuration, this.lastTimerDuration);
    }


    @Override
    public void onTimerComplete() {
        if(this.timerType == TimerType.WORK) {
            setTimerType(TimerType.BREAK);
        } else {
            setTimerType(TimerType.WORK);
        }

        this.lastTimerDuration = mTask.getTimerDuration(timerType);
        mView.startTimer(this.lastTimerDuration, this.lastTimerDuration);
    }

    @Override
    public void onPauseButton() {
        long elapsed = mView.stopTimer();
        this.lastTimerDuration -= elapsed;
    }

    @Override
    public void onPlayButton() {
        if(this.lastTimerDuration <= 0) {
            onTimerComplete();
        } else {
            mView.startTimer(this.lastTimerDuration, mTask.getTimerDuration(timerType));
        }
    }

    @Override
    public void pause() {
        onPauseButton();
    }
}
