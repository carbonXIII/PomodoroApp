import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.team.app.presenter.SetupTaskPresenter;

import org.team.app.contract.SetupTaskContract;
import org.team.app.model.TaskStore;

import java.util.UUID;

class SetupTaskPresenterTest {
    static class MockView implements SetupTaskContract.View {
        public SetupTaskContract.Presenter mPresenter;
        public String name;

        @Override
        public void setPresenter(SetupTaskContract.Presenter presenter) {
            this.mPresenter = presenter;
        }

        @Override
        public void setTaskName(String name) {
            this.name = name;
        }
    }

    MockView view;
    TaskStore taskStore;
    SetupTaskPresenter presenter;

    @BeforeEach
    void setupTaskStoreAndView() {
        view = new MockView();
        taskStore = new TaskStore("default");
        presenter = new SetupTaskPresenter(view, taskStore);
        presenter.start();
    }

    @Test
    void presenterShouldAttachToProvidedView() {
        assertEquals(view.mPresenter, presenter);
    }

    @Test
    void changingCurrentTaskShouldUpdateView() {
        String newTaskName = UUID.randomUUID().toString();
        taskStore.createTask(newTaskName);
        assertEquals(newTaskName, view.name);
    }

    @Test
    void changingCurrentTaskNameShouldUpdateView() {
        String newTaskName = UUID.randomUUID().toString();
        taskStore.getCurrentTask().setName(newTaskName);
        assertEquals(newTaskName, view.name);
    }
}
