package org.team.app.view;

import androidx.fragment.app.Fragment;

/// Main interface allowing fragments and other UI elements to send messages to the activity
public interface ActivityListener {
    /// Stores the information about a task
    interface TabInfo {
        /// Get the tab title
        String getTitle();

        /// Get the stored fragment to display for this tab
        Fragment getFragment();
    }

    /// Get the TabInfo object for some tab position
    TabInfo getTab(int pos);

    /// Get the number of tabs
    int tabCount();

    /// Hide the soft keyboard
    void hideKeyboard();
}
