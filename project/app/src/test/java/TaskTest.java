import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import org.team.app.model.Task;
import org.team.app.model.TaskStore;

import java.util.UUID;

class TaskTest {
    static class MockSubscriber implements Task.Listener, TaskStore.Listener {
        public String name = null;

        @Override
        public void onCurrentTaskUpdate(Task newTask) {
            this.name = newTask.getName();
        }

        @Override
        public void onTaskNameUpdate(Task task, String name) {
            this.name = name;
        }
    }

    @Test
    void createTaskWithName() {
        String testName = "TEST";
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
    void updatingTaskNameShouldUpdateSubscribers() {
        MockSubscriber sub = new MockSubscriber();

        Task task = new Task("A");
        task.subscribe(sub);

        String updatedTaskName = UUID.randomUUID().toString();
        task.setName(updatedTaskName);

        assertEquals(sub.name, updatedTaskName);
    }

    @Test
    void taskStoreShouldStartWithDefaultTask() {
        String defaultName = "default";
        TaskStore store = new TaskStore(defaultName);
        assertEquals(store.getCurrentTask().getName(), defaultName);
    }

    @Test
    void taskStoreCreateTaskShouldSetCurrentTask() {
        TaskStore store = new TaskStore("default");

        String taskName = "TEST";
        store.createTask(taskName);

        assertEquals(store.getCurrentTask().getName(), taskName);
    }

    @Test
    void updateCurrentTaskShouldUpdateSubscribers() {
        MockSubscriber sub = new MockSubscriber();

        TaskStore store = new TaskStore("default");
        store.subscribe(sub);

        String updatedTaskName = UUID.randomUUID().toString();
        store.createTask(updatedTaskName);

        assertEquals(sub.name, updatedTaskName);
    }
}
