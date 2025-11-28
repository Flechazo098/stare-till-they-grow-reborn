package com.sighs.staretilltheygrow.fabric;

import com.sighs.staretilltheygrow.actions.block.ApplyBoneMealAction;
import com.sighs.staretilltheygrow.actions.block.RegrowCakeAction;
import com.sighs.staretilltheygrow.actions.entity.FallInLoveAction;
import com.sighs.staretilltheygrow.actions.entity.GrowUpAction;
import com.sighs.staretilltheygrow.actions.entity.RegrowWoolAction;
import com.sighs.staretilltheygrow.actions.entity.TraderRestockAction;
import com.sighs.staretilltheygrow.config.Config;
import com.sighs.staretilltheygrow.dictionaries.PlayerTargetDictionary;
import com.sighs.staretilltheygrow.eventhooks.TraderOpenHooks;
import com.sighs.staretilltheygrow.fabric.network.NetworkFabric;
import com.sighs.staretilltheygrow.fabric.config.ConfigFabricService;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.AbstractVillager;

public class StareTillTheyGrowFabric implements ModInitializer {
    public static final String MOD_ID = "staretilltheygrow";

    @Override
    public void onInitialize() {
        Config.init(ConfigFabricService.INSTANCE);
        ConfigFabricService.register();
        NetworkFabric.initialize();
        ServerTickEvents.END_SERVER_TICK.register(server -> {
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
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> PlayerTargetDictionary.unregister(handler.getPlayer()));
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof AbstractVillager trader) TraderOpenHooks.onInteract(trader);
            return InteractionResult.PASS;
        });
    }
}