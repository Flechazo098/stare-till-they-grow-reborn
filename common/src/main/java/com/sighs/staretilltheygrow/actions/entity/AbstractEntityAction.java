package com.sighs.staretilltheygrow.actions.entity;

import com.sighs.staretilltheygrow.actions.AbstractAction;
import com.sighs.staretilltheygrow.dictionaries.PlayerTargetDictionary;
import net.minecraft.world.entity.Entity;

abstract public class AbstractEntityAction extends AbstractAction {

    protected Entity entity;

    public AbstractEntityAction(PlayerTargetDictionary.PlayerEntityTarget playerEntityTarget) {
        super(playerEntityTarget);
        entity = playerEntityTarget.getTarget();

    }
}