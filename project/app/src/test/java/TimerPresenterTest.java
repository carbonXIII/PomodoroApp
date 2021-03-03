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

        public long duration;
        public long debugElapsed = 0;

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
        public void startTimer(long duration) {
            this.duration = duration;
        }

        @Override
        public long stopTimer() {
            return debugElapsed;
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
    // UID 001 RID 016 Presenters should be attached to views
    void presenterShouldAttachToProvidedViewAndSetTimerDetails() {
        assertEquals(view.mPresenter, presenter);
        assertEquals(view.type, TimerType.WORK);
        assertEquals(view.duration, taskStore.getCurrentTask().getTimerDuration(TimerType.WORK));
    }

    @Test
    // UID 001 RID 015 Model updates should be propogated ...
    void changingCurrentTaskShouldUpdateView() {
        String newTaskName = UUID.randomUUID().toString();
        taskStore.createTask(newTaskName);
        assertEquals(newTaskName, view.name);
    }

    @Test
    // UID 001 RID 015 Model updates should be propogated ...
    void changingCurrentTaskNameShouldUpdateView() {
        String newTaskName = UUID.randomUUID().toString();
        taskStore.getCurrentTask().setName(newTaskName);
        assertEquals(newTaskName, view.name);
    }

    @Test
    // UID 001 RID 017 Pausing the timer should save the current time.
    void resumingTimerShouldAdjustDuration() {
        view.debugElapsed = taskStore.getCurrentTask().getTimerDuration(view.type) - 1;
        presenter.onPauseButton();
        presenter.onPlayButton();
        assertEquals(view.duration, 1);
    }

    @Test
    // UID 001 RID 002 When the working timer completes
    void onTimerCompleteShouldResetTimerDetails() {
        TimerType original = view.type;
        view.duration = -1;
        presenter.onTimerComplete();
        assertNotEquals(view.type, original);
        assertNotEquals(view.duration, -1);
    }

    @Test
    // UID 001 RID 002 When the working timer completes ...
    void resumingCompletedTimerShouldChangeTimerType() {
        TimerType original = view.type;
        view.debugElapsed = taskStore.getCurrentTask().getTimerDuration(view.type) + 1;
        presenter.onPauseButton();
        presenter.onPlayButton();
        assertNotEquals(view.type, original);
    }
}
