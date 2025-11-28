package com.sighs.staretilltheygrow.network.messages;

import net.minecraft.network.FriendlyByteBuf;

public class UnregisterPayload {
    public static void encode(UnregisterPayload m, FriendlyByteBuf buf) {}
    public static UnregisterPayload decode(FriendlyByteBuf buf) { return new UnregisterPayload(); }
}