import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import org.team.app.model.Task;
import org.team.app.model.TaskStore;

class TaskTest {
    @Test
    void createTaskWithName() {
        String testName = "TEST NAME";
        Task task = new Task(testName);
        assertEquals(task.getName(), testName);
    }

    @Test
    void taskShouldHaveUniqueUUID() {
        Task task = new Task("A");
        Task task2 = new Task("A");

        assertNotEquals(task.getUUID(), task2.getUUID());
    }

    @Test
    void taskStoreShouldStartWithoutCurrentTask() {
        TaskStore store = new TaskStore();
        assertEquals(store.getCurrentTask(), null);
    }

    @Test
    void taskStoreCreateTaskShouldSetCurrentTask() {
        TaskStore store = new TaskStore();

        String taskName = "TEST";
        store.createTask(taskName);

        assertEquals(store.getCurrentTask().getName(), taskName);
    }
}
