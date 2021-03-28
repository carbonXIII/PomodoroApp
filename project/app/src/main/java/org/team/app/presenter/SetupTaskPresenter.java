package org.team.app.presenter;

import org.team.app.contract.SetupTaskContract;
import org.team.app.model.TaskStore;
import org.team.app.model.Task;
import org.team.app.model.TimerType;

import java.util.UUID;

/// The presenter for the Task Setup screen
public class SetupTaskPresenter
    implements SetupTaskContract.Presenter, TaskStore.Listener, Task.Listener {
    protected final SetupTaskContract.View mView;
    protected final TaskStore mTaskStore;

    protected Task mTask;

    /// Construct a presenter, attaching it to a view and task store
    public SetupTaskPresenter(SetupTaskContract.View view,
                              TaskStore taskStore,
                              UUID task) {
        this.mView = view;
        this.mView.setPresenter(this);

        this.mTaskStore = taskStore;
        mTaskStore.subscribe(this);

        this.mTask = this.mTaskStore.getTaskByUUID(task);
        this.mTask.subscribe(this);
    }

    @Override
    public void onCurrentTaskUpdate(Task newTask) {
    }

    @Override
    public void onTaskAdded(Task newTask) {
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
        String taskName = mTask.getName();
        mView.setTaskName(taskName);

        long workDuration = mTask.getTimerDuration(TimerType.WORK);
        long breakDuration = mTask.getTimerDuration(TimerType.BREAK);
        mView.setTaskTime(TimerType.WORK, workDuration);
        mView.setTaskTime(TimerType.BREAK, breakDuration);
    }

    @Override
    public void pause() {
    }

    @Override
    public void setTaskName(String name) {
        mTask.setName(name);
    }

    @Override
    public void setTaskTime(TimerType type, long duration) {mTask.setTimerDuration(type, duration);}
}
