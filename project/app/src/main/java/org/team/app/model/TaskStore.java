
package org.team.app.model;

import org.team.app.view.ActivityListener;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class TaskStore {
    // TODO support multiple tasks
    // TODO serialize and store in bundle

    protected Task currentTask = null;
    protected final String defaultTaskName;

    public static interface Listener {
        public void onCurrentTaskUpdate(Task newTask);
    }

    Set<Listener> listeners;

    public void subscribe(Listener listener) {
        listeners.add(listener);
    }

    public TaskStore(String defaultTaskName) {
        this.defaultTaskName = defaultTaskName;
        // weak hash map so old listeners that have been GC'd will be removed
        this.listeners = Collections.newSetFromMap(new WeakHashMap<Listener,Boolean>());
    }

    public void createTask(String taskName) {
        currentTask = new Task(taskName);
        for(Listener listener: listeners)
            listener.onCurrentTaskUpdate(currentTask);
    }

    public Task getCurrentTask() {
        if(currentTask == null)
            createTask(defaultTaskName);
        return currentTask;
    }

}
