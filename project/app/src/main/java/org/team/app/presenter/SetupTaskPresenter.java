package org.team.app.presenter;

import org.team.app.contract.SetupTaskContract;
import org.team.app.model.TaskStore;
import org.team.app.model.Task;
import org.team.app.model.TimerType;

/// The presenter for the Task Setup screen
public class SetupTaskPresenter
    implements SetupTaskContract.Presenter, TaskStore.Listener, Task.Listener {
    protected final SetupTaskContract.View mView;
    protected final TaskStore mTaskStore;

    protected Task mTask;

    /// Construct a presenter, attaching it to a view and task store
    public SetupTaskPresenter(SetupTaskContract.View view,
                              TaskStore taskStore) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mTaskStore = taskStore;
    }

    @Override
    public void onCurrentTaskUpdate(Task newTask) {
        if(mTask != null)
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
    public void onTaskTimerDurationUpdate(Task task, TimerType timer, long newDuration) {
    }

    @Override
    public void start() {
        mTaskStore.subscribe(this);

        mTask = mTaskStore.getCurrentTask();
        mTask.subscribe(this);

        String taskName = mTask.getName();
        mView.setTaskName(taskName);
    }

    @Override
    public void pause() {
    }

    @Override
    public void setTaskName(String name) {
        mTask.setName(name);
    }
}
