package net.kamkeyke.raccooncore.util;

import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.List;

/**
 * Utility class containing helper methods for building formatted text {@link Component}s.
 * <p>
 * This class centralizes common text formatting logic used across commands,
 * messages and UI-related things.
 */
public class TextUtils {

    /**
     * Builds a human-readable {@link Component} containing the provided players' display names.
     * <ul>
     *      <li>1 player → {@code Player}</li>
     *      <li>2 players → {@code Player1 and Player2}</li>
     *      <li>3+ players → {@code Player1, Player2 and Player3}</li>
     * </ul>
     * <p>
     * The returned component preserves each player's display name formatting
     * and defers translation resolution to the client.
     *
     * @param players the collection of players whose names should be formatted
     * @return a formatted {@link Component} representing the player list
     */
    public static Component buildPlayerList(Collection<? extends Player> players){
        List<MutableComponent> names = players.stream().map(player -> player.getDisplayName().copy()).toList();

        return buildComponentList(names);
    }

    /**
     * Builds a human-readable {@link Component} containing the provided players' display names.
     * Clicking a player's name will copy their nickname to the clipboard.
     * <ul>
     *      <li>1 player → {@code Player}</li>
     *      <li>2 players → {@code Player1 and Player2}</li>
     *      <li>3+ players → {@code Player1, Player2 and Player3}</li>
     * </ul>
     * <p>
     * The returned component preserves each player's display name formatting
     * and defers translation resolution to the client.
     *
     * @param players the collection of players whose names should be formatted
     * @return a formatted {@link Component} representing the player list
     */
    public static Component buildPlayerListCopyName(Collection<? extends Player> players){
        List<MutableComponent> names = players.stream().map(player -> player
                .getDisplayName().copy().withStyle(s -> s
                        .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, player.getName().getString()))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.raccooncore.copy.nickname.click")))
                )
        ).toList();

        return buildComponentList(names);
    }

    /**
     * Builds a human-readable {@link Component} containing the provided players' display names.
     * Clicking a player's name will copy their {@link java.util.UUID} to the clipboard.
     * <ul>
     *      <li>1 player → {@code Player}</li>
     *      <li>2 players → {@code Player1 and Player2}</li>
     *      <li>3+ players → {@code Player1, Player2 and Player3}</li>
     * </ul>
     * <p>
     * The returned component preserves each player's display name formatting
     * and defers translation resolution to the client.
     *
     * @param players the collection of players whose names should be formatted
     * @return a formatted {@link Component} representing the player list
     */
    public static Component buildPlayerListCopyUUID(Collection<? extends Player> players){
        List<MutableComponent> names = players.stream().map(player -> player
                .getDisplayName().copy().withStyle(s -> s
                        .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, player.getStringUUID()))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.raccooncore.copy.uuid.click")))
                )
        ).toList();

        return buildComponentList(names);
    }

    public static Component buildComponentList(List<MutableComponent> components){
        if (components.isEmpty()) return Component.empty();

        int size = components.size();

        if(size == 1) return components.get(0);
        if(size == 2) return Component.empty()
                .append(components.get(0))
                .append(Component.translatable("misc.raccooncore.buildViewersList.and"))
                .append(components.get(1));

        MutableComponent result = Component.empty();

        for(int i = 0; i < size; i++){
            if(i > 0) {
                if(i == size - 1){
                    result.append(Component.translatable("misc.raccooncore.buildViewersList.and"));
                } else {
                    result.append(", ");
                }
            }
            result.append(components.get(i));
        }

        return result;
    }

    public static Component buildComponentUnorderedList(List<MutableComponent> components){
        if (components.isEmpty()) return Component.empty();

        int size = components.size();

        MutableComponent result = Component.literal("- ");

        for(int i = 0; i < size; i++){
            result.append(components.get(i));
            if(i < size - 1){

                result.append("\n- ");
            }
        }

        return result;
    }
}
