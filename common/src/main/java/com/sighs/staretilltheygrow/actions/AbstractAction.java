package com.sighs.staretilltheygrow.actions;

import com.sighs.staretilltheygrow.dictionaries.PlayerTargetDictionary;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractAction implements ActionInterface {

    protected ServerLevel dimension;

    protected Player player;

    protected boolean isEnabledInConfig;

    public AbstractAction(PlayerTargetDictionary.PlayerTarget playerTarget) {
        dimension = playerTarget.getDimension();
        player = playerTarget.getPlayer();
        isEnabledInConfig = getIsEnabledInConfig();
    }

    protected abstract boolean getIsEnabledInConfig();

    protected void emitParticles(BlockPos pos) {
        emitParticles(pos.getX(), pos.getY(), pos.getZ());
    }

    protected void emitParticles(Vec3 pos) {
        emitParticles(pos.x(), pos.y(), pos.z());
    }

    protected void emitParticles(double x, double y, double z) {
        dimension.sendParticles(
                ParticleTypes.HAPPY_VILLAGER,
                x + 0.5,
                y + 0.5,
                z + 0.5,
                75,
                1,
                1,
                1,
                1);
    }
}