package it.einjojo.akani.core.paper.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class ItemBuilder {
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;
    private String skullTexture = null;

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder customModelData(int customModelData) {
        this.itemMeta.setCustomModelData(customModelData);
        return this;
    }


    public ItemBuilder displayName(Component displayName) {
        this.itemMeta.displayName(displayName);
        return this;
    }


    public ItemBuilder unbreakable(boolean unbreakable) {
        this.itemMeta.setUnbreakable(unbreakable);
        return this;
    }


    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
        this.itemMeta.addItemFlags(itemFlags);
        return this;
    }


    public ItemBuilder addItemFlag(ItemFlag itemFlag) {
        this.itemMeta.addItemFlags(itemFlag);
        return this;
    }


    public ItemBuilder lore(List<Component> lore) {
        this.itemMeta.lore(lore);
        return this;
    }


    public <T, V> ItemBuilder dataContainer(NamespacedKey key, PersistentDataType<T, V> type, V value) {
        this.itemMeta.getPersistentDataContainer().set(key, type, value);
        return this;
    }


    public ItemBuilder addEnchantment(Enchantment enchantment) {
        this.itemMeta.addEnchant(enchantment, 100, true);
        return this;
    }


    public ItemBuilder addEnchantments(Enchantment... enchantments) {
        for (Enchantment current : enchantments) {
            this.addEnchantment(current);
        }
        return this;
    }


    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        this.itemMeta.removeEnchant(enchantment);
        return this;
    }


    public ItemBuilder removeAllEnchantments() {
        for (Enchantment current : this.itemMeta.getEnchants().keySet()) {
            this.removeEnchantment(current);
        }
        return this;
    }


    public ItemBuilder skullTexture(String texture) {
        this.skullTexture = texture;
        return this;
    }




    public ItemStack build() {
        if (this.skullTexture != null) {
            this.itemStack.setItemMeta(this.itemMeta);
            this.itemStack.setType(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();
            PlayerProfile profile = Bukkit.getServer().createProfile(UUID.randomUUID(), "");
            profile.setProperty(new ProfileProperty("textures", this.skullTexture));
            skullMeta.setPlayerProfile(profile);
            this.itemStack.setItemMeta(skullMeta);
        } else {
            this.itemStack.setItemMeta(this.itemMeta);
        }
        return this.itemStack;
    }


}
