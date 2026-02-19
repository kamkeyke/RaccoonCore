package net.kamkeyke.raccooncore.util;

public class TimeUtils {

    /**
     * Transforms ticks into a readable string (e.g., 72000 -> "1h", 108000 -> "1h 30m")
     */
    public static String formatDurationFromTicks(long ticks) {
        if (ticks < 0) return "∞";

        long totalSeconds = ticks / 20;

        long seconds = totalSeconds % 60;
        long totalMinutes = totalSeconds / 60;

        long minutes = totalMinutes % 60;
        long totalHours = totalMinutes / 60;

        long hours = totalHours % 24;
        long days = totalHours / 24;

        StringBuilder sb = new StringBuilder();

        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        if ((seconds > 0 && hours <= 0) || sb.isEmpty()) sb.append(seconds).append("s");

        return sb.toString().trim();
    }
}
