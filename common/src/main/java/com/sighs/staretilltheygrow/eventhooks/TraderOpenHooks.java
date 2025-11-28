package com.sighs.staretilltheygrow.eventhooks;

import com.sighs.staretilltheygrow.actions.entity.TraderRestockAction;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TraderOpenHooks {
    private static final Map<UUID, Map<Integer, TradeState>> traderStates = new ConcurrentHashMap<>();

    private static class TradeState {
        int maxUses;
        boolean outOfStock;
        long lockedTime;

        TradeState(int maxUses, boolean outOfStock) {
            this.maxUses = maxUses;
            this.outOfStock = outOfStock;
            this.lockedTime = System.currentTimeMillis();
        }
    }

    public static void onOpen(AbstractVillager trader) {
        UUID id = trader.getUUID();
        Map<Integer, TradeState> trades = traderStates.computeIfAbsent(id, k -> new ConcurrentHashMap<>());
        MerchantOffer[] offers = trader.getOffers().toArray(new MerchantOffer[0]);
        for (int i = 0; i < offers.length; i++) {
            MerchantOffer offer = offers[i];
            TradeState state = trades.computeIfAbsent(i, k -> new TradeState(offer.getMaxUses(), offer.isOutOfStock()));
            if (state.outOfStock && !TraderRestockAction.canTraderRestock(trader)) {
                while (offer.getUses() < offer.getMaxUses()) {
                    offer.increaseUses();
                }
            }
            state.maxUses = offer.getMaxUses();
            state.outOfStock = offer.isOutOfStock();
            if (state.outOfStock) {
                state.lockedTime = System.currentTimeMillis();
            }
        }
    }

    public static void onInteract(AbstractVillager trader) {
        Map<Integer, TradeState> trades = traderStates.get(trader.getUUID());
        if (trades == null) return;
        MerchantOffer[] offers = trader.getOffers().toArray(new MerchantOffer[0]);
        for (int i = 0; i < offers.length; i++) {
            TradeState state = trades.get(i);
            if (state != null && state.outOfStock && !TraderRestockAction.canTraderRestock(trader)) {
                MerchantOffer offer = offers[i];
                while (offer.getUses() < offer.getMaxUses()) {
                    offer.increaseUses();
                }
            }
        }
    }

    public static void reset(AbstractVillager trader) {
        Map<Integer, TradeState> trades = traderStates.remove(trader.getUUID());
        if (trades != null) {
            for (MerchantOffer offer : trader.getOffers()) {
                offer.resetUses();
            }
        }
    }
}