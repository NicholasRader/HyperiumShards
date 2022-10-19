package us.hyperiummc.hyperiumshards.managers.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import us.hyperiummc.hyperiumshards.HyperiumShards;
import us.hyperiummc.hyperiumshards.managers.InventoryManager;

public class UpgradeMenu {

    private final HyperiumShards instance = HyperiumShards.getInstance();

    private Inventory gui;

    public Inventory getMenu() { return this.gui; }

    private String title = instance.getConfig().getString("UpgradeMenu.Title");

    public void openMenu(Player p) {
        InventoryManager.upgraders.put(p, this);
        p.openInventory(this.getMenu());
    }

    public void closeMenu(Player p) {
        InventoryManager.upgraders.remove(p);
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

    public int getEmptySlots() {
        int empty = 0;
        for(int i = 1; i < this.getMenu().getSize(); i++) {
            if(this.getMenu().getItem(i) == null) {
                empty++;
            }
        }
        return empty;
    }

    public int isCorrectlyFull() {
        int numT1 = 0;
        int numT2 = 0;
        for(int i = 1; i < this.getMenu().getSize(); i++) {
            if(this.getMenu().getItem(i) == null)
                return -1;
            if(this.getMenu().getItem(i).getType() != Material.PRISMARINE_SHARD)
                return -1;
            if(!this.getMenu().getItem(i).hasItemMeta())
                return -1;
            if(this.getMenu().getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase(instance.tier1Name))
                numT1++;
            if(this.getMenu().getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase(instance.tier2Name))
                numT2++;
        }

        if(numT1 == 9)
            return 1;
        if(numT2 == 9)
            return 2;

        return -1;
    }

    public boolean isFull() {
        for(int i = 1; i < this.getMenu().getSize(); i++) {
            if(this.getMenu().getItem(i) == null)
                return false;
        }
        return true;
    }

    public UpgradeMenu() {
        this.gui = Bukkit.createInventory(null, InventoryType.WORKBENCH, ChatColor.translateAlternateColorCodes('&', this.title));
    }
}
