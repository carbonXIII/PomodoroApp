package org.team.app.contract;

import java.util.UUID;

public interface ListTaskContract {
    interface View extends BaseView<Presenter> {
        public void addTask(UUID task);

        public void removeTask(UUID task);

        public void updateTask(UUID task);

        public void selectCurrentTask(UUID task);
    }

    interface Presenter extends BasePresenter {
        public String getTaskName(UUID task);
    }
}
