package com.flechazo.StareTillTheyGrowRb.Actions.Entity;

import com.flechazo.StareTillTheyGrowRb.Config.Config;
import com.flechazo.StareTillTheyGrowRb.Dictionaries.PlayerTargetDictionary;
import com.flechazo.StareTillTheyGrowRb.EventHandlers.TraderOpenHandler;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 商人补货动作类
 * 用于处理商人的补货逻辑和冷却时间
 *
 * @author Flechazo
 * @since 3.0.0
 */
public class TraderRestockAction extends AbstractEntityAction {
    private static final Logger logger = LoggerFactory.getLogger(TraderRestockAction.class);
    private static final Map<UUID, Long> lastRestockTimes = new ConcurrentHashMap<>();
    private static final long COOLDOWN_TIME = 10 * 1000; // 10秒冷却时间
    private final AbstractVillager trader;

    /**
     * 构造函数，初始化让商人补货的操作。
     *
     * @param playerEntityTarget 玩家的目标实体信息
     * @param trader 要补货的商人
     */
    public TraderRestockAction(PlayerTargetDictionary.PlayerEntityTarget playerEntityTarget, AbstractVillager trader) {
        super(playerEntityTarget);
        this.trader = trader;
    }

    @Override
    public void invoke() {
        if (trader == null) {
            logger.error("Trader is null, cannot restock");
            return;
        }

        UUID traderId = trader.getUUID();

        // 检查是否有交易被买断
        boolean anyTradeOutOfStock = checkAnyTradeOutOfStock(trader);
        if (!anyTradeOutOfStock) {
            logger.debug("Trader {} has no out-of-stock trades, not restocking", traderId);
            return;
        }

        // 检查冷却时间
        if (!canTraderRestock(trader)) {
            logger.debug("Trader {} is in cooldown, cannot restock", traderId);
            return;
        }

        try {
            // 执行补货
            MerchantOffers offers = trader.getOffers();
            boolean restocked = false;
            for (MerchantOffer offer : offers) {
                if (offer.isOutOfStock()) {
                    offer.resetUses();
                    restocked = true;
                }
            }

            if (restocked) {
                // 更新补货时间
                updateLastRestockTime(trader);

                // 重置商人状态
                TraderOpenHandler.resetTraderState(trader);

                // 发送粒子效果
                emitParticles(trader.position());

                logger.info("Successfully restocked out-of-stock trades for trader {}", traderId);
            }
        } catch (Exception e) {
            logger.error("Error while restocking trader {}: {}", traderId, e.getMessage());
        }
    }

    /**
     * 检查商人的交易中是否有至少一个商品已被买断
     *
     * @param trader 要检查的商人
     * @return 如果有一个或多个商品的交易已买断返回 true，否则返回 false
     */
    private boolean checkAnyTradeOutOfStock(AbstractVillager trader) {
        MerchantOffers offers = trader.getOffers();
        if (offers.isEmpty()) {
            return false;
        }

        for (MerchantOffer offer : offers) {
            if (offer.isOutOfStock()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查商人是否可以补货
     *
     * @param trader 要检查的商人
     * @return 是否可以补货
     */
    public static boolean canTraderRestock(AbstractVillager trader) {
        if (trader == null) {
            return false;
        }

        UUID traderId = trader.getUUID();
        Long lastRestockTime = lastRestockTimes.get(traderId);

        if (lastRestockTime == null) {
            return true;
        }

        long currentTime = System.currentTimeMillis();
        boolean canRestock = (currentTime - lastRestockTime) >= COOLDOWN_TIME;

        logger.debug("Trader {} cooldown check: lastRestock={}, current={}, canRestock={}, cooldownTime={}",
            traderId, lastRestockTime, currentTime, canRestock, COOLDOWN_TIME);

        return canRestock;
    }

    /**
     * 更新商人最后补货时间
     *
     * @param trader 要更新的商人
     */
    private void updateLastRestockTime(AbstractVillager trader) {
        if (trader != null) {
            lastRestockTimes.put(trader.getUUID(), System.currentTimeMillis());
            logger.debug("Updated last restock time for trader {}", trader.getUUID());
        }
    }

    @Override
    protected boolean getIsEnabledInConfig() {
        return Config.COMMON.enableTraderRestock.get();
    }
}