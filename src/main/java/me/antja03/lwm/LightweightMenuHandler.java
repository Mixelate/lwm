package me.antja03.lwm;

import me.antja03.lwm.component.ButtonComponent;
import me.antja03.lwm.component.ItemComponent;
import me.antja03.lwm.component.MenuComponent;
import me.antja03.lwm.component.SlotComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LightweightMenuHandler implements Listener {

    private HashMap<Player, LightweightMenu> openMenus;

    public LightweightMenuHandler() {
        this.openMenus = new HashMap<>();
    }

    public void registerListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void openMenuForPlayer(Player player, LightweightMenu menu) {
        player.openInventory(menu.getInventory());

        if (openMenus.containsKey(player))
            openMenus.remove(player);

        openMenus.put(player, menu);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        Optional<Map.Entry<Player, LightweightMenu>> optionalEntry = openMenus.entrySet().stream()
                .filter(entry -> entry.getKey() == event.getWhoClicked() &&
                        entry.getValue().getInventory().hashCode() == event.getInventory().hashCode()).findAny();

        if (!optionalEntry.isPresent())
            return;

        LightweightMenu menu = optionalEntry.get().getValue();

        if (menu.isAllowDragging())
            return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Optional<Map.Entry<Player, LightweightMenu>> optionalEntry = openMenus.entrySet().stream()
                .filter(entry -> entry.getKey() == event.getWhoClicked() &&
                        entry.getValue().getInventory().hashCode() == event.getClickedInventory().hashCode()).findAny();

        if (!optionalEntry.isPresent())
            return;

        Player player = optionalEntry.get().getKey();
        LightweightMenu menu = optionalEntry.get().getValue();

        if (event.getClick() != ClickType.LEFT) {
            event.setCancelled(true);
            return;
        }

        for (MenuComponent component : menu.getComponentList()) {
            if (component.getMenuSlot() != event.getSlot())
                continue;

            if (component instanceof ButtonComponent) {
                ((ButtonComponent) component).runOnClick(player, menu);
                continue;
            }

            if (component instanceof ItemComponent) {
                ItemComponent itemComponent = (ItemComponent) component;
                ItemStack stackInSlot = itemComponent.getStackInSlot();

                event.getClickedInventory().setItem(event.getSlot(), stackInSlot);
                player.setItemOnCursor(event.getCursor());
                event.setCancelled(true);

                continue;
            }

            if (component instanceof SlotComponent) {
                SlotComponent slotComponent = (SlotComponent) component;
                ItemStack stackInSlot = slotComponent.getStackInSlot();

                if (stackInSlot == null && event.getCursor().getType() == Material.AIR)
                    continue;

                if (stackInSlot == null && event.getCursor().getType() != Material.AIR) {
                    event.setCancelled(true);

                    if (slotComponent.onSlotFill(player, event.getClickedInventory().getItem(event.getSlot()), event.getCursor())) {
                        event.getClickedInventory().setItem(event.getSlot(), event.getCursor());
                        player.setItemOnCursor(null);
                    } else {
                        event.getClickedInventory().setItem(event.getSlot(), stackInSlot);
                        player.setItemOnCursor(event.getCursor());
                    }

                    return;
                }

                if (stackInSlot != null && event.getCursor().getType() == Material.AIR) {
                    event.setCancelled(true);

                    if (slotComponent.onSlotEmpty(player, event.getClickedInventory().getItem(event.getSlot()))) {
                        player.setItemOnCursor(stackInSlot);
                        event.getClickedInventory().setItem(event.getSlot(), null);
                    } else {
                        event.getClickedInventory().setItem(event.getSlot(), stackInSlot);
                        player.setItemOnCursor(event.getCursor());
                    }

                    return;
                }

                if (stackInSlot != null && event.getCursor().getType() != Material.AIR) {
                    if (!slotComponent.onSlotEmpty(player, event.getClickedInventory().getItem(event.getSlot()))) {
                        event.setCancelled(true);
                    } else {
                        event.getClickedInventory().setItem(event.getSlot(), null);
                    }

                    if (!slotComponent.onSlotFill(player, event.getClickedInventory().getItem(event.getSlot()), event.getCursor())) {
                        event.setCancelled(true);
                    } else {
                        event.getClickedInventory().setItem(event.getSlot(), event.getCursor());
                        player.setItemOnCursor(null);
                    }

                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (openMenus.containsKey(player)) {
            LightweightMenu menu = openMenus.get(player);
            menu.onClose(player);
            openMenus.remove(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (openMenus.containsKey(event.getPlayer())) {
            LightweightMenu menu = openMenus.get(event.getPlayer());
            Player player = event.getPlayer().getServer().getPlayer(event.getPlayer().getUniqueId());
            menu.onClose(player);
            openMenus.remove(event.getPlayer());
        }
    }
}
