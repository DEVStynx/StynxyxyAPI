package de.stynxyxy.stynxyxyAPI.menu;

import de.stynxyxy.stynxyxyAPI.PaperAPI;
import de.stynxyxy.stynxyxyAPI.menu.item.MenuItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class Menu implements InventoryHolder, Listener {
    public int size;
    private ArrayList<MenuItem> buttons;
    public String name;

    public Menu(String name, int rows) {
        this.name = name;
        buttons = new ArrayList<>();
        size = rows * 9;
        Bukkit.getPluginManager().registerEvents(this,PaperAPI.getCustomPlugin());
        initButtons();
    }

    public void addButton(MenuItem item) {
        buttons.add(item);
    }

    public abstract void initButtons();

    @Override
    public @NotNull Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this,size, Component.text(name));
        for (MenuItem button : buttons) {
            inventory.setItem(button.slot,button.itemStack);
        }
        return inventory;
    }
    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != this) return;
        if (buttons.size() < event.getSlot()) return;

        MenuItem slot = buttons.get(event.getSlot());
        slot.onButtonClick((Player) event.getWhoClicked(),event);
    }

    public void showPlayer(Player player) {
        player.closeInventory();
        player.openInventory(getInventory());
    }
}
