
package org.team.app.presenter;

import org.team.app.contract.TimerContract;

import org.team.app.model.TaskStore;
import org.team.app.model.Task;

public class TimerPresenter
    implements TimerContract.Presenter, TaskStore.Listener, Task.Listener {
    protected final TimerContract.View mView;
    protected final TaskStore mTaskStore;

    protected Task mTask;

    public TimerPresenter(TimerContract.View view, TaskStore taskStore) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mTaskStore = taskStore;
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

    private void updateTimerInfo() {
        // TODO: set the time, and find the appropriate timer type
        mView.setTimerType(TimerContract.TimerType.WORK);
    }

    @Override
    public void start() {
        mTaskStore.subscribe(this);

        mTask = mTaskStore.getCurrentTask();
        mTask.subscribe(this);

        String taskName = mTask.getName();
        mView.setTaskName(taskName);
        updateTimerInfo();
    }
}
