package org.team.app.contract;

import java.util.UUID;
import java.util.Collection;

public interface ListTaskContract {
    interface View extends BaseView<Presenter> {
        /// Update the list of currently displayed tasks (filtered)
        public void updateTaskList(Collection<UUID> list);

        /// Update the currently selected task
        public void selectCurrentTask(UUID task);
    }

    interface Presenter extends BasePresenter {
        /// @return the name property for a task
        public String getTaskName(UUID task);

        /// Create a new task
        /// @return a UUID/handle for the new task
        public UUID createNewTask();

        /// Update the filter string
        public void updateFilter(String filter);

        /// Update the model's currently selected task
        /// @return true if the current task changed, false otherwise
        public boolean selectCurrentTask(UUID task);
    }
}
