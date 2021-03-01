package org.team.app.contract;

public interface TimerContract {
    enum TimerType {
        WORK, BREAK
    }

    interface View extends BaseView<Presenter> {
        void setTaskName(String taskName);
        void setTimerType(TimerType type);
    }

    interface Presenter extends BasePresenter {
    }
}
