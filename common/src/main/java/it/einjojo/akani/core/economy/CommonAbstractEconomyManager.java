package it.einjojo.akani.core.economy;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.einjojo.akani.core.api.economy.EconomyHolder;
import it.einjojo.akani.core.api.economy.EconomyManager;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.messaging.ChannelMessage;
import it.einjojo.akani.core.api.messaging.ChannelReceiver;
import it.einjojo.akani.core.api.messaging.MessageProcessor;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public abstract class CommonAbstractEconomyManager implements EconomyManager, MessageProcessor, EconomyObserver {
    protected final BrokerService brokerService;
    protected final CommonEconomyStorage storage;
    protected final AsyncLoadingCache<UUID, EconomyHolder> cached;

    public CommonAbstractEconomyManager(BrokerService brokerService, CommonEconomyStorage storage) {
        this.brokerService = brokerService;
        this.storage = storage;
        cached = Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .buildAsync(((uuid, executor) -> CompletableFuture.supplyAsync(() -> {
                    var res = (CommonEconomyHolder) storage.loadEconomy(uuid);
                    if (res != null) {
                        res.setObserver(this);
                    }
                    return res;
                }, executor)));
        brokerService.registerMessageProcessor(this);

    }

    abstract String invalidateMessageId();

    abstract String updateMessageId();

    @Override
    public void processMessage(ChannelMessage message) {
        if (message.messageTypeID().equals(invalidateMessageId())) {
            UUID uuid = UUID.fromString(message.content());
            invalidateLocalCachedEconomy(uuid);
        } else if (message.messageTypeID().equals(updateMessageId())) {
            var payload = message.content().split(";");
            UUID uuid = UUID.fromString(payload[0]);
            long balance = Long.parseLong(payload[1]);
            ((CommonEconomyHolder) cached.synchronous().get(uuid)).setBalanceWithoutNotify(balance);
        }
    }

    @Override
    public Optional<EconomyHolder> cachedPlayerEconomy(UUID uuid) {
        return Optional.ofNullable(cached.synchronous().getIfPresent(uuid));
    }

    @Override
    public CompletableFuture<Optional<EconomyHolder>> playerEconomyAsync(UUID uuid) {
        return cached.get(uuid).thenApply(Optional::ofNullable);
    }

    @Override
    public Optional<EconomyHolder> playerEconomy(UUID uuid) {
        return Optional.ofNullable(cached.synchronous().get(uuid));
    }

    @Override
    public void updateEconomy(EconomyHolder economyHolder) {
        storage.updateEconomy(economyHolder);
        invalidateEconomy(economyHolder.ownerUuid());
    }

    @Override
    public void invalidateLocalCachedEconomy(UUID uuid) {
        cached.synchronous().invalidate(uuid);
    }

    public void invalidateEconomy(UUID player) {
        invalidateLocalCachedEconomy(player);
        brokerService.publish(ChannelMessage.builder()
                .messageTypeID(invalidateMessageId())
                .recipient(ChannelReceiver.all())
                .content(player.toString())
                .channel(processingChannel())
                .build());
    }

    @Override
    public void onBalanceChange(EconomyHolder holder, long oldBalance, long newBalance) {
        brokerService.publish(ChannelMessage.builder()
                .messageTypeID(updateMessageId())
                .recipient(ChannelReceiver.all())
                .content(holder.ownerUuid().toString() + ";" + newBalance)
                .channel(processingChannel())
                .build());
    }

    @Override
    public CompletableFuture<Void> updateEconomyAsync(EconomyHolder economyHolder) {
        return CompletableFuture.runAsync(() -> updateEconomy(economyHolder));
    }
}
