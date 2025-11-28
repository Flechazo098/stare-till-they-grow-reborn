package com.sighs.staretilltheygrow.actions.entity;

import com.sighs.staretilltheygrow.config.Config;
import com.sighs.staretilltheygrow.dictionaries.PlayerTargetDictionary;
import com.sighs.staretilltheygrow.eventhooks.TraderOpenHooks;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TraderRestockAction extends AbstractEntityAction {
    private static final Logger logger = LoggerFactory.getLogger(TraderRestockAction.class);
    private static final Map<UUID, Long> lastRestockTimes = new ConcurrentHashMap<>();
    private static final long COOLDOWN_TIME = 60 * 1000;
    private final AbstractVillager trader;
    public TraderRestockAction(PlayerTargetDictionary.PlayerEntityTarget playerEntityTarget, @NotNull AbstractVillager trader, Player player) {
        super(playerEntityTarget);
        this.trader = trader;
        this.player = player;
    }

    @Override
    public void invoke() {
        if (trader == null) {
            logger.error("Trader is null, cannot restock");
            return;
        }

        UUID traderId = trader.getUUID();

        if (!canTraderRestock(trader)) {
            return;
        }

        try {
            MerchantOffers offers = trader.getOffers();
            boolean restocked = false;

            for (MerchantOffer offer : offers) {
                if (offer.isOutOfStock()) {
                    offer.resetUses();
                    restocked = true;
                }
            }

            if (restocked) {
                updateLastRestockTime(trader);

                TraderOpenHooks.reset(trader);

                emitParticles(trader.position());

            }
        } catch (Exception e) {
            logger.error("Error while restocking trader {}: {}", traderId, e.getMessage());
        }
    }

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

        return (currentTime - lastRestockTime) >= COOLDOWN_TIME;
    }

    private void updateLastRestockTime(AbstractVillager trader) {
        if (trader != null) {
            lastRestockTimes.put(trader.getUUID(), System.currentTimeMillis());
            logger.debug("Updated last restock time for trader {}", trader.getUUID());
        }
    }

    @Override
    protected boolean getIsEnabledInConfig() {
        return Config.get().enableTraderRestock();
    }
}