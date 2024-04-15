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

public abstract class CommonAbstractEconomyManager implements EconomyManager, MessageProcessor {
    protected final BrokerService brokerService;
    protected final EconomyStorage storage;
    protected final AsyncLoadingCache<UUID, EconomyHolder> cached;

    public CommonAbstractEconomyManager(BrokerService brokerService, EconomyStorage storage) {
        this.brokerService = brokerService;
        this.storage = storage;
        cached = Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.MINUTES)
                .buildAsync(((uuid, executor) -> CompletableFuture.supplyAsync(() -> storage.loadEconomy(uuid), executor)));
        brokerService.registerMessageProcessor(this);

    }

    abstract String invalidateMessageId();

    @Override
    public void processMessage(ChannelMessage message) {
        if (message.messageTypeID().equals(invalidateMessageId())) {
            UUID uuid = UUID.fromString(message.content());
            invalidateLocalCachedEconomy(uuid);
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
    public CompletableFuture<Void> updateEconomyAsync(EconomyHolder economyHolder) {
        return CompletableFuture.runAsync(() -> updateEconomy(economyHolder));
    }
}
