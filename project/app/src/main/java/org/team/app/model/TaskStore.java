
package org.team.app.model;

import org.team.app.view.ActivityListener;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/// The TaskStore representation
public class TaskStore {
    // TODO support multiple tasks
    // TODO serialize and store in bundle

    protected Task currentTask = null;
    protected final String defaultTaskName;

    /// Listener for Task Store Updates
    public static interface Listener {
        /// Called when the current task is changed
        public void onCurrentTaskUpdate(Task newTask);
    }

    Set<Listener> listeners;

    /// Add a subscriber
    public void subscribe(Listener listener) {
        listeners.add(listener);
    }

    /// Construct an in-memory Task Store
    /// @param defaultTaskName: the name for the default task
    public TaskStore(String defaultTaskName) {
        this.defaultTaskName = defaultTaskName;
        // weak hash map so old listeners that have been GC'd will be removed
        this.listeners = Collections.newSetFromMap(new WeakHashMap<Listener,Boolean>());
    }


    /// Create a task and set it as the current task
    public void createTask(String taskName) {
        currentTask = new Task(taskName);
        for(Listener listener: listeners)
            listener.onCurrentTaskUpdate(currentTask);
    }

    /// Get a reference to the current task
    public Task getCurrentTask() {
        if(currentTask == null)
            createTask(defaultTaskName);
        return currentTask;
    }
}
