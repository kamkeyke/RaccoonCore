package net.kamkeyke.raccooncore.util;

import net.kamkeyke.raccooncore.RaccoonCore;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A lightweight, tick-based task scheduler for the server environment.
 * <p>This utility allows developers to defer task execution for a specific number
 * of game ticks. It runs exclusively on the server's main thread, making it safe
 * for world, entity, and block manipulations.
 * <p><strong>Implementation Note:</strong> This scheduler is automatically managed
 * by the {@code RaccoonCore}. Never manually call {@link #tick()}
 * if this mod is active, as it is already called by the mod in the {@link TickEvent.ServerTickEvent}.
 */
public class ServerScheduler {
    /**
     * List of tasks currently waiting to be executed.
     */
    private static final List<ScheduledTask> TASKS = new ArrayList<>();

    /**
     * Schedules a task to be executed after a specified time in ticks.
     * @param ticks Time in game ticks that the task should wait before being
     *              executed (e.g., 20 ticks = 1 second).
     * @param task  The {@link Runnable} containing the logic to be executed.
     */
    public static void schedule(int ticks, Runnable task) {
        TASKS.add(new ScheduledTask(ticks, task));
    }

    /**
     * Advances the countdown for all registered tasks and execute the tasks when the countdown reaches zero.
     * <p>This method is invoked internally by {@code RaccoonCore}'s event handlers
     * during the server tick. When a task's timer reaches zero, it is executed
     * and removed from the queue. Exceptions within tasks are caught and logged
     * to prevent server-side crashes.
     */
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

    /**
     * Internal data class representing a task with its remaining delay.
     */
    private static class ScheduledTask {
        int ticks;
        Runnable task;

        ScheduledTask(int ticks, Runnable task) {
            this.ticks = ticks;
            this.task = task;
        }
    }
}
