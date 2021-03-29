package org.team.app.contract;

import java.util.Map;

public interface StatsContract {
    interface View extends BaseView<Presenter> {
        void setData(Map<String, Long> data);
    }

    interface Presenter extends BasePresenter {
    }
}
