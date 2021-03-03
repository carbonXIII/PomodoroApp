
package org.team.app.presenter;

import org.team.app.contract.TimerContract;

import org.team.app.model.TaskStore;
import org.team.app.model.Task;
import org.team.app.model.TimerType;

public class TimerPresenter
    implements TimerContract.Presenter, TaskStore.Listener, Task.Listener {
    protected final TimerContract.View mView;
    protected final TaskStore mTaskStore;

    protected Task mTask;

    protected TimerType timerType;

    protected long lastTimerDuration = -1;

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

        System.out.println("stopped timer");
    }

    @Override
    public void onPlayButton() {
        if(this.lastTimerDuration <= 0) {
            System.out.println("resetting timer");
            onTimerComplete();
        } else {
            System.out.println("continuing timer");
            mView.startTimer(this.lastTimerDuration);
        }
    }

    @Override
    public void pause() {}
}
