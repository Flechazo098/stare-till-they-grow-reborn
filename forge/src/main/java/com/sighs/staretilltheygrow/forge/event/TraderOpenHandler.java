package com.sighs.staretilltheygrow.forge.event;

import com.sighs.staretilltheygrow.eventhooks.TraderOpenHooks;
import com.sighs.staretilltheygrow.forge.StareTillTheyGrowForge;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StareTillTheyGrowForge.MOD_ID)
public class TraderOpenHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onTradeOpen(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getTarget() instanceof AbstractVillager trader) {
            TraderOpenHooks.onOpen(trader);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof AbstractVillager trader) {
            TraderOpenHooks.onInteract(trader);
        }
    }
}