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
import java.util.List;

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
    public void fillSpaceWithButton(MenuItem menuItem) {
        List<Integer> filledspots = new ArrayList();
        int i = 0;
        for (MenuItem button: buttons) {
            filledspots.add(button.slot);
            i++;
        }

        for (int j = 0; j < size; j++) {
            if (filledspots.contains(j))
                continue;
            MenuItem button = menuItem;
            button.slot = j;
            addButton(button);
        }
    }
    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != this) return;
        if (size < event.getSlot()) return;

        MenuItem slot = buttons.stream().filter(item -> item.slot == event.getSlot()).findFirst().get();
        if (slot == null)
            return;
        slot.onButtonClick((Player) event.getWhoClicked(),event);
    }

    public void showPlayer(Player player) {
        player.closeInventory();
        player.openInventory(getInventory());
    }
}
