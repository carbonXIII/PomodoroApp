import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import org.team.app.model.Task;
import org.team.app.model.TaskStore;

class TaskTest {
    @Test //UID 022 RID 000 have a user be able to name a task.
    void createTaskWithName() {
        String testName = "TEST";
        Task task = new Task(testName);
        assertEquals(task.getName(), testName);
    }

    @Test // UID 003 RID 012 each task should have a unique id number.
    void taskShouldHaveUniqueUUID() {
        Task task = new Task("A");
        Task task2 = new Task("A");

        assertNotEquals(task.getUUID(), task2.getUUID());
    }

    @Test //Update when we have timer functionality, allow for timer to start without a task.
    void taskStoreShouldStartWithoutCurrentTask() {
        TaskStore store = new TaskStore();
        assertEquals(store.getCurrentTask(), null);
    }

    @Test //UID 022 RID 010 the created task should stay on screen.
    void taskStoreCreateTaskShouldSetCurrentTask() {
        TaskStore store = new TaskStore();

        String taskName = "TEST";
        store.createTask(taskName);

        assertEquals(store.getCurrentTask().getName(), taskName);
    }
}
