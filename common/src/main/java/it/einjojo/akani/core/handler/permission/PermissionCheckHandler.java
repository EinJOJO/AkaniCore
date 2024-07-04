package it.einjojo.akani.core.handler.permission;

import java.util.UUID;

public interface PermissionCheckHandler {

    boolean hasPermission(UUID playerUuid, String permission);

}
