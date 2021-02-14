package org.team.app.model;

import java.util.UUID;

public class Task {
    protected final UUID uuid;
    public final String name;

    public Task(String name) {
        uuid = UUID.randomUUID();
        this.name = name;
    }

    public final String getName() {
        return this.name;
    }
}
