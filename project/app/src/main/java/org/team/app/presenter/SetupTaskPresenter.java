package org.team.app.presenter;

import org.team.app.contract.SetupTaskContract;
import org.team.app.model.TaskStore;
//Create a task and give name (give time for the timer in future)
public class SetupTaskPresenter implements SetupTaskContract.Presenter {
    protected final SetupTaskContract.View mView;
    protected final TaskStore mTaskStore;

    public SetupTaskPresenter(SetupTaskContract.View view,
                              TaskStore taskStore) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mTaskStore = taskStore;
    }

    @Override
    public void start() {}

    @Override
    public void submitForm(String taskName) {
        // TODO: validate task details
        mTaskStore.createTask(taskName);

        mView.complete();
    }
}
