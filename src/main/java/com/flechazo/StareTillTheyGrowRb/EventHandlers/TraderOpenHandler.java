package com.flechazo.StareTillTheyGrowRb.EventHandlers;

import com.flechazo.StareTillTheyGrowRb.Actions.Entity.TraderRestockAction;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理商人交易界面打开事件
 * 用于防止商人在冷却时间内自动补货
 *
 * @author Flechazo
 * @since 3.0.0
 */
public class TraderOpenHandler {
    private static final Logger logger = LoggerFactory.getLogger(TraderOpenHandler.class);
    private static final Map<UUID, Map<Integer, TradeState>> traderStates = new ConcurrentHashMap<>();

    /**
     * 交易状态类，记录每个交易的状态
     */
    private static class TradeState {
        int maxUses;
        boolean isOutOfStock;
        long lockedTime; // 记录交易被锁定的时间

        TradeState(int maxUses, boolean isOutOfStock) {
            this.maxUses = maxUses;
            this.isOutOfStock = isOutOfStock;
            this.lockedTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTradeOpen(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getTarget() instanceof AbstractVillager trader) {
            UUID traderId = trader.getUUID();
            logger.debug("Checking trades for trader: {}", traderId);

            // 获取或创建商人的交易状态记录
            Map<Integer, TradeState> trades = traderStates.computeIfAbsent(traderId, k -> new ConcurrentHashMap<>());

            // 检查每个交易
            MerchantOffer[] offers = trader.getOffers().toArray(new MerchantOffer[0]);
            for (int i = 0; i < offers.length; i++) {
                MerchantOffer offer = offers[i];

                // 获取或创建交易状态
                TradeState state = trades.computeIfAbsent(i, k -> new TradeState(offer.getMaxUses(), offer.isOutOfStock()));

                // 如果交易之前是买断的，且在冷却时间内，保持买断状态
                if (state.isOutOfStock && !TraderRestockAction.canTraderRestock(trader)) {
                    logger.debug("Locking out-of-stock trade at index {} for trader {}", i, traderId);
                    while (offer.getUses() < offer.getMaxUses()) {
                        offer.increaseUses();
                    }
                }

                // 更新交易状态
                state.maxUses = offer.getMaxUses();
                state.isOutOfStock = offer.isOutOfStock();
                if (state.isOutOfStock) {
                    state.lockedTime = System.currentTimeMillis();
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof AbstractVillager trader) {
            UUID traderId = trader.getUUID();
            Map<Integer, TradeState> trades = traderStates.get(traderId);

            if (trades != null) {
                MerchantOffer[] offers = trader.getOffers().toArray(new MerchantOffer[0]);
                for (int i = 0; i < offers.length; i++) {
                    TradeState state = trades.get(i);
                    // 只在冷却时间内锁定交易
                    if (state != null && state.isOutOfStock && !TraderRestockAction.canTraderRestock(trader)) {
                        MerchantOffer offer = offers[i];
                        while (offer.getUses() < offer.getMaxUses()) {
                            offer.increaseUses();
                        }
                    }
                }
            }
        }
    }

    /**
     * 重置商人的交易状态
     *
     * @param trader 要重置的商人
     */
    public static void resetTraderState(AbstractVillager trader) {
        if (trader != null) {
            Map<Integer, TradeState> trades = traderStates.remove(trader.getUUID());
            if (trades != null) {
                logger.debug("Resetting trade states for trader: {}", trader.getUUID());
                // 重置所有交易
                for (MerchantOffer offer : trader.getOffers()) {
                    offer.resetUses();
                }
            }
        }
    }
}