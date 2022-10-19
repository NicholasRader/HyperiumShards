package us.hyperiummc.hyperiumshards.managers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.hyperiummc.hyperiumshards.managers.inventories.DisenchantMenu;
import us.hyperiummc.hyperiumshards.managers.inventories.ShardMenu;
import us.hyperiummc.hyperiumshards.managers.inventories.UpgradeMenu;

import java.util.HashMap;

public class InventoryManager {

    public static HashMap<Player, ShardMenu> sharders = new HashMap<>();
    public static HashMap<Player, UpgradeMenu> upgraders = new HashMap<>();
    public static HashMap<Player, DisenchantMenu> disenchanters = new HashMap<>();

    public static ItemStack createGlass() {
        ItemStack glass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(" ");
        glass.setItemMeta(meta);
        return glass;
    }

}
