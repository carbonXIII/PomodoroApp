
package org.team.app.presenter;

import org.team.app.contract.TimerContract;
import org.team.app.model.TaskStore;
//Display the Timer and get information from taskstore
public class TimerPresenter implements TimerContract.Presenter {
    protected final TimerContract.View mView;
    protected final TaskStore mTaskStore;

    public TimerPresenter(TimerContract.View view, TaskStore taskStore) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mTaskStore = taskStore;
    }

    @Override
    public void start() {
        String taskName = mTaskStore.getCurrentTask().getName();

        // TODO: determine title based on timer type
        mView.setTitle("Working on", taskName);
    }

    @Override
    public void onTimerComplete() {
        mView.complete();
    }
}
