package org.team.app.contract;

import java.util.UUID;
import java.util.Collection;

public interface ListTaskContract {
    interface View extends BaseView<Presenter> {
        public void updateTaskList(Collection<UUID> task);

        public void selectCurrentTask(UUID task);
    }

    interface Presenter extends BasePresenter {
        public String getTaskName(UUID task);

        public UUID createNewTask();

        public void updateFilter(String filter);

        public boolean selectCurrentTask(UUID task);
    }
}
