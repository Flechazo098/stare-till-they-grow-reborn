package com.sighs.staretilltheygrow.network.messages;

import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public record RegisterEntityPayload(UUID uuid) {

    public static void encode(RegisterEntityPayload m, FriendlyByteBuf buf) {
        buf.writeUUID(m.uuid);
    }

    public static RegisterEntityPayload decode(FriendlyByteBuf buf) {
        return new RegisterEntityPayload(buf.readUUID());
    }
}