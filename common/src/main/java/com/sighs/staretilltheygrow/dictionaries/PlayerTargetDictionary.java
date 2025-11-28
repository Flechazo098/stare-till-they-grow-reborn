package com.sighs.staretilltheygrow.dictionaries;

import com.sighs.staretilltheygrow.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class PlayerTargetDictionary {
    private static final Map<Player, PlayerTarget> DICTIONARY = new ConcurrentHashMap<>();

    public static void registerBlock(Player player, ServerLevel dimension, BlockPos position) {
        DICTIONARY.put(player, new PlayerBlockTarget(player, dimension, position));
    }

    public static void registerEntity(Player player, ServerLevel dimension, UUID entityUuid) {
        Entity entity = dimension.getEntity(entityUuid);
        DICTIONARY.put(player, new PlayerEntityTarget(entityUuid, player, dimension, entity));
    }

    public static void unregister(Player player) {
        DICTIONARY.remove(player);
    }

    public static void forEach(BiConsumer<Player, PlayerTarget> consumer) {
        DICTIONARY.forEach(consumer);
    }

    public static abstract class PlayerTarget {
        protected final Player player;
        protected final ServerLevel dimension;
        protected int internalTick;

        public PlayerTarget(Player player, ServerLevel dimension) {
            this.player = player;
            this.dimension = dimension;
            this.internalTick = 0;
        }

        public Player getPlayer() {
            return this.player;
        }

        public ServerLevel getDimension() {
            return this.dimension;
        }

        public void tick() {
            internalTick++;
            if (internalTick > 500000) {
                internalTick = Config.get().ticksDelay();
            }
        }

        public boolean canInvoke() {
            return internalTick > Config.get().ticksDelay() && internalTick % Config.get().ticksBetween() == 0;
        }
    }

    public static class PlayerBlockTarget extends PlayerTarget {
        private final BlockPos target;

        public PlayerBlockTarget(Player player, ServerLevel dimension, BlockPos target) {
            super(player, dimension);
            this.target = target;
        }

        public BlockPos getTarget() {
            return target;
        }
    }

    public static class PlayerEntityTarget extends PlayerTarget {
        private final Entity target;
        private final UUID targetUUID;

        public PlayerEntityTarget(UUID targetUUID, Player player, ServerLevel dimension, Entity target) {
            super(player, dimension);
            this.target = target;
            this.targetUUID = targetUUID;
        }

        public Entity getTarget() {
            return target;
        }

        public Optional<AbstractVillager> getEntity() {
            Entity entity = getDimension().getEntity(targetUUID);
            if (entity instanceof AbstractVillager abstractVillager) {
                return Optional.of(abstractVillager);
            }
            return Optional.empty();
        }
    }
}