package org.team.app.contract;

import org.team.app.model.TimerType;

public interface TimerContract {
    interface View extends BaseView<Presenter> {
        void setTaskName(String taskName);
        void setTimerType(TimerType type);

        void startTimer(long duration);
        long stopTimer();
    }

    interface Presenter extends BasePresenter {
        void onTimerComplete();

        void onPauseButton();

        void onPlayButton();
    }
}
