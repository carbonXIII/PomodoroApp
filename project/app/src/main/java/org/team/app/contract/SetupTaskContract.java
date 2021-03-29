package org.team.app.contract;

import org.team.app.model.TimerType;

public interface SetupTaskContract {
    interface View extends BaseView<Presenter> {
        // void showValidationError(); // TODO

        /// Set the task name in response to model updates
        void setTaskName(String name);

        /// Set the task name in response to model updates
        void setTaskCategory(String category);

        /// Set the task time in response to model updates
        void setTaskTime(TimerType type, long duration);
    }

    interface Presenter extends BasePresenter {
        /// Update the model with new task name from the view's form
        void setTaskName(String name);

        /// Update the model with new task category from the view's form
        void setTaskCategory(String category);

        /// Update the model with new task Time from the view's form
        void setTaskTime(TimerType type, long duration);
    }
}
