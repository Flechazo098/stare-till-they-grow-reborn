package com.sighs.staretilltheygrow.fabric.client;

import com.sighs.staretilltheygrow.fabric.network.NetworkFabric;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.UUID;

public final class StareTillTheyGrowFabricClient implements ClientModInitializer {
    private static BlockPos currentBlockPos = null;
    private static UUID currentEntityUuid = null;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;
            if (mc.hitResult instanceof BlockHitResult bhr) {
                BlockPos pos = bhr.getBlockPos();
                if (currentBlockPos == null || !currentBlockPos.equals(pos)) {
                    currentBlockPos = pos;
                    currentEntityUuid = null;
                    NetworkFabric.sendRegisterBlock(pos);
                }
            } else if (mc.hitResult instanceof EntityHitResult ehr) {
                UUID id = ehr.getEntity().getUUID();
                if (!id.equals(currentEntityUuid)) {
                    currentEntityUuid = id;
                    currentBlockPos = null;
                    NetworkFabric.sendRegisterEntity(id);
                }
            } else {
                currentBlockPos = null;
                currentEntityUuid = null;
                NetworkFabric.sendUnregister();
            }
        });
    }
}