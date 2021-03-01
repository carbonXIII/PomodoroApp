package org.team.app.model;

import java.util.UUID;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class Task {
    protected final UUID uuid;
    public String name;

    public static interface Listener {
        // TODO: rename this to avoid collisions
        public void onTaskNameUpdate(Task task, String newName);
    }

    Set<Listener> listeners;

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

    public final String getName() {
        return this.name;
    }

    public final void setName(String name) {
        this.name = name;
        for(Listener listener: listeners)
            listener.onTaskNameUpdate(this, name);
    }

    public final UUID getUUID() {
        return this.uuid;
    }
}
