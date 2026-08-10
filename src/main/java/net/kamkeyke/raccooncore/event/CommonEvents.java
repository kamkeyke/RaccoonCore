package net.kamkeyke.raccooncore.event;

import com.mojang.brigadier.CommandDispatcher;
import net.kamkeyke.raccooncore.RaccoonCore;
import net.kamkeyke.raccooncore.command.UuidCommand;
import net.kamkeyke.raccooncore.misc.scheduler.ServerTaskScheduler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class CommonEvents {

    @Mod.EventBusSubscriber(modid = RaccoonCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents{

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event){
            if (event.phase != TickEvent.Phase.END) return;

            ServerTaskScheduler.tick();
        }

        @SubscribeEvent
        public static void registerCommandsEvent(RegisterCommandsEvent event){
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

            UuidCommand.register(dispatcher);
        }
    }

    @Mod.EventBusSubscriber(modid = RaccoonCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {

    }
}
