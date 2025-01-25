package com.flechazo.StareTillTheyGrowRb.Dictionaries;

import com.flechazo.StareTillTheyGrowRb.Config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;

import java.util.Hashtable;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * 玩家目标字典，用于管理玩家与其目标（方块或实体）之间的映射。
 *
 * @author Flechazo
 * @since 1.20.1
 * @version 3.0.0
 */
public class PlayerTargetDictionary {
    /**
     * 存储玩家与他们的目标信息的字典。
     */
    private static final Hashtable<Player, PlayerTarget> DICTIONARY = new Hashtable<>();

    /**
     * 注册一个方块为目标。
     *
     * @param player 玩家对象
     * @param dimension 游戏维度
     * @param position 方块位置
     */
    public static void registerBlock(Player player, ServerLevel dimension, BlockPos position) {
        DICTIONARY.put(player, new PlayerBlockTarget(player, dimension, position));
    }

    /**
     * 注册一个实体为目标。
     *
     * @param player 玩家对象
     * @param dimension 游戏维度
     * @param entityUuid 实体的UUID
     */
    public static void registerEntity(Player player, ServerLevel dimension, UUID entityUuid) {
        Entity entity = dimension.getEntity(entityUuid);
        DICTIONARY.put(player, new PlayerEntityTarget(entityUuid, player, dimension, entity));
    }

    /**
     * 取消注册指定玩家的目标。
     *
     * @param player 玩家对象
     */
    public static void unregister(Player player) {
        DICTIONARY.remove(player);
    }

    /**
     * 对字典中的每个条目执行给定的操作。
     *
     * @param consumer 操作函数
     */
    public static void forEach(BiConsumer<Player, PlayerTarget> consumer) {
        DICTIONARY.forEach(consumer);
    }

    /**
     * 抽象玩家目标类，包含玩家及其目标的基本信息。
     */
    public static abstract class PlayerTarget {
        protected final Player player;
        protected final ServerLevel dimension;
        protected int internalTick = 0;

        /**
         * 构造函数，初始化玩家目标。
         *
         * @param player 玩家对象
         * @param dimension 游戏维度
         */
        public PlayerTarget(Player player, ServerLevel dimension) {
            this.player = player;
            this.dimension = dimension;
        }

        /**
         * 获取玩家对象。
         *
         * @return 玩家对象
         */
        public Player getPlayer() {
            return this.player;
        }

        /**
         * 获取游戏维度。
         *
         * @return 游戏维度
         */
        public ServerLevel getDimension() {
            return this.dimension;
        }

        /**
         * 更新内部计数器。
         */
        public void tick() {
            internalTick++;

            // 如果跟踪的tick过多，则重置
            if (internalTick > 500000) {
                internalTick = Config.COMMON.ticksDelay.get();
            }
        }

        /**
         * 判断是否可以调用动作。
         *
         * @return 如果满足条件则返回true
         */
        public boolean canInvoke() {
            return internalTick > Config.COMMON.ticksDelay.get() && internalTick % Config.COMMON.ticksBetween.get() == 0;
        }
    }

    /**
     * 玩家方块目标类。
     */
    public static class PlayerBlockTarget extends PlayerTarget {
        private final BlockPos target;

        /**
         * 构造函数，初始化玩家方块目标。
         *
         * @param player 玩家对象
         * @param dimension 游戏维度
         * @param target 目标方块的位置
         */
        public PlayerBlockTarget(Player player, ServerLevel dimension, BlockPos target) {
            super(player, dimension);
            this.target = target;
        }

        /**
         * 获取目标方块的位置。
         *
         * @return 目标方块的位置
         */
        public BlockPos getTarget() {
            return target;
        }
    }

    /**
     * 玩家实体目标类。
     */
    public static class PlayerEntityTarget extends PlayerTarget {
        private final Entity target;
        private final UUID targetUUID;

        /**
         * 构造函数，初始化玩家实体目标。
         *
         * @param player 玩家对象
         * @param dimension 游戏维度
         * @param target 目标实体
         */
        public PlayerEntityTarget(UUID targetUUID, Player player, ServerLevel dimension, Entity target) {
            super(player, dimension);
            this.target = target;
            this.targetUUID = targetUUID;
        }

        /**
         * 获取目标实体。
         *
         * @return 目标实体
         */
        public Entity getTarget() {
            return target;
        }

        /**
         * 根据目标实体的UUID从当前世界中获取对应的AbstractVillager实体。
         *
         * @return Optional 包含找到的 AbstractVillager 实例，如果未找到则为空。
         */
        public Optional<AbstractVillager> getEntity() {
            Entity entity = getDimension().getEntity(targetUUID);
            if (entity instanceof AbstractVillager abstractVillager) {
                return Optional.of(abstractVillager);
            }
            return Optional.empty();
        }

    }
}