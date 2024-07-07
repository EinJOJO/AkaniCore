package it.einjojo.akani.core.tags;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.api.tags.TagHolder;
import it.einjojo.akani.core.api.tags.TagManager;
import it.einjojo.akani.core.api.tags.TagStorage;
import it.einjojo.akani.core.util.LuckPermsHook;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CommonTagManager implements TagManager, CommonTagHolderObserver {
    private static final Logger log = LoggerFactory.getLogger(CommonTagManager.class);
    private final LoadingCache<UUID, TagHolder> tagHolderLoadingCache;
    private final Set<TagHolder> dirtyTagHolders = new HashSet<>();
    private final TagStorage tagStorage;
    private final LuckPermsHook luckPermsHook;

    public CommonTagManager(TagStorage tagStorage, LuckPermsHook luckPermsHook) {
        this.tagStorage = tagStorage;
        this.tagHolderLoadingCache = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(2))
                .build((uuid) -> {
                    TagHolder tagHolder = tagStorage.loadTagHolder(uuid);
                    if (tagHolder instanceof CommonTagHolder commonTagHolder) {
                        commonTagHolder.setObserver(this);
                    } else {
                        log.warn("Loaded tag holder for {} is not an instance of CommonTagHolder", uuid);
                    }
                    return tagHolder;
                });
        this.luckPermsHook = luckPermsHook;
    }


    @Override
    public @NotNull TagHolder tagHolder(UUID uuid) {
        return tagHolderLoadingCache.get(uuid);
    }

    @Override
    @Blocking
    public void updateTagHolder(UUID uuid, TagHolder tagHolder) {
        tagHolderLoadingCache.put(uuid, tagHolder);
        tagStorage.saveTagHolder(tagHolder);
        dirtyTagHolders.remove(tagHolder);
        log.debug("Updated tag holder for {}", uuid);
    }

    @Override
    public void onSelectTag(TagHolder tagHolder, Tag tag) {
        dirtyTagHolders.add(tagHolder);
        log.debug("Tag holder {} has selected tag {}", tagHolder.uuid(), tag.id());
    }

    public Set<TagHolder> dirtyTagHolders() {
        return dirtyTagHolders;
    }

    @Override
    public void onAddTag(TagHolder tagHolder, Tag added) {
        this.luckPermsHook.addPermission(tagHolder.uuid(), added.permission());
        log.debug("Permission {} granted to {} ", added.permission(), tagHolder.uuid());
    }
}
