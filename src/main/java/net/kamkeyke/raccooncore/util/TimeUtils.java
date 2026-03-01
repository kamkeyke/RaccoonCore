package net.kamkeyke.raccooncore.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Small utility class for time-related calculations and formatting within the Minecraft environment.
 * <p>
 * This class provides methods to convert game ticks into human-readable strings.
 */
public class TimeUtils {
    private static final Pattern DURATION_PATTERN = Pattern.compile("(\\d+)\\s*([dhms])");

    /**
     * Formats a duration given in game ticks into a human-readable string.
     * <p>The format scales dynamically:
     * <ul>
     *      <li>Values over 24h include days (e.g., "1d 2h 30m")</li>
     *      <li>Values over 1h omit seconds for brevity (e.g., "1h 15m")</li>
     *      <li>Small values show minutes and seconds (e.g., "2m 30s")</li>
     * </ul>
     * @param ticks The amount of game ticks to format.
     * @return A formatted string (e.g., "1h 30m", "45s", "∞" if negative).
     */
    public static String formatTicksToDuration(long ticks) {
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

    /**
     * Parses a human-readable duration string into game ticks.
     * <p>
     * The parser identifies units regardless of spacing or order:
     * <ul>
     * <li>Supports days (d), hours (h), minutes (m), and seconds (s)</li>
     * <li>Examples: "1d 12h", "30m 10s", "1h30m"</li>
     * <li>The order does not matter (e.g., "10s 1m" is valid)</li>
     * </ul>
     * @param duration The human-readable string to be parsed.
     * @return The total duration in ticks, or 0 if no valid patterns are found.
     * Returns -1 if the input is "∞" or null.
     */
    public static long parseDurationToTicks(String duration) {
        if (duration == null || duration.equalsIgnoreCase("∞")) return -1;

        long totalTicks = 0;
        Matcher matcher = DURATION_PATTERN.matcher(duration.toLowerCase());
        boolean found = false;

        while (matcher.find()) {
            found = true;
            long value = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2);

            switch (unit) {
                case "d" -> totalTicks += value * 24 * 60 * 60 * 20;
                case "h" -> totalTicks += value * 60 * 60 * 20;
                case "m" -> totalTicks += value * 60 * 20;
                case "s" -> totalTicks += value * 20;
            }
        }

        return found ? totalTicks : 0;
    }
}
