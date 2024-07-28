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
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;

public class CommonTagManager implements TagManager, CommonTagHolderObserver {
    private static final Logger log = LoggerFactory.getLogger(CommonTagManager.class);
    private final LoadingCache<UUID, TagHolder> tagHolderLoadingCache;
    private final Set<TagHolder> dirtyTagHolders = new HashSet<>();
    private final TagStorage tagStorage;
    private @Nullable LuckPermsHook luckPermsHook;
    private List<Tag> loadedTags;

    public CommonTagManager(@NotNull TagStorage tagStorage) {
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
    }

    @Nullable
    public LuckPermsHook luckPermsHook() {
        return luckPermsHook;
    }

    public void setLuckPermsHook(@Nullable LuckPermsHook luckPermsHook) {
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
        log.debug("Tag holder {} has selected tag {}", tagHolder.uuid(), tag == null ? "null" : tag.id());
    }

    public Set<TagHolder> dirtyTagHolders() {
        return dirtyTagHolders;
    }

    @Override
    public void onAddTag(TagHolder tagHolder, Tag added) {
        if (luckPermsHook == null) {
            log.warn("TagHolder {} was modified but luckpermsHook has not been set. Thus permission for the added tag was not granted.", tagHolder);
            return;
        }
        ;
        luckPermsHook.luckPerms().getUserManager().modifyUser(tagHolder.uuid(), user -> {
            if (luckPermsHook.checkPermission(user, added.permission())) return;
            luckPermsHook.addPermission(user, added.permission());
        });
    }

    public LoadingCache<UUID, TagHolder> tagHolderLoadingCache() {
        return tagHolderLoadingCache;
    }

    @Override
    public List<Tag> availableTags() {
        if (tagStorage instanceof CommonTagsStorage commonTagsStorage) {
            return commonTagsStorage.lastLoadedTags();
        } else {
            if (loadedTags == null) {
                loadedTags = new LinkedList<>(tagStorage.loadTags());
            }
            return loadedTags;
        }
    }

    @Override
    public void addAvailableTag(Tag tag) {
        tagStorage.saveTag(tag);
        availableTags().add(tag);
    }

    @Override
    public @Nullable Tag tagById(String id) {
        return tagByFirstMatchPredicate((tag) -> tag.id().equalsIgnoreCase(id));
    }


    public @Nullable Tag tagByFirstMatchPredicate(Predicate<Tag> predicate) {
        for (Tag tag : availableTags()) {
            if (predicate.test(tag)) return tag;
        }
        return null;
    }

}
