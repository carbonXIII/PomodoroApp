
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

    private  Boolean timerChange = Boolean.FALSE;
    public static  int checkTimer = 0;

    protected TimerType timerType;
    protected long lastTimerDuration = -1;

    public Boolean getTimerChange() {
        timerChange = Boolean.TRUE;
        return this.timerChange;
    }

    public int getCheckTimer() {
        checkTimer = 1;
        return this.checkTimer;
    }

    /// Construct a presenter, attaching it to a view and task store
    public TimerPresenter(TimerContract.View view, TaskStore taskStore) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mTaskStore = taskStore;
    }

    private void setTimerType(TimerType type) {
        this.timerType = type;
        mView.setTimerType(type);
    }

    @Override
    public void onCurrentTaskUpdate(Task newTask) {
        if (mTask != null)
            mTask.unsubscribe(this);
        mTask = newTask;
        mTask.subscribe(this);

        mView.setTaskName(mTask.getName());
    }

    @Override
    public void onTaskNameUpdate(Task task, String newName) {
        mView.setTaskName(newName);
    }

    @Override
    public void onTaskTimerDurationUpdate(Task task, TimerType timer, long newDuration) {}

    @Override
    public void start() {
        mTaskStore.subscribe(this);

        mTask = mTaskStore.getCurrentTask();
        mTask.subscribe(this);

        String taskName = mTask.getName();
        mView.setTaskName(taskName);

        onTimerComplete();
    }

    @Override
    public void onTimerComplete() {
        if(this.timerType == TimerType.WORK) {
            setTimerType(TimerType.BREAK);
        } else {
            setTimerType(TimerType.WORK);
        }

        this.lastTimerDuration = mTask.getTimerDuration(timerType);
        mView.startTimer(this.lastTimerDuration);
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
            mView.startTimer(this.lastTimerDuration);
        }
    }

    // Check if timer is done, not sure if it completely works
    public Boolean isTimerDone() {
        if(this.lastTimerDuration <= 0) {
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }

    @Override
    public void pause() {}

    public void Notification(Activity activity) {
        if ( activity instanceof MainActivity) {
            ((MainActivity)activity).Notification();
        }
    }
}
