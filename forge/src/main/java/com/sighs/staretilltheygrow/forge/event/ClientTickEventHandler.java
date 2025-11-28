package com.sighs.staretilltheygrow.forge.event;

import com.sighs.staretilltheygrow.network.messages.RegisterBlockPayload;
import com.sighs.staretilltheygrow.network.messages.RegisterEntityPayload;
import com.sighs.staretilltheygrow.network.messages.UnregisterPayload;
import com.sighs.staretilltheygrow.forge.network.NetworkForge;
import com.sighs.staretilltheygrow.forge.StareTillTheyGrowForge;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = StareTillTheyGrowForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientTickEventHandler {
    private static @Nullable BlockPos currentBlockPos = null;
    private static @Nullable UUID currentEntityUuid = null;

    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (mc.hitResult instanceof BlockHitResult blockHitResult) {
            BlockPos newPos = blockHitResult.getBlockPos();
            if (currentBlockPos == null || !currentBlockPos.equals(newPos)) {
                currentBlockPos = newPos;
                currentEntityUuid = null;
                NetworkForge.sendToServer(new RegisterBlockPayload(newPos));
            }
        } else if (mc.hitResult instanceof EntityHitResult entityHitResult) {
            UUID newUuid = entityHitResult.getEntity().getUUID();
            if (!newUuid.equals(currentEntityUuid)) {
                currentEntityUuid = newUuid;
                currentBlockPos = null;
                NetworkForge.sendToServer(new RegisterEntityPayload(newUuid));
            }
        } else {
            currentBlockPos = null;
            currentEntityUuid = null;
            NetworkForge.sendToServer(new UnregisterPayload());
        }
    }
}