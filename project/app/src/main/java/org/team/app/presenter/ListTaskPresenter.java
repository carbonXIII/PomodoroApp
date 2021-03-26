package org.team.app.presenter;

import org.team.app.contract.ListTaskContract;
import org.team.app.model.TaskStore;
import org.team.app.model.Task;
import org.team.app.model.TimerType;

import java.util.List;
import java.util.UUID;

public class ListTaskPresenter
    implements ListTaskContract.Presenter, TaskStore.Listener, Task.Listener {
    protected final ListTaskContract.View mView;
    protected final TaskStore mTaskStore;

    protected List<Task> filtered;

    public ListTaskPresenter(ListTaskContract.View view,
                             TaskStore taskStore) {
        this.mView = view;
        this.mView.setPresenter(this);

        this.mTaskStore = taskStore;
        this.mTaskStore.subscribe(this);

        this.filtered = null;
    }

    @Override
    public void onCurrentTaskUpdate(Task newTask) {
        mView.selectCurrentTask(newTask.getUUID());
    }

    @Override
    public void onTaskNameUpdate(Task task, String newName) {
        mView.updateTask(task.getUUID());
    }

    @Override
    public void onTaskTimerDurationUpdate(Task task, TimerType timer, long newDuration) {
    }

    @Override
    public void start() {
        reloadTaskList("");
    }

    @Override
    public void pause() {
    }

    @Override
    public String getTaskName(UUID uuid) {
        Task task = mTaskStore.getTaskByUUID(uuid);
        return task.getName();
    }

    private void reloadTaskList(String filter) {
        if(this.filtered != null) {
            for (Task task : this.filtered) {
                mView.removeTask(task.getUUID());
                task.unsubscribe(this);
            }
        }

        this.filtered = mTaskStore.getTasks(filter);

        for(Task task: this.filtered) {
            mView.addTask(task.getUUID());
            task.subscribe(this);
        }

        mView.selectCurrentTask(mTaskStore.getCurrentTask().getUUID());
    }
}
