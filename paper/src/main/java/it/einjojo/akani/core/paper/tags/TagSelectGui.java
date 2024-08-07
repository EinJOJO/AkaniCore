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
import java.util.Objects;

public class TagSelectGui extends Gui {
    private static final String TITLE = "§cWähle einen Tag aus";
    private final TagManager tagManager;
    private final PaginationManager paginationManager;
    private final TagHolder tagHolder;

    public TagSelectGui(@NotNull Player player, TagManager tagManager) {
        super(player, "tags_select", TITLE, 6);
        this.tagManager = tagManager;
        this.paginationManager = new PaginationManager(this);
        paginationManager.registerPageSlotsBetween(0, 9 * 5 - 1);
        this.tagHolder = tagManager.tagHolder(player.getUniqueId());

    }

    private void addPageSwitchItems() {
        Icon previousPage = new Icon(Material.ARROW).onClick((event) -> {
            if (paginationManager.isFirstPage()) {
                return;
            }
            paginationManager.setPage(paginationManager.getCurrentPage() - 1);
            paginationManager.update();
            addPageSwitchItems();
        }).setName((paginationManager.isFirstPage() ? "§7" : "§a") + "Vorherige Seite");
        ;

        Icon nextPage = new Icon(Material.ARROW).onClick((event) -> {
            if (paginationManager.isLastPage()) {
                return;
            }
            paginationManager.setPage(paginationManager.getCurrentPage() + 1);
            paginationManager.update();
            addPageSwitchItems();
        }).setName((paginationManager.isLastPage() ? "§7" : "§a") + "Nächste Seite");
        addItem(45, previousPage);
        addItem(53, nextPage);
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        paginationManager.getItems().clear();
        addPageSwitchItems();
        for (Tag tag : tagManager.availableTags()) {
            if (!player.hasPermission(tag.permission())) {
                return;
            }
            Icon icon = new Icon(Material.NAME_TAG).toComp()
                    .setName(tag.displayText())
                    .setLore(List.of(
                            Component.empty(),
                            Component.text(tag.lore(), NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                            Component.empty(),
                            Component.text("Auswählen", NamedTextColor.GREEN)
                    ))
                    .toIcon().onClick((clickEvent -> {
                        onSelectTag(clickEvent, tag);
                    }));
            if (tag.equals(tagHolder.selectedTag())) {
                icon.enchant(Enchantment.ARROW_DAMAGE);
                icon.hideFlags(ItemFlag.HIDE_ENCHANTS);
            }
            paginationManager.addItem(icon);
        }
        paginationManager.update();
        if (paginationManager.getItems().isEmpty()) {
            addItem(9 * 2 + 4, new Icon(Material.BARRIER).setName("§cDu besitzt keine Tags"));
        }
    }

    private void onSelectTag(InventoryClickEvent event, Tag tag) {
        if (Objects.equals(tagHolder.selectedTag(), tag)) {
            tagHolder.setSelectedTag(null);
        } else {
            tagHolder.setSelectedTag(tag);
        }
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1, 1.2f);
        open();
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        super.onClose(event);
        if (event.getReason().equals(InventoryCloseEvent.Reason.PLAYER)) {
            tagManager.updateTagHolder(player.getUniqueId(), tagHolder);
        }

    }
}
