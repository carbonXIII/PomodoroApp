package org.team.app.contract;

import org.team.app.model.TimerType;

public interface SetupTaskContract {
    interface View extends BaseView<Presenter> {
        // void showValidationError(); // TODO

        /// Set the task name in response to model updates
        void setTaskName(String name);
    }

    interface Presenter extends BasePresenter {
        /// Update the model with new task name from the view's form
        void setTaskName(String name);

        void setTaskTime(TimerType type, long duration);
    }
}
