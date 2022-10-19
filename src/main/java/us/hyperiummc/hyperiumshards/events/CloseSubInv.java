package us.hyperiummc.hyperiumshards.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import us.hyperiummc.hyperiumshards.HyperiumShards;
import us.hyperiummc.hyperiumshards.managers.InventoryManager;

public class CloseSubInv implements Listener {

    private final HyperiumShards instance = HyperiumShards.getInstance();

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if(InventoryManager.sharders.containsKey(p))
            InventoryManager.sharders.get(p).closeMenu(p);

        if(InventoryManager.upgraders.containsKey(p)) {
            InventoryManager.upgraders.get(p).refundItems(p);
            InventoryManager.upgraders.get(p).closeMenu(p);
//            ShardMenu shardMenu = new ShardMenu();
//            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(instance, () -> shardMenu.openMenu(p), 1L);
        }

        if(InventoryManager.disenchanters.containsKey(p)) {
            InventoryManager.disenchanters.get(p).refundItems(p);
            InventoryManager.disenchanters.get(p).closeMenu(p);
//            ShardMenu shardMenu = new ShardMenu();
//            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(instance, () -> shardMenu.openMenu(p), 1L);
        }
    }

}
