import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.team.app.presenter.TimerPresenter;

import org.team.app.contract.TimerContract;
import org.team.app.model.TaskStore;
import org.team.app.model.TimerType;

import java.util.UUID;

class TimerPresenterTest {
    static class MockView implements TimerContract.View {
        public TimerContract.Presenter mPresenter;
        public String name;
        public TimerType type;

        @Override
        public void setPresenter(TimerContract.Presenter presenter) {
            this.mPresenter = presenter;
        }

        @Override
        public void setTaskName(String name) {
            this.name = name;
        }

        @Override
        public void setTimerType(TimerType type) {
            this.type = type;
        }

        @Override
        public void setTimerDisplay(long minutes, long seconds) {}

        @Override
        public void startTimer(long tickRate) {}

        @Override
        public long stopTimer() {
            return 0;
        }
    }

    MockView view;
    TaskStore taskStore;
    TimerPresenter presenter;

    @BeforeEach
    void setupTaskStoreAndView() {
        view = new MockView();
        taskStore = new TaskStore("default");
        presenter = new TimerPresenter(view, taskStore);
        presenter.start();
    }

    @Test
    void presenterShouldAttachToProvidedViewAndSetTimerType() {
        assertEquals(view.mPresenter, presenter);
        assertNotNull(view.type);
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
