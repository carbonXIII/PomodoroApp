import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import org.team.app.model.Task;
import org.team.app.model.TaskStore;
import org.team.app.model.TimerType;

import java.util.UUID;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.ref.WeakReference;

class TaskTest {
    static class MockSubscriber implements Task.Listener, TaskStore.Listener {
        public String name = null;
        public String category = null;
        protected long duration = 0;

        @Override
        public void onCurrentTaskUpdate(Task newTask) {
            this.name = newTask.getName();
        }

        @Override
        public void onTaskAdded(Task newTask) {
        }

        @Override
        public void onTaskNameUpdate(Task task, String name) {
            this.name = name;
        }

        @Override
        public void onTaskCategoryUpdate(Task task, String category) {
            this.category = category;
        }

        @Override
        public void onTaskTimerDurationUpdate(Task task, TimerType timer, long newDuration) {
            this.duration = newDuration;
        }
    }

    @Test
    // UID 022 RID 000 have a user be able to name a task.
    void createTaskWithName() {
        String testName = "TEST";
        Task task = new Task(testName, testName);
        assertEquals(task.getName(), testName);
        assertEquals(task.getCategory(), testName);
    }

    @Test
    // UID 003 RID 012 each task should have a unique id number.
    void taskShouldHaveUniqueUUID() {
        Task task = new Task("A", "A");
        Task task2 = new Task("A", "A");

        assertNotEquals(task.getUUID(), task2.getUUID());
    }

    @Test
    // UID 001 RID 015 model updates should propogate to subscribers
    void updatingTaskNameShouldUpdateSubscribers() {
        MockSubscriber sub = new MockSubscriber();

        Task task = new Task("A", "A");
        task.subscribe(sub);

        String updatedTaskName = UUID.randomUUID().toString();
        task.setName(updatedTaskName);

        assertEquals(sub.name, updatedTaskName);
    }

    @Test
    // UID 001 RID 015 model updates should propogate to subscribers
    void updatingTaskTimerDurationShouldUpdateSubscribers() {
        MockSubscriber sub = new MockSubscriber();

        Task task = new Task("A", "A");
        task.subscribe(sub);

        long updatedTaskDuration = 500;
        task.setTimerDuration(TimerType.WORK, updatedTaskDuration);

        assertEquals(sub.duration, updatedTaskDuration);
    }

    @Test
    // UID 001 RID 014
    // Listener maps in Task/TaskStore should hold weak references to
    // listeners so that they can be GC'd.
    void listenerMapsShouldHoldWeakReferences() {
        TaskStore store = new TaskStore("A", "A");

        final int M = 1000;
        MockSubscriber[] subs = new MockSubscriber[M];

        // Setup the first mock subscribe with a weak reference
        // If the object eventually gets GC'd, the reference should
        // become null.
        subs[0] = new MockSubscriber();
        store.subscribe(subs[0]);
        store.getCurrentTask().subscribe(subs[0]);
        WeakReference shouldDie = new WeakReference(subs[0]);

        for (int i = 0; i < 10000 && shouldDie.get() != null; i++) {
            subs[i % M] = new MockSubscriber();
            store.subscribe(subs[i % M]);
            store.getCurrentTask().subscribe(subs[i % M]);

            if(i == 0)
                System.gc();
        }

        assert(shouldDie.get() == null);
    }

    @Test
    void taskStoreShouldStartWithDefaultTask() {
        String defaultName = "default";
        TaskStore store = new TaskStore(defaultName, "general");
        assertEquals(store.getCurrentTask().getName(), defaultName);
    }

    @Test
    //UID 022 RID 010 the created task should be on the screen.
    void taskStoreCreateTaskShouldAddTaskToList() {
        TaskStore store = new TaskStore("default", "general");

        String taskName = "TEST";
        UUID task = store.createTask(taskName, null);
        Task val = store.getTaskByUUID(task);

        assertNotNull(val);
    }

    @Test
    // UID 001 RID 015 model updates should propogate to subscribers
    void updateCurrentTaskShouldUpdateSubscribers() {
        MockSubscriber sub = new MockSubscriber();

        TaskStore store = new TaskStore("default", "general");
        store.subscribe(sub);

        String updatedTaskName = UUID.randomUUID().toString();
        UUID task = store.createTask(updatedTaskName, null);
        store.setCurrentTask(task);

        assertEquals(sub.name, updatedTaskName);
    }

    @Test
    // UID 019 RID 025 The task list should contain all tabs by default
    void getTaskWithEmptyFilterShouldReturnAListofAllTasks() {
        TaskStore store = new TaskStore("default", "general");

        ArrayList<UUID> expected = new ArrayList<UUID>();
        expected.add(store.createTask("A", null));
        expected.add(store.createTask("B", null));
        expected.add(store.createTask("C", null));
        expected.add(store.createTask("D", null));

        Collection<Task> got = store.getTasks("");
        assertEquals(got.size(), expected.size());

        for (Task task : got)
            assert(expected.contains(task.getUUID()));
    }

    @Test
    // UID 019 RID 031 Typing in a filter will filter the list of displayed tasks
    void getTaskWithFilterShouldReturnReasonableSubsetOfTasks() {
        TaskStore store = new TaskStore("default", "general");

        ArrayList<UUID> expected = new ArrayList<UUID>();
        expected.add(store.createTask("foo A", null));
        expected.add(store.createTask("foo B", null));
        expected.add(store.createTask("foobar C", null));
        store.createTask("bar D", null);
        store.createTask("fobar D", null);

        Collection<Task> got = store.getTasks("foo");

        for (Task task : got)
            assert(expected.contains(task.getUUID()));
    }
}
