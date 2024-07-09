package it.einjojo.akani.core.paper.tags;

import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.tags.CommonTagManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventSubscription;
import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.List;

public class TagHolderLuckPermsNodeChangeListener implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(TagHolderLuckPermsNodeChangeListener.class);
    private final CommonTagManager commonTagManager;
    private final List<EventSubscription<?>> subscriptions;

    public TagHolderLuckPermsNodeChangeListener(LuckPerms lp, CommonTagManager commonTagManager) {
        this.commonTagManager = commonTagManager;
        subscriptions = List.of(lp.getEventBus().subscribe(NodeMutateEvent.class, this::onLuckPermsNodeMute));
    }

    public void onLuckPermsNodeMute(NodeMutateEvent event) {
        for (Node node : event.getDataAfter()) {
            onNodeChange(node, event.getTarget());
        }

    }

    public void onNodeChange(Node node, PermissionHolder target) {
        if (!(node.getType().equals(NodeType.PERMISSION) ||
                node.getType().equals(NodeType.REGEX_PERMISSION))) return;
        if (!node.getKey().startsWith(Tag.PERMISSION_PREFIX)) return;
        log.debug("Node change detected with node: {} on target {}", node, target);
        if (target instanceof User user) {
            commonTagManager.tagHolderLoadingCache().invalidate(user.getUniqueId());
            log.debug("Invalidated tag holder for {}", user.getUniqueId());
        } else {
            commonTagManager.tagHolderLoadingCache().invalidateAll();
            log.debug("Invalidated all tag holders");
        }
    }


    @Override
    public void close() {
        for (var sub : subscriptions) {
            sub.close();
        }
    }
}
