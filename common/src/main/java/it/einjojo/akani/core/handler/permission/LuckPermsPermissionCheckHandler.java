package it.einjojo.akani.core.handler.permission;

import net.luckperms.api.LuckPerms;

import java.util.UUID;

public class LuckPermsPermissionCheckHandler implements PermissionCheckHandler {
    private final LuckPerms luckPerms;

    public LuckPermsPermissionCheckHandler(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    @Override
    public boolean hasPermission(UUID playerUuid, String permission) {
        var user = luckPerms.getUserManager().getUser(playerUuid);
        if (user == null) return false;
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }
}
