package me.antja03.lwm.component;

import me.antja03.lwm.LightweightMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class ButtonComponent extends ItemComponent {

    public interface ClickListener {
        boolean onClick(Player player, LightweightMenu menu);
    }

    private ClickListener clickListener;

    public ButtonComponent(LightweightMenu parentMenu, int menuSlot, ItemStack itemStack, ClickListener clickListener) {
        super(parentMenu, menuSlot, itemStack);
        this.clickListener = clickListener;
    }

    public ButtonComponent(LightweightMenu parentMenu, int menuSlot, Material material, String name, List<String> lore, ClickListener clickListener) {
        super(parentMenu, menuSlot, material, name, lore);
        this.clickListener = clickListener;
    }

    public ButtonComponent(LightweightMenu parentMenu, int menuSlot, Material material, String name, ClickListener clickListener) {
        this(parentMenu, menuSlot, material, name, Collections.emptyList(), clickListener);
    }

    public ButtonComponent(LightweightMenu parentMenu, int menuSlot, Material material, ClickListener clickListener) {
        this(parentMenu, menuSlot, material, "", clickListener);
    }

    public boolean runOnClick(Player player, LightweightMenu menu) {
        try {
            return clickListener.onClick(player, menu);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
