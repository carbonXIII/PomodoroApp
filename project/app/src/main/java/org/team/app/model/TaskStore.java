
package org.team.app.model;

import org.team.app.view.ActivityListener;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ConcurrentModificationException;
import java.util.TreeSet;
import java.util.Collection;

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

        public void onTaskAdded(Task newTask);
    }

    Set<Listener> listeners;
    Map<UUID, Task> list;

    private Set<Listener> getListeners() {
        synchronized (listeners) {
            Set<Listener> ret = null;
            while (ret == null) {
                try {
                    ret = new HashSet<Listener>(listeners);
                } catch (ConcurrentModificationException e) {
                    ret = null;
                }
            }
            return ret;
        }
    }

    /// Add a subscriber
    public void subscribe(Listener listener) {
        synchronized(listeners) {
            listeners.add(listener);
        }
    }

    /// Construct an in-memory Task Store
    /// @param defaultTaskName: the name for the default task
    public TaskStore(String defaultTaskName) {
        this.defaultTaskName = defaultTaskName;
        // weak hash map so old listeners that have been GC'd will be removed
        this.listeners = Collections.newSetFromMap(new WeakHashMap<Listener,Boolean>());
        this.list = new HashMap<UUID, Task>();
    }


    /// Create a task
    public UUID createTask(String taskName) {
        Task task = new Task(taskName == null ? defaultTaskName : taskName);
        list.put(task.getUUID(), task);

        if(this.currentTask == null)
            this.currentTask = task;

        for (Listener listener : getListeners())
            listener.onTaskAdded(task);

        return task.getUUID();
    }

    public void setCurrentTask(UUID task) {
        Task target = list.get(task);
        if(target == null)
            return;

        this.currentTask = target;

        for (Listener listener : getListeners())
            listener.onCurrentTaskUpdate(currentTask);
    }

    /// Get a reference to the current task
    public Task getCurrentTask() {
        if(currentTask == null) {
            setCurrentTask(createTask(null));
        }
        return currentTask;
    }

    public Collection<Task> getTasks(String filter) {
        filter = filter.toLowerCase();

        TreeSet<Task> ret = new TreeSet<Task>();
        for(Map.Entry<UUID, Task> task: list.entrySet()) {
            // TODO: this is definitely slow, probably KMP for contains in the future
            if(filter.length() == 0 || task.getValue().getName().toLowerCase().contains(filter)) {
                ret.add(task.getValue());
            }
        }

        return (Collection<Task>) ret;
    }

    public Task getTaskByUUID(UUID uuid) {
        return list.get(uuid);
    }
}
