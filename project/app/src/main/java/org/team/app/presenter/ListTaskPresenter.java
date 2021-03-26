package org.team.app.presenter;

import org.team.app.contract.ListTaskContract;
import org.team.app.model.TaskStore;
import org.team.app.model.Task;
import org.team.app.model.TimerType;

import java.util.Collection;
import java.util.ArrayList;
import java.util.UUID;

public class ListTaskPresenter
    implements ListTaskContract.Presenter, TaskStore.Listener, Task.Listener {
    protected final ListTaskContract.View mView;
    protected final TaskStore mTaskStore;

    protected String currentFilter = "";
    protected Collection<Task> filtered = null;

    public ListTaskPresenter(ListTaskContract.View view,
                             TaskStore taskStore) {
        this.mView = view;
        this.mView.setPresenter(this);

        this.mTaskStore = taskStore;
        this.mTaskStore.subscribe(this);
    }

    @Override
    public void onCurrentTaskUpdate(Task newTask) {
        mView.selectCurrentTask(newTask.getUUID());
    }

    @Override
    public void onTaskAdded(Task newTask) {
        reloadTaskList();
    }

    @Override
    public void onTaskNameUpdate(Task task, String newName) {
        reloadTaskList();
    }

    @Override
    public void onTaskTimerDurationUpdate(Task task, TimerType timer, long newDuration) {
    }

    @Override
    public void start() {
        reloadTaskList();
    }

    @Override
    public void pause() {
    }

    @Override
    public String getTaskName(UUID uuid) {
        Task task = mTaskStore.getTaskByUUID(uuid);
        return task.getName();
    }

    @Override
    public UUID createNewTask() {
        return mTaskStore.createTask(null);
    }

    @Override
    public void updateFilter(String filter) {
        this.currentFilter = filter;
        reloadTaskList();
    }

    @Override
    public boolean selectCurrentTask(UUID task) {
        if(this.mTaskStore.getCurrentTask().getUUID() == task)
            return false;

        this.mTaskStore.setCurrentTask(task);
        reloadTaskList();

        return true;
    }

    // This is the only method that accesses the task list,
    // but it can be called in different threads, so it is synchronized.
    // It might make sense to synchronize on this.filtered instead.
    private synchronized void reloadTaskList() {
        if(this.filtered != null) {
            for (Task task : this.filtered) {
                task.unsubscribe(this);
            }
        }

        Task currentTask = mTaskStore.getCurrentTask();
        this.filtered = mTaskStore.getTasks(currentFilter);

        this.filtered.add(currentTask);
        currentTask.subscribe(this);

        ArrayList<UUID> inOrder = new ArrayList<UUID>();
        inOrder.add(currentTask.getUUID());

        for(Task task: this.filtered) {
            if(task.getUUID() != currentTask.getUUID())
                inOrder.add(task.getUUID());
            task.subscribe(this);
        }

        System.out.println(inOrder.size());

        mView.updateTaskList(inOrder);
        mView.selectCurrentTask(currentTask.getUUID());
    }
}
