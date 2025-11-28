package com.sighs.staretilltheygrow.network.messages;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class RegisterBlockPayload {
    private final double x, y, z;

    public RegisterBlockPayload(double x, double y, double z) {
        this.x = x; this.y = y; this.z = z;
    }

    public RegisterBlockPayload(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public BlockPos toBlockPos() {
        return new BlockPos((int)x, (int)y, (int)z);
    }

    public static void encode(RegisterBlockPayload m, FriendlyByteBuf buf) {
        buf.writeDouble(m.x);
        buf.writeDouble(m.y);
        buf.writeDouble(m.z);
    }

    public static RegisterBlockPayload decode(FriendlyByteBuf buf) {
        return new RegisterBlockPayload(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }
}