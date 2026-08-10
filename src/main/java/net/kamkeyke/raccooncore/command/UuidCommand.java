package net.kamkeyke.raccooncore.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.GameProfileCache;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class UuidCommand {
    private UuidCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("uuid")
                .then(Commands.argument("nick", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                context.getSource().getServer().getPlayerList().getPlayers().stream()
                                        .map(p -> p.getGameProfile().getName()),
                                builder
                        ))
                        .executes(context -> canonical(context, StringArgumentType.getString(context, "nick")))
                        .then(Commands.literal("int")
                                .executes(context -> intArray(context, StringArgumentType.getString(context, "nick")))
                        )
                )
        );

        dispatcher.register(Commands.literal("nick")
                .then(Commands.argument("uuid", UuidArgument.uuid())
                        .executes(UuidCommand::nick)
                )
        );
    }

    private static int nick(CommandContext<CommandSourceStack> context) {
        UUID uuid = UuidArgument.getUuid(context, "uuid");
        MinecraftServer server = context.getSource().getServer();
        GameProfileCache profileCache = server.getProfileCache();

        if (profileCache == null) {
            context.getSource().sendFailure(Component.translatable("command.raccooncore.profile_cache_unavailable"));
            return 0;
        }

        Optional<com.mojang.authlib.GameProfile> profile = profileCache.get(uuid);

        if (profile.isEmpty()) {
            context.getSource().sendFailure(Component.translatable("command.raccooncore.nick.notfound"));
            return 0;
        }

        send(context, profile.get().getName(), uuid.toString());
        return 1;
    }

    private static int canonical(CommandContext<CommandSourceStack> context, String nick) {
        resolve(context, nick, uuid -> send(context, nick, uuid.toString()));
        return 1;
    }

    private static int intArray(CommandContext<CommandSourceStack> context, String nick) {
        resolve(context, nick, uuid -> {
            int[] p = uuidToIntArray(uuid);
            String intArray = "I; " + p[0] + ", " + p[1] + ", " + p[2] + ", " + p[3];

            context.getSource().sendSuccess(() -> Component.empty()
                    .append(copyable(nick))
                    .append(Component.literal(" UUID: "))
                    .append(copyable(intArray, "[" + intArray + "]")), false);
        });

        return 1;
    }

    private static void resolve(CommandContext<CommandSourceStack> context, String nick, Consumer<UUID> onFound) {
        MinecraftServer server = context.getSource().getServer();
        CommandSourceStack source = context.getSource();
        GameProfileCache profileCache = server.getProfileCache();

        if (profileCache == null) {
            source.sendFailure(Component.translatable("command.raccooncore.profile_cache_unavailable"));
            return;
        }

        profileCache.getAsync(nick, optProfile -> server.execute(() -> {
            if (optProfile.isEmpty()) {
                source.sendFailure(Component.translatable("command.raccooncore.uuid.notfound", nick));
                return;
            }
            onFound.accept(optProfile.get().getId());
        }));
    }

    private static void send(CommandContext<CommandSourceStack> context, String nick, String uuidText) {
        context.getSource().sendSuccess(() -> Component.empty()
                .append(copyable(nick))
                .append(Component.literal(" UUID: "))
                .append(copyable(uuidText)), false);
    }

    private static MutableComponent copyable(String value) {
        return copyable(value, value);
    }

    private static MutableComponent copyable(String displayValue, String copyValue) {
        return Component.literal("["+displayValue+"]").withStyle(style -> style
                .withColor(ChatFormatting.GREEN)
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copyValue))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.copy.click"))));
    }

    private static int[] uuidToIntArray(UUID uuid) {
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();
        return new int[]{(int) (most >> 32), (int) most, (int) (least >> 32), (int) least};
    }
}