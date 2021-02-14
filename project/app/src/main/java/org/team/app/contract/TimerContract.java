package org.team.app.contract;

public interface TimerContract {
    interface View extends BaseView<Presenter> {
        void setTitle(String title, String taskName);
        void complete();
    }

    interface Presenter extends BasePresenter {
        void onTimerComplete();
    }
}
