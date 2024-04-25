package it.einjojo.akani.core.service;

import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.api.service.BackService;
import redis.clients.jedis.params.SetParams;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonBackService implements BackService {
    private static final int BACK_EXPIRE_SECONDS = 60 * 5;
    private static final String BACK_PREFIX = "akani:back:";
    private final InternalAkaniCore core;

    public CommonBackService(InternalAkaniCore core) {
        this.core = core;
    }

    public CompletableFuture<Optional<NetworkLocation>> loadBackLocation(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (var jedis = core.jedisPool().getResource()) {
                var location = jedis.get(BACK_PREFIX + uuid.toString());
                if (location == null) {
                    return Optional.empty();
                }
                return Optional.of(core.gson().fromJson(location, NetworkLocation.class));
            }
        });
    }

    public void saveBackLocation(UUID uuid, NetworkLocation location) {
        try (var jedis = core.jedisPool().getResource()) {
            jedis.set(BACK_PREFIX + uuid.toString(), core.gson().toJson(location), SetParams.setParams().ex(BACK_EXPIRE_SECONDS));
        }
    }

    @Override
    public void deleteBackLocation(UUID uuid) {
        try (var jedis = core.jedisPool().getResource()) {
            jedis.del(BACK_PREFIX + uuid.toString());
        }
    }

    @Override
    public CompletableFuture<Boolean> teleportBackAsync(AkaniPlayer player) {
        return loadBackLocation(player.uuid()).thenApply(optionalLocation -> {
            if (optionalLocation.isPresent()) {
                player.teleport(optionalLocation.get());
                return true;
            }
            return false;
        });
    }
}
