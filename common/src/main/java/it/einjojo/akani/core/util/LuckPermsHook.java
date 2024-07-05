package it.einjojo.akani.core.util;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LuckPermsHook {

    private final LuckPerms luckPerms;


    public LuckPermsHook(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    public CompletableFuture<Boolean> hasPlayerPermission(UUID playerUuid, String permission) {
        User user = luckPerms.getUserManager().getUser(playerUuid);
        if (user != null) {
            return CompletableFuture.completedFuture(checkPermission(user, permission));
        } else {
            return luckPerms.getUserManager().loadUser(playerUuid).thenApply(u -> checkPermission(u, permission));
        }
    }

    public boolean checkPermission(User lpUser, String permission) {
        return lpUser.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }



    public CompletableFuture<String> prefix(UUID playerUuid) {
        User user = luckPerms.getUserManager().getUser(playerUuid);
        if (user != null) {
            return CompletableFuture.completedFuture(user.getCachedData().getMetaData().getPrefix());
        } else {
            return luckPerms.getUserManager().loadUser(playerUuid).thenApply(u -> u.getCachedData().getMetaData().getPrefix());
        }
    }

    public CompletableFuture<String> suffix(UUID playerUuid) {
        User user = luckPerms.getUserManager().getUser(playerUuid);
        if (user != null) {
            return CompletableFuture.completedFuture(user.getCachedData().getMetaData().getSuffix());
        } else {
            return luckPerms.getUserManager().loadUser(playerUuid).thenApply(u -> u.getCachedData().getMetaData().getSuffix());
        }
    }


    public LuckPerms luckPerms() {
        return luckPerms;
    }
}
