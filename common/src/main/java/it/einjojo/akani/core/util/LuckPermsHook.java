package it.einjojo.akani.core.util;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeBuilder;
import net.luckperms.api.node.types.PermissionNode;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record LuckPermsHook(LuckPerms luckPerms) {

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

    public void addPermission(User user, String permission) {
        user.getNodes().add(PermissionNode.builder(permission).build());
    }

    public CompletableFuture<Void> addPermission(UUID userId, String permission) {
        return luckPerms.getUserManager().loadUser(userId).thenAccept((u) -> addPermission(u, permission));
    }
}
