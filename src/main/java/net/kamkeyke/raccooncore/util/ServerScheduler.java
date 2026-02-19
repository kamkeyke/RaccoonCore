package net.kamkeyke.raccooncore.util;

import net.kamkeyke.raccooncore.RaccoonCore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerScheduler {
    private static final List<ScheduledTask> TASKS = new ArrayList<>();

    public static void schedule(int ticks, Runnable task) {
        TASKS.add(new ScheduledTask(ticks, task));
    }

    public static void tick() {
        Iterator<ScheduledTask> it = TASKS.iterator();
        while (it.hasNext()) {
            ScheduledTask task = it.next();
            task.ticks--;

            if (task.ticks <= 0) {
                try {
                    task.task.run();
                } catch (Exception e) {
                    // Evita que um erro em uma tarefa agendada trave o servidor inteiro
                    RaccoonCore.LOGGER.error("Error while trying to run scheduled task: {}", e.getMessage());
                }
                it.remove();
            }
        }
    }

    private static class ScheduledTask {
        int ticks;
        Runnable task;

        ScheduledTask(int ticks, Runnable task) {
            this.ticks = ticks;
            this.task = task;
        }
    }
}
