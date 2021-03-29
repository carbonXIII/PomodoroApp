package org.team.app.presenter;

import org.team.app.contract.StatsContract;

import org.team.app.model.TaskStore;
import org.team.app.model.Task;

import java.util.TreeMap;
import java.util.Collection;

public class StatsPresenter implements StatsContract.Presenter {
    protected final StatsContract.View mView;
    protected final TaskStore mTaskStore;

    public StatsPresenter(StatsContract.View view, TaskStore taskStore) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mTaskStore = taskStore;
    }

    @Override
    public void start() {
        TreeMap<String, Long> timeWorked = new TreeMap<>();

        Collection<Task> taskList = mTaskStore.getTasks("");
        for(Task task: taskList) {
            String category = task.getCategory();

            Long total = timeWorked.get(category);
            if(total == null)
                total = new Long(0);
            total += task.getTimeWorked();
            timeWorked.put(category, total);
        }

        mView.setData(timeWorked);
    }

    @Override
    public void pause() {
    }
}
