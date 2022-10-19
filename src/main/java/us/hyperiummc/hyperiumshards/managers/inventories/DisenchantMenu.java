package us.hyperiummc.hyperiumshards.managers.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import us.hyperiummc.hyperiumshards.HyperiumShards;
import us.hyperiummc.hyperiumshards.managers.InventoryManager;

public class DisenchantMenu {

    private final HyperiumShards instance = HyperiumShards.getInstance();

    private Inventory gui;

    public Inventory getMenu() { return this.gui; }

    private String title = instance.getConfig().getString("DisenchantMenu.Title");

    public void openMenu(Player p) {
        InventoryManager.disenchanters.put(p, this);
        p.openInventory(this.getMenu());
    }

    public void closeMenu(Player p) {
        InventoryManager.disenchanters.remove(p);
        p.closeInventory();

        for(int i = 0; i < p.getInventory().getSize(); i++) {
            if(p.getInventory().getItem(i) == null)
                continue;

            if(p.getInventory().getItem(i).isSimilar(ShardMenu.getUpgrade()))
                p.getInventory().getItem(i).setAmount(0);
            else if(p.getInventory().getItem(i).isSimilar(ShardMenu.getDisenchant()))
                p.getInventory().getItem(i).setAmount(0);
        }
    }

    public int getEmptySlots() {
        int empty = 0;
        for(int i = 1; i < this.getMenu().getSize(); i++) {
            if(this.getMenu().getItem(i) == null) {
                empty++;
            }
        }
        return empty;
    }

    public boolean isFull() {
        for(int i = 1; i < this.getMenu().getSize(); i++) {
            if(this.getMenu().getItem(i) == null)
                return false;
        }
        return true;
    }

    public boolean isEmpty() {
        for(int i = 1; i < this.getMenu().getSize(); i++) {
            if(this.getMenu().getItem(i) != null)
                return false;
        }
        return true;
    }

    public long checkValue() {
        long total = 0;
        for(int i = 1; i < this.getMenu().getSize(); i++) {
            if(this.getMenu().getItem(i) != null && this.getMenu().getItem(i).hasItemMeta()) {
                String clickedLore = ChatColor.stripColor(this.getMenu().getItem(i).getItemMeta().getLore().toString());
                if(clickedLore.contains("Tier I"))
                    total += 20000;
                else if(clickedLore.contains("Tier II"))
                    total += 50000;
                else if(clickedLore.contains("Tier III"))
                    total += 250000;
            }
        }
        return total;
    }

    public void refundItems(Player p) {
        for(int i = 1; i < this.getMenu().getSize(); i++) {
            if(this.getMenu().getItem(i) != null) {
                if (p.getInventory().firstEmpty() == -1)
                    p.getWorld().dropItemNaturally(p.getLocation(), this.getMenu().getItem(i));
                else
                    p.getInventory().addItem(this.getMenu().getItem(i));
            }
        }
    }

    public DisenchantMenu() {
        this.gui = Bukkit.createInventory(null, InventoryType.WORKBENCH, ChatColor.translateAlternateColorCodes('&', this.title));
    }
}
