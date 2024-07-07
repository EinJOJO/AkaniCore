package it.einjojo.akani.core.paper.tags;

import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.api.tags.TagHolder;
import it.einjojo.akani.core.api.tags.TagManager;
import mc.obliviate.inventory.Gui;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.pagination.PaginationManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

public class TagSelectGui extends Gui {
    private static final String TITLE = "§cWähle einen Tag aus";
    private final TagManager tagManager;
    private final PaginationManager paginationManager;
    private final TagHolder tagHolder;
    public TagSelectGui(@NotNull Player player, TagManager tagManager) {
        super(player, "tags_select", TITLE, 6);
        this.tagManager = tagManager;
        this.paginationManager = new PaginationManager(this);
        this.tagHolder = tagManager.tagHolder(player.getUniqueId());

    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        paginationManager.getItems().clear();
        for (Tag tag : tagManager.availableTags()) {
            Icon icon = new Icon(Material.NAME_TAG).toComp()
                    .setName(tag.displayText())
                    .toIcon();
            if (tag.equals(tagHolder.selectedTag())) {
                icon.enchant(Enchantment.ARROW_DAMAGE);
                icon.hideFlags(ItemFlag.HIDE_ENCHANTS);
            }
            paginationManager.addItem(icon);
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        super.onClose(event);
        player.sendMessage(event.getReason().name());
        if (event.getReason().equals(InventoryCloseEvent.Reason.OPEN_NEW)) {
            return;
        }
        tagManager.updateTagHolder(player.getUniqueId(), tagHolder);

    }
}
