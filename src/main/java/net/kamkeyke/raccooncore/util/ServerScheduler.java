package net.kamkeyke.raccooncore.util;

import net.kamkeyke.raccooncore.misc.scheduler.ServerTaskScheduler;

/**
 * @deprecated Use {@link ServerTaskScheduler}
 * instead. This class will be removed in RaccoonCore 3.0.0.
 *
 * <p>This class is kept for backwards compatibility and will be removed in a
 * future major version of RaccoonCore.</p>
 *
 * <p>All methods in this class delegate directly to the new
 * {@link ServerTaskScheduler} implementation.</p>
 */
@Deprecated(since = "2.6.0", forRemoval = true)
public final class ServerScheduler {

    /**
     * Schedules a task to be executed after a specified time in ticks.
     *
     * @param ticks Time in game ticks that the task should wait before being
     *              executed (e.g., 20 ticks = 1 second).
     * @param task  The {@link Runnable} containing the logic to be executed.
     *
     * @deprecated Use {@link ServerTaskScheduler#schedule(int, Runnable)}
     * instead.
     */
    @Deprecated(since = "2.6.0", forRemoval = true)
    public static void schedule(int ticks, Runnable task) {
        ServerTaskScheduler.schedule(ticks, task);
    }

    /**
     * Schedules a task to be executed after a specified time in ticks.
     *
     * @param ticks Time in game ticks that the task should wait before being
     *              executed (e.g., 20 ticks = 1 second).
     * @param task  The {@link Runnable} containing the logic to be executed.
     * @param owner Optional owner tag that can be used to cancel all tasks with
     *              that same owner.
     *
     * @deprecated Use {@link ServerTaskScheduler#schedule(int, Runnable, String)}
     * instead.
     */
    @Deprecated(since = "2.6.0", forRemoval = true)
    public static void schedule(int ticks, Runnable task, String owner) {
        ServerTaskScheduler.schedule(ticks, task, owner);
    }

    /**
     * Cancels all pending or active tasks that have the given owner tag.
     *
     * @param owner Owner tag.
     *
     * @deprecated Use {@link ServerTaskScheduler#cancelAllWithOwner(String)}
     * instead.
     */
    @Deprecated(since = "2.6.0", forRemoval = true)
    public static void cancelAllWithOwner(String owner) {
        ServerTaskScheduler.cancelAllWithOwner(owner);
    }

    /**
     * Advances the scheduler by one server tick.
     *
     * <p>This method is normally invoked internally by RaccoonCore's server
     * tick event handler and should not be called manually when RaccoonCore
     * is active.</p>
     *
     * @deprecated You still shouldn't use this... But if you do for some reason,
     * use {@link ServerTaskScheduler#tick()} instead.
     */
    @Deprecated(since = "2.6.0", forRemoval = true)
    public static void tick() {
        ServerTaskScheduler.tick();
    }
}