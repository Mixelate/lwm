package me.antja03.lwm.component;

import me.antja03.lwm.LightweightMenu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SlotComponent extends MenuComponent {

    public interface SlotListener {
        boolean run(Player player, Inventory inventory, ItemStack newStack);
    }

    private SlotListener onSlotFill;
    private SlotListener onSlotEmpty;

    public SlotComponent(LightweightMenu parentMenu, int menuSlot, SlotListener onSlotFill, SlotListener onSlotEmpty) {
        super(parentMenu, menuSlot);
        this.onSlotFill = onSlotFill;
        this.onSlotEmpty = onSlotEmpty;
    }

    public boolean onSlotFill(Player player, Inventory inventory, ItemStack newStack) {
        try {
            return onSlotFill.run(player, inventory, newStack);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean onSlotEmpty(Player player, Inventory inventory, ItemStack newStack) {
        try {
            return onSlotEmpty.run(player, inventory, newStack);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
