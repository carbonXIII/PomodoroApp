package org.team.app.model;

import java.util.UUID;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class Task {
    protected final UUID uuid;
    protected String name;

    // TODO: get/set/listener for these durations
    protected long workDuration = 25 * 60 * 1000;
    protected long breakDuration = 5 * 60 * 1000;

    public static interface Listener {
        public void onTaskNameUpdate(Task task, String newName);
    }

    protected Set<Listener> listeners;

    public void subscribe(Listener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(Listener listener) {
        if(listeners.contains(listener))
            listeners.remove(listener);
    }

    public Task(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;

        this.listeners = Collections.newSetFromMap(new WeakHashMap<Listener,Boolean>());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        for(Listener listener: listeners)
            listener.onTaskNameUpdate(this, name);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public long getTimerDuration(TimerType type) {
        switch(type) {
        case WORK: return workDuration;
        case BREAK: return breakDuration;
        }
        return 0;
    }
}
