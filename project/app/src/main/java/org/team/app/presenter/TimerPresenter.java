
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
        }

        mTask = newTask;
        mTask.subscribe(this);

        mView.setTaskName(mTask.getName());
        mView.setTaskCategory(mTask.getCategory());

        setTimerType(TimerType.WORK);
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
    public void onTaskCategoryUpdate(Task task, String newCategory) {
        if(task.getUUID() == mTask.getUUID())
            mView.setTaskCategory(newCategory);
    }

    @Override
    public void onTaskTimerDurationUpdate(Task task, TimerType type, long newDuration) {
        if(task == mTask && type == timerType) {
            boolean wasRunning = false;
            if(mView.running()) {
                wasRunning = true;
                onPauseButton();
            }

            mTask.resetElapsed(timerType);
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
        onTimerComplete();
    }


    @Override
    public void onTimerComplete() {
        if(this.timerType == TimerType.WORK) {
            setTimerType(TimerType.BREAK);
        } else {
            setTimerType(TimerType.WORK);
        }

        mTask.resetElapsed(timerType);
        mView.startTimer(mTask.getTimerRemaining(timerType), mTask.getTimerDuration(timerType));
    }

    @Override
    public void onPauseButton() {
        mTask.addElapsed(timerType, mView.stopTimer());
    }

    @Override
    public void onPlayButton() {
        if(mTask.getTimerRemaining(timerType) <= 0) {
            onTimerComplete();
        } else {
            mView.startTimer(mTask.getTimerRemaining(timerType), mTask.getTimerDuration(timerType));
        }
    }

    @Override
    public void pause() {
        onPauseButton();
    }
}
