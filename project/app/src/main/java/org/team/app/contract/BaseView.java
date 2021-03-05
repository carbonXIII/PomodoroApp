package org.team.app.contract;

/// Allows a presenter to send messages to the view
public interface BaseView<T extends BasePresenter> {
    /// Attach a presenter to this view as a listener
    void setPresenter(T presenter);
}
