package org.team.app.view;

import androidx.fragment.app.Fragment;

public interface ActivityListener {
    void startSetupTaskView();
    void startTimerView();
    // void startContinueView();

    interface TabInfo {
        String getTitle();
        Fragment getFragment();
    }

    TabInfo getTab(int pos);
    int tabCount();
}
