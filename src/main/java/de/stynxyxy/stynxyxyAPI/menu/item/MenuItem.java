package de.stynxyxy.stynxyxyAPI.menu.item;

import de.stynxyxy.stynxyxyAPI.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class MenuItem {
    public ItemStack itemStack;
    public int slot;
    public Menu menu;


    public MenuItem(Material material,int slot) {
        this(material,1,slot);
    }
    public  MenuItem(Material material, int num, int slot) {
        this(new ItemStack(material,num),slot);
    }
    public MenuItem(ItemStack stack, int slot) {
        this.itemStack = stack.clone();
        this.slot = slot;
    }

    public abstract void onButtonClick(Player player, InventoryClickEvent event);

}
