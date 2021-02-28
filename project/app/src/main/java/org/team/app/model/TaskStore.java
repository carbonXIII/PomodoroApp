
package org.team.app.model;
//Task Updates and show current task (for now)
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
