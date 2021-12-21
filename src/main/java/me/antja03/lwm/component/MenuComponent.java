package me.antja03.lwm.component;

import me.antja03.lwm.LightweightMenu;
import org.bukkit.inventory.ItemStack;

public class MenuComponent {

    private LightweightMenu parentMenu;
    private int menuSlot;

    public MenuComponent(LightweightMenu parentMenu, int menuSlot) {
        this.parentMenu = parentMenu;
        this.menuSlot = menuSlot;
    }

    public ItemStack getStackInSlot() {
        return parentMenu.getInventory().getItem(menuSlot);
    }

    public int getMenuSlot() {
        return menuSlot;
    }
}
