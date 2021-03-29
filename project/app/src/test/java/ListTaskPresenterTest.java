import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.team.app.presenter.ListTaskPresenter;

import org.team.app.contract.ListTaskContract;
import org.team.app.model.Task;
import org.team.app.model.TaskStore;

import java.util.UUID;
import java.util.Collection;
import java.util.ArrayList;

class ListTaskPresenterTest {
    static class MockView implements ListTaskContract.View {
        public ListTaskContract.Presenter mPresenter;
        public Collection<ListTaskContract.Element> list;
        public UUID currentTask;

        @Override
        public void setPresenter(ListTaskContract.Presenter presenter) {
            this.mPresenter = presenter;
        }

        @Override
        public void updateTaskList(Collection<ListTaskContract.Element> list) {
            this.list = list;
        }

        @Override
        public void selectCurrentTask(UUID task) {
            this.currentTask = task;
        }
    }

    MockView view;
    TaskStore taskStore;
    ListTaskPresenter presenter;

    @BeforeEach
    void setupTaskStoreAndView() {
        view = new MockView();
        taskStore = new TaskStore("default", "general");
        presenter = new ListTaskPresenter(view, taskStore);
        presenter.start();
    }

    @Test
    // UID 001 RID 016 Presenters should be attached to views
    void presenterShouldAttachToProvidedViewAndSetTimerDetails() {
        assertEquals(view.mPresenter, presenter);
    }

    @Test
    // UID 019 RID 028 When the user clicks the new task button a new task will be added to the listing
    void createTaskShouldUpdateListing() {
        presenter.createNewTask();
        presenter.createNewTask();
        presenter.createNewTask();
        presenter.createNewTask();

        Collection<Task> expected = taskStore.getTasks("");

        assertEquals(expected.size() + 1, view.list.size());
        assertEquals(taskStore.getCurrentTask().getUUID(), view.currentTask);
        for(Task task: expected)
            assert(view.list.contains(new ListTaskContract.Element(task.getUUID())));
    }

    @Test
    // UID 019 RID 029 When a user clicks on a task it will replace the current task
    // UID 019 RID 032 Visual indication of selected task
    void updatingCurrentTaskShouldUpdateListing() {
        UUID a = presenter.createNewTask();

        UUID b = presenter.createNewTask();
        presenter.selectCurrentTask(b);

        assertEquals(b, view.currentTask);
    }

    @Test
    // UID 019 RID 031 Typing in a filter will filter the list of displayed tasks
    void updatingFilterShouldUpdateListing() {
        taskStore.createTask("fobar D", null);
        taskStore.createTask("foo A", null);
        taskStore.createTask("foo B", null);
        taskStore.createTask("foobar C", null);
        taskStore.createTask("bar D", null);

        final String testFilter = "foo";
        presenter.updateFilter(testFilter);
        Collection<Task> expected = taskStore.getTasks(testFilter);

        for(Task task: expected)
            assert(view.list.contains(new ListTaskContract.Element(task.getUUID())));
    }

}
