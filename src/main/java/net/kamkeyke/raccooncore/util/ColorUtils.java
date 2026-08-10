package net.kamkeyke.raccooncore.util;

import net.kamkeyke.raccooncore.RaccoonCore;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Utility methods for reading, writing and mixing item colors stored via NBT,
 * following the same {@code display.color} convention vanilla uses for dyeable
 * leather items.
 * <p>
 * Also resolves a {@link DyeColor} from colored block-item variants (wool,
 * terracotta, concrete, ...) and mixes dyes together the same way vanilla does
 * for leather armor.
 */
public class ColorUtils {
    private static final Map<DyeColor, Item> WOOL_BY_COLOR = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            WOOL_BY_COLOR.put(color, resolveVariant(color, "minecraft", "_wool"));
        }
    }

    /**
     * Resolves a vanilla colored-block-item variant for a given {@link DyeColor}
     * and suffix (e.g. {@code "_wool"}, {@code "_terracotta"}, {@code "_concrete"}).
     * <p>
     * Use for suffixes other than wool, which already has a lookup table cached.
     */
    public static Item resolveVariant(DyeColor color, String modIdFromBlock, String suffix) {
        Item item = RaccoonCore.simulateItem(modIdFromBlock, color.getName() + suffix);

        if(item == null) RaccoonCore.LOGGER.warn("Could not find item: {}:{}{}", modIdFromBlock, color.getName(), suffix);

        return item;
    }

    /** @return the {@link DyeColor} matching the given stack, if it is a wool item. */
    public static Optional<DyeColor> getWoolColor(ItemStack stack) {
        for (Map.Entry<DyeColor, Item> entry : WOOL_BY_COLOR.entrySet()) {
            if (entry.getValue() == stack.getItem()) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }

    /** Converts a {@link DyeColor} into a packed {@code 0xRRGGBB} value. */
    public static int toRgb(DyeColor color) {
        float[] rgb = color.getTextureDiffuseColors();
        int r = (int) (rgb[0] * 255f);
        int g = (int) (rgb[1] * 255f);
        int b = (int) (rgb[2] * 255f);
        return (r << 16) | (g << 8) | b;
    }

    /**
     * Reads the custom color stored in the item's {@code display.color} NBT tag.
     *
     * @param fallbackColor value returned when the stack has no custom color set
     */
    public static int getColor(ItemStack stack, int fallbackColor) {
        CompoundTag display = stack.getTagElement("display");
        if (display != null && display.contains("color", Tag.TAG_INT)) {
            return display.getInt("color");
        }
        return fallbackColor;
    }

    /** @return {@code true} if the stack has a custom color set via {@link #setColor}. */
    public static boolean hasColor(ItemStack stack) {
        CompoundTag display = stack.getTagElement("display");
        return display != null && display.contains("color", Tag.TAG_INT);
    }

    /** Stores an RGB color ({@code 0xRRGGBB}) in the stack's {@code display.color} NBT tag. */
    public static void setColor(ItemStack stack, int rgb) {
        stack.getOrCreateTagElement("display").putInt("color", rgb);
    }

    private static final int MIX_POWER = 3; // quanto maior, mais rápido um corante repetido domina a mistura
    private static final double SNAP_DISTANCE = 18.0; // raio (euclidiano, 0-441) para arredondar para uma DyeColor pura

    /**
     * Mixes an existing color (or none) with one or more dye stacks. Colors with a higher
     * combined count dominate super-linearly (see {@link #MIX_POWER}), and results close
     * enough to a pure {@link DyeColor} snap to it exactly instead of leaving a faint residue.
     *
     * @param baseColor -1 if the item has no color yet, otherwise its current RGB color
     * @param dyes dye item stacks being applied (stack count is honored)
     */
    public static int mixColors(int baseColor, Iterable<ItemStack> dyes) {
        Map<Integer, Integer> countByColor = new LinkedHashMap<>();

        if (baseColor != -1) {
            countByColor.merge(baseColor, 1, Integer::sum);
        }

        for (ItemStack dyeStack : dyes) {
            if (!(dyeStack.getItem() instanceof DyeItem dyeItem)) continue;
            countByColor.merge(toRgb(dyeItem.getDyeColor()), dyeStack.getCount(), Integer::sum);
        }

        if (countByColor.isEmpty()) return baseColor;

        double r = 0, g = 0, b = 0;
        double totalWeight = 0;
        double totalMax = 0;

        for (Map.Entry<Integer, Integer> entry : countByColor.entrySet()) {
            int rgb = entry.getKey();
            double weight = Math.pow(entry.getValue(), MIX_POWER);

            int cr = (rgb >> 16) & 0xFF;
            int cg = (rgb >> 8) & 0xFF;
            int cb = rgb & 0xFF;

            r += cr * weight;
            g += cg * weight;
            b += cb * weight;
            totalMax += Math.max(cr, Math.max(cg, cb)) * weight;
            totalWeight += weight;
        }

        r /= totalWeight;
        g /= totalWeight;
        b /= totalWeight;
        double avgMax = totalMax / totalWeight;

        double currentMax = Math.max(r, Math.max(g, b));
        if (currentMax > 0) {
            r = r * avgMax / currentMax;
            g = g * avgMax / currentMax;
            b = b * avgMax / currentMax;
        }

        return snapToNearestDyeColor(((int) r << 16) | ((int) g << 8) | (int) b);
    }

    private static int snapToNearestDyeColor(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        for (DyeColor color : DyeColor.values()) {
            int pure = toRgb(color);
            int pr = (pure >> 16) & 0xFF;
            int pg = (pure >> 8) & 0xFF;
            int pb = pure & 0xFF;

            double distance = Math.sqrt(Math.pow(r - pr, 2) + Math.pow(g - pg, 2) + Math.pow(b - pb, 2));
            if (distance <= SNAP_DISTANCE) {
                return pure;
            }
        }

        return rgb;
    }
}