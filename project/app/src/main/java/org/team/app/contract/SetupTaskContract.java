package org.team.app.contract;

public interface SetupTaskContract {
    interface View extends BaseView<Presenter> {
        // void showValidationError(); // TODO
        void setTaskName(String name);
    }

    interface Presenter extends BasePresenter {
        void setTaskName(String name);
    }
}
