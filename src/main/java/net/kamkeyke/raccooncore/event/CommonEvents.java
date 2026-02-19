package net.kamkeyke.raccooncore.event;

import net.kamkeyke.raccooncore.RaccoonCore;
import net.kamkeyke.raccooncore.util.ServerScheduler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class CommonEvents {

    @Mod.EventBusSubscriber(modid = RaccoonCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents{

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event){
            if (event.phase != TickEvent.Phase.END) return;

            ServerScheduler.tick();
        }
    }

    @Mod.EventBusSubscriber(modid = RaccoonCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {

    }
}
