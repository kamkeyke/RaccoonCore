package net.kamkeyke.raccooncore.misc.scheduler;

import net.kamkeyke.raccooncore.RaccoonCore;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A tick-based task scheduler for the server environment.
 * <p>This utility allows developers to defer task execution for a specific number
 * of game ticks. It runs exclusively on the server's main thread, making it safe
 * for world, entity, and block manipulations.
 * <p><strong>Implementation Note:</strong> This scheduler is automatically managed
 * by the {@code RaccoonCore}. Never manually call {@link #tick()}
 * if this mod is active, as it is already called by the mod in the {@link TickEvent.ServerTickEvent}.
 */
public class ServerTaskScheduler {
    /**
     * List of tasks currently waiting to be executed.
     */
    private static final List<ScheduledTask> TASKS = new ArrayList<>();
    private static final List<ScheduledTask> PENDING = new ArrayList<>();

    /**
     * Schedules a task to be executed after a specified time in ticks.
     *
     * @param ticks Time in game ticks that the task should wait before being
     *              executed (e.g., 20 ticks = 1 second).
     * @param task  The {@link Runnable} containing the logic to be executed.
     */
    public static void schedule(int ticks, Runnable task) {
        scheduleTask(ticks, task, null);
    }

    /**
     * Schedules a task to be executed after a specified time in ticks.
     *
     * @param ticks Time in game ticks that the task should wait before being
     *              executed (e.g., 20 ticks = 1 second).
     * @param task  The {@link Runnable} containing the logic to be executed.
     * @param owner Optional owner tag that can be used to cancel all tasks with
     *              that same owner. <p><i><b>Note:</b> owner doesn't have to be a modid or
     *              player name. Any name will do, as long the other correlated
     *              tasks have the same owner tag.</i>
     */
    public static void schedule(int ticks, Runnable task, String owner) {
        scheduleTask(ticks, task, owner);
    }

    private static void scheduleTask(int ticks, Runnable task, String owner) {
        if (ticks < 0) ticks = 0;
        synchronized (PENDING) {
            PENDING.add(new ScheduledTask(ticks, task, owner));
        }
    }

    /**
     * Cancel all pending or happening tasks that have the given owner tag.
     *
     * @param owner owner tag
     */
    public static void cancelAllWithOwner(String owner) {
        if (owner == null) return;
        for (ScheduledTask t : TASKS) {
            if (owner.equals(t.owner)) t.cancelled = true;
        }
        synchronized (PENDING) {
            for (ScheduledTask t : PENDING) {
                if (owner.equals(t.owner)) t.cancelled = true;
            }
        }
    }

    /**
     * Advances the countdown for all registered tasks and execute the tasks when the countdown reaches zero.
     *
     <p>This method is normally invoked internally by RaccoonCore's server
     * tick event handler and should NOT be called manually when RaccoonCore
     * is active.</p>
     *
     * During the server tick, when a task's timer reaches zero, it is executed
     * and removed from the queue. Exceptions within tasks are caught and logged
     * to prevent server-side crashes.
     */
    public static void tick() {
        synchronized (PENDING){
            if(!PENDING.isEmpty()){
                TASKS.addAll(PENDING);
                PENDING.clear();
            }
        }

        Iterator<ScheduledTask> it = TASKS.iterator();
        while (it.hasNext()) {
            ScheduledTask task = it.next();

            if (task.cancelled) {
                it.remove();
                continue;
            }

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

        // Note: any tasks scheduled during execution are in PENDING and will be moved
        // at the start of the next tick, which avoids concurrent modification issues.
    }

    /**
     * Internal data class representing a task with its remaining delay.
     */
    private static class ScheduledTask {
        int ticks;
        Runnable task;
        final String owner;
        volatile boolean cancelled = false;

        ScheduledTask(int ticks, Runnable task, String owner) {
            this.ticks = Math.max(0, ticks);
            this.task = task;
            this.owner = owner;
        }
    }
}
