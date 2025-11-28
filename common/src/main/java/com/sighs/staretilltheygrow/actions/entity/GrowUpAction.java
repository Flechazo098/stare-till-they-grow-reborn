package com.sighs.staretilltheygrow.actions.entity;

import com.sighs.staretilltheygrow.config.Config;
import com.sighs.staretilltheygrow.dictionaries.PlayerTargetDictionary;
import net.minecraft.world.entity.animal.Animal;

public class GrowUpAction extends AbstractEntityAction {

    public GrowUpAction(PlayerTargetDictionary.PlayerEntityTarget playerEntityTarget) {
        super(playerEntityTarget);
    }

    @Override
    public void invoke() {
        if (
                isEnabledInConfig
                        && entity instanceof Animal animal
                        && animal.isBaby()
        ) {
            animal.ageUp(60 * 5);
            emitParticles(entity.position());
        }
    }

    @Override
    protected boolean getIsEnabledInConfig() {
        return Config.get().enableGrowUp();
    }
}