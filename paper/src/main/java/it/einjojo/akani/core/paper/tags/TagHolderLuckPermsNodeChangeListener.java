package it.einjojo.akani.core.paper.tags;

import com.google.common.eventbus.Subscribe;
import it.einjojo.akani.core.tags.CommonTagManager;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Target;

public class TagHolderLuckPermsNodeChangeListener {
    private static final Logger log = LoggerFactory.getLogger(TagHolderLuckPermsNodeChangeListener.class);
    private final CommonTagManager commonTagManager;

    public TagHolderLuckPermsNodeChangeListener(CommonTagManager commonTagManager) {
        this.commonTagManager = commonTagManager;
    }

    @Subscribe
    public void onLuckPermsNodeAdd(NodeAddEvent addEvent) {
        log.info("Node added k:{} v: {}", addEvent.getNode().getKey(), addEvent.getNode().getValue());

        var target = addEvent.getTarget();
        if (target instanceof User user) {
            commonTagManager.tagHolder(user.getUniqueId())
                    .addTag(commonTagManager.tagByFirstMatchPredicate(tag -> tag.permission().equals(addEvent.getNode().getKey())));
        }


    }



}
