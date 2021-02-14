package org.team.app.contract;

public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);
}
