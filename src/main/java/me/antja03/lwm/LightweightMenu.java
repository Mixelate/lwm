package me.antja03.lwm;

import me.antja03.lwm.component.ItemComponent;
import me.antja03.lwm.component.MenuComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class LightweightMenu {

    private String title;
    private int rows;
    private ArrayList<MenuComponent> componentList;
    private Inventory inventory;
    private boolean allowDragging = false;

    public LightweightMenu(String title, int rows) {
        this.title = title;
        this.rows = rows;
        this.componentList = new ArrayList<>();
    }

    public void onClose(Player player) { }

    public Inventory getInventory() {
        if (inventory == null) {
            inventory = Bukkit.createInventory(null, rows * 9, title);

            for (MenuComponent component : componentList) {
                if (!(component instanceof ItemComponent))
                    continue;

                ItemComponent itemComponent = (ItemComponent) component;
                inventory.setItem(itemComponent.getMenuSlot(), itemComponent.getItemStack());
            }
        }

        return inventory;
    }

    public ArrayList<MenuComponent> getComponentList() {
        return componentList;
    }

    public void addMenuComponent(MenuComponent component) {
        if (isSlotTaken(component.getMenuSlot()))
            throw new RuntimeException("Multiple components using the same inventory slot");

        componentList.add(component);
    }

    public void addMenuComponents(MenuComponent... components) {
        for (MenuComponent component : components) {
            addMenuComponent(component);
        }
    }

    public boolean isSlotTaken(int slot) {
        for (MenuComponent component : componentList) {
            if (component.getMenuSlot() == slot)
                return true;
        }

        return false;
    }

    public boolean isAllowDragging() {
        return allowDragging;
    }

    public void setAllowDragging(boolean allowDragging) {
        this.allowDragging = allowDragging;
    }
}
