package com.sighs.staretilltheygrow.actions.entity;

import com.sighs.staretilltheygrow.config.Config;
import com.sighs.staretilltheygrow.dictionaries.PlayerTargetDictionary;
import net.minecraft.world.entity.animal.Sheep;

public class RegrowWoolAction extends AbstractEntityAction {

    public RegrowWoolAction(PlayerTargetDictionary.PlayerEntityTarget playerEntityTarget) {
        super(playerEntityTarget);
    }


    @Override
    public void invoke() {
        if (
                isEnabledInConfig
                        && entity instanceof Sheep sheep
                        && sheep.isSheared()
        ) {
            sheep.setSheared(false);
            emitParticles(entity.position());
        }
    }

    @Override
    protected boolean getIsEnabledInConfig() {
        return Config.get().enableRegrowWool();
    }
}