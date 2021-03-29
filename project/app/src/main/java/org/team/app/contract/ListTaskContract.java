package org.team.app.contract;

import java.util.UUID;
import java.util.Collection;

public interface ListTaskContract {
    static class Element {
        public final UUID task;
        public final String category;

        public Element(UUID task) {
            this.task = task;
            this.category = null;
        }

        public Element(String category) {
            this.task = null;
            this.category = category;
        }

        public boolean equals(Object o) {
            if(o instanceof Element) {
                Element O = (Element) o;
                if(this.task != O.task)
                    return false;
                if(this.category == null ? O.category != null : !this.category.equals(O.category))
                    return false;

                return true;
            }

            return false;
        }
    }

    interface View extends BaseView<Presenter> {
        /// Update the list of currently displayed tasks (filtered)
        public void updateTaskList(Collection<Element> list);

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
