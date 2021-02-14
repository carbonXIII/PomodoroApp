
package org.team.app.model;

public class TaskStore {
    // TODO support multiple tasks
    // TODO serialize and store in bundle

    Task currentTask = null;

    public void createTask(String taskName) {
        currentTask = new Task(taskName);
    }

    public final Task getCurrentTask() {
        return currentTask;
    }
}
