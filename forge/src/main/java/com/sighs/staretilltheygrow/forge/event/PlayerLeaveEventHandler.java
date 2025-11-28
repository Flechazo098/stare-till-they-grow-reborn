package com.sighs.staretilltheygrow.forge.event;

import com.sighs.staretilltheygrow.dictionaries.PlayerTargetDictionary;
import com.sighs.staretilltheygrow.forge.StareTillTheyGrowForge;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StareTillTheyGrowForge.MOD_ID)
public class PlayerLeaveEventHandler {
    @SubscribeEvent
    public static void playerLeftHandler(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerTargetDictionary.unregister(player);
        }
    }
}