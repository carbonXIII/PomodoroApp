package org.team.app.contract;

/// Allows a view to send messages to the presenter
public interface BasePresenter {
    /// Called when the view is resumed
    void start();

    /// Called when the view is paused
    void pause();
}
