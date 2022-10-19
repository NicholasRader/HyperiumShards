package us.hyperiummc.hyperiumshards.managers.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.hyperiummc.hyperiumshards.HyperiumShards;
import us.hyperiummc.hyperiumshards.managers.InventoryManager;
import us.hyperiummc.hyperiumshards.utils.Chat;

import java.util.List;

public class ShardMenu {

    private final HyperiumShards instance = HyperiumShards.getInstance();

    private final Inventory gui;

    public Inventory getMenu() { return this.gui; }

    private final int size = instance.getConfig().getInt("ShardMenu.Size");

    private String title = instance.getConfig().getString("ShardMenu.Title");

    private static final ItemStack upgrade = initUpgrade();
    public static ItemStack getUpgrade() { return upgrade; }

    private static final ItemStack disenchant = initDisenchant();
    public static ItemStack getDisenchant() { return disenchant; }

    public void openMenu(Player p) {
        InventoryManager.sharders.put(p, this);
        p.openInventory(this.getMenu());
    }

    public void closeMenu(Player p) {
        InventoryManager.sharders.remove(p);
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

    public ShardMenu() {
        this.gui = Bukkit.createInventory(null, this.size, ChatColor.translateAlternateColorCodes('&', this.title));

        for (int i = 0; i < this.size; i++) {
            this.gui.setItem(i, InventoryManager.createGlass());
        }

        this.gui.setItem(instance.getConfig().getInt("ShardMenu.Upgrade.Slot"), upgrade);
        this.gui.setItem(instance.getConfig().getInt("ShardMenu.Disenchant.Slot"), disenchant);
    }

    private static ItemStack initUpgrade() {
        ItemStack upg = new ItemStack(Material.PRISMARINE_SHARD, 1);

        ItemMeta upgradeMeta = upg.getItemMeta();
        upgradeMeta.setDisplayName(Chat.color(HyperiumShards.getInstance().getConfig().getString("ShardMenu.Upgrade.Title")));
        List<String> upgradeLore = HyperiumShards.getInstance().getConfig().getStringList("ShardMenu.Upgrade.Description");
        for(int i = 0; i < upgradeLore.size(); i++) {
            String s = Chat.color(upgradeLore.get(i));
            upgradeLore.set(i, s);
        }
        upgradeMeta.setLore(upgradeLore);
        upgradeMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        upgradeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        upg.setItemMeta(upgradeMeta);

        return upg;
    }

    private static ItemStack initDisenchant() {
        ItemStack dis = new ItemStack(Material.PRISMARINE_CRYSTALS, 1);

        ItemMeta disenchantMeta = dis.getItemMeta();
        disenchantMeta.setDisplayName(Chat.color(HyperiumShards.getInstance().getConfig().getString("ShardMenu.Disenchant.Title")));
        List<String> disenchantLore = HyperiumShards.getInstance().getConfig().getStringList("ShardMenu.Disenchant.Description");
        for(int i = 0; i < disenchantLore.size(); i++) {
            String s = Chat.color(disenchantLore.get(i));
            disenchantLore.set(i, s);
        }
        disenchantMeta.setLore(disenchantLore);
        disenchantMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        disenchantMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        dis.setItemMeta(disenchantMeta);

        return dis;
    }

}
