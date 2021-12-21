package me.antja03.lwm.component;

import me.antja03.lwm.LightweightMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class ItemComponent extends MenuComponent {

    private ItemStack itemStack;

    public ItemComponent(LightweightMenu parentMenu, int menuSlot, ItemStack itemStack) {
        super(parentMenu, menuSlot);
        this.itemStack = itemStack;
    }

    public ItemComponent(LightweightMenu parentMenu, int menuSlot, Material material, String name, List<String> lore) {
        super(parentMenu, menuSlot);

        this.itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    public ItemComponent(LightweightMenu parentMenu, int menuSlot, Material material, String name) {
        this(parentMenu, menuSlot, material, name, Collections.emptyList());
    }

    public ItemComponent(LightweightMenu parentMenu, int menuSlot, Material material) {
        this(parentMenu, menuSlot, material, "");
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
