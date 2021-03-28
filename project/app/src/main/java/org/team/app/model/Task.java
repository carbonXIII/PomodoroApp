package org.team.app.model;

import java.util.UUID;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.HashSet;
import java.util.ConcurrentModificationException;

import static org.team.app.model.TimerType.WORK;

/// Task representation
public class Task implements Comparable<Task> {
    protected final UUID uuid;
    protected String name;
    protected String category;

    protected static final int DEFAULT_WORK_TIME = 25 * 60 * 1000;
    protected static final int DEFAULT_BREAK_TIME = 5* 60 * 1000;

    protected long workDuration = DEFAULT_WORK_TIME;
    protected long breakDuration = DEFAULT_BREAK_TIME;

    /// Listener for Task updates
    public static interface Listener {
        /// Called when the task's name is updated
        /// @param task: A reference to the task for listeners that manange multiple Tasks
        /// @param newName: The updated name
        public void onTaskNameUpdate(Task task, String newName);

        public void onTaskCategoryUpdate(Task task, String newCategory);

        /// Called when the duration of one of the timer type's is updated
        /// @param task: A reference to the task for listeners that manange multiple Tasks
        /// @param type: The timer type that is being updated
        /// @param newDuration: The updated duration (ms)
        public void onTaskTimerDurationUpdate(Task task, TimerType type, long newDuration);
    }

    protected Set<Listener> listeners;

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

    /// Add a listener
    public void subscribe(Listener listener) {
        synchronized(listeners) {
            listeners.add(listener);
        }
    }

    /// Remove a listener
    public void unsubscribe(Listener listener) {
        synchronized (listeners) {
            if (listeners.contains(listener))
                listeners.remove(listener);
        }
    }

    /// Construct a task
    public Task(String name, String category) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.category = category;

        this.listeners = Collections.newSetFromMap(new WeakHashMap<Listener,Boolean>());
    }

    public String getName() {
        return this.name;
    }

    /// Sets the name, and calls attached listeners with the update
    public void setName(String name) {
        this.name = name;

        for (Listener listener : getListeners())
            listener.onTaskNameUpdate(this, name);
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;

        for(Listener listener: getListeners()) {
            listener.onTaskCategoryUpdate(this, category);
        }
    }

    /// Sets the duration for a timer type, and calls attached listeners with the update
    public void setTimerDuration(TimerType type, long duration) {
        if(getTimerDuration(type) == duration)
            return;

        switch(type) {
        case WORK:
            this.workDuration = duration;
            break;
        case BREAK:
            this.breakDuration = duration;
            break;
        }

        for (Listener listener : getListeners())
            listener.onTaskTimerDurationUpdate(this, type, duration);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    /// Get the timer duration for a timer type
    public long getTimerDuration(TimerType type) {
        switch(type) {
        case WORK: return workDuration;
        case BREAK: return breakDuration;
        }
        return 0;
    }

    @Override
    public int compareTo(Task other) {
        if(this.name.compareTo(other.getName()) != 0)
            return this.name.compareTo(other.getName());
        return this.uuid.compareTo(other.getUUID());
    }
}
