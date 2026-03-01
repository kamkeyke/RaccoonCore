package net.kamkeyke.raccooncore.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
     * Builds a human-readable {@link Component} containing the display names of the
     * provided players.
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
        List<Component> names = players.stream().map(Player::getDisplayName).toList();

        int size = names.size();

        if(size == 1) return names.get(0);
        if(size == 2) return Component.empty()
                .append(names.get(0))
                .append(Component.translatable("misc.raccooncore.buildViewersList.and"))
                .append(names.get(1));

        MutableComponent result = Component.empty();

        for(int i = 0; i < size; i++){
            if(i > 0) {
                if(i == size - 1){
                    result.append(Component.translatable("misc.raccooncore.buildViewersList.and"));
                } else {
                    result.append(", ");
                }
            }
            result.append(names.get(i));
        }

        return result;
    }
}
