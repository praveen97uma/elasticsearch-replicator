package com.phonepe.plaftorm.es.replicator.commons;

import java.util.Optional;

public class TaskRegistry {
    private Registry<String, Task> tasks = new Registry<>();

    public void registerTask(Task task) {
        tasks.add(task.id(), task);
    }

    public Optional<Task> getTaskById(String taskId) {
        return tasks.get(taskId);
    }
}
