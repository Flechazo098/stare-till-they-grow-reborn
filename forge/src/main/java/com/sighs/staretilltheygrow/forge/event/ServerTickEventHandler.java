package com.sighs.staretilltheygrow.forge.event;

import com.sighs.staretilltheygrow.actions.block.*;
import com.sighs.staretilltheygrow.actions.entity.*;
import com.sighs.staretilltheygrow.dictionaries.PlayerTargetDictionary;
import com.sighs.staretilltheygrow.forge.StareTillTheyGrowForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StareTillTheyGrowForge.MOD_ID)
public final class ServerTickEventHandler {
    @SubscribeEvent
    public static void serverTickHandler(TickEvent.ServerTickEvent event) {
        if (event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END) {
            PlayerTargetDictionary.forEach((player, target) -> {
                target.tick();
                if (target.canInvoke()) {
                    if (target instanceof PlayerTargetDictionary.PlayerBlockTarget pb) {
                        new ApplyBoneMealAction(pb).invoke();
                        new RegrowCakeAction(pb).invoke();
                    } else if (target instanceof PlayerTargetDictionary.PlayerEntityTarget pe) {
                        new FallInLoveAction(pe).invoke();
                        new GrowUpAction(pe).invoke();
                        new RegrowWoolAction(pe).invoke();
                        pe.getEntity().ifPresent(v ->
                                new TraderRestockAction(pe, v, player).invoke()
                        );
                    }
                }
            });
        }
    }
}