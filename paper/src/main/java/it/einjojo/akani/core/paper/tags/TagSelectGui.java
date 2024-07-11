package it.einjojo.akani.core.paper.tags;

import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.api.tags.TagHolder;
import it.einjojo.akani.core.api.tags.TagManager;
import it.einjojo.akani.util.inventory.Gui;
import it.einjojo.akani.util.inventory.Icon;
import it.einjojo.akani.util.inventory.pagination.PaginationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
                    .setLore(List.of(
                            Component.empty(),
                            Component.text(tag.lore(), NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                            Component.empty(),
                            Component.text("Auswählen", NamedTextColor.GREEN)
                    ))
                    .toIcon();
            if (tag.equals(tagHolder.selectedTag())) {
                icon.enchant(Enchantment.ARROW_DAMAGE);
                icon.hideFlags(ItemFlag.HIDE_ENCHANTS);
            } else {
                icon.onClick((clickEvent -> {
                    onSelectTag(clickEvent, tag);
                }));
            }
            paginationManager.addItem(icon);
        }
    }

    private void onSelectTag(InventoryClickEvent event, Tag tag) {
        open();
        tagHolder.setSelectedTag(tag);
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1.2f);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        super.onClose(event);
        player.sendMessage(event.getReason().name()); //TODO
        if (event.getReason().equals(InventoryCloseEvent.Reason.OPEN_NEW)) {
            return;
        }
        tagManager.updateTagHolder(player.getUniqueId(), tagHolder);

    }
}
