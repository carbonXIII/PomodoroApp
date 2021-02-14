package org.team.app.contract;

public interface SetupTaskContract {
    interface View extends BaseView<Presenter> {
        // void showValidationError(); // TODO
        void complete();
    }

    interface Presenter extends BasePresenter {
        void submitForm(String taskName);
    }
}
