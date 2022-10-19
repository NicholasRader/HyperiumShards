package us.hyperiummc.hyperiumshards.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.hyperiummc.hyperiumshards.HyperiumShards;
import us.hyperiummc.hyperiumshards.managers.InventoryManager;
import us.hyperiummc.hyperiumshards.managers.inventories.DisenchantMenu;
import us.hyperiummc.hyperiumshards.managers.inventories.ShardMenu;
import us.hyperiummc.hyperiumshards.managers.inventories.UpgradeMenu;
import us.hyperiummc.hyperiumshards.utils.Chat;

public class ShardMenuClick implements Listener {

    private final HyperiumShards instance = HyperiumShards.getInstance();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ItemStack clicked = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();
        int slot = e.getRawSlot();

        if (clicked == null)
            return;

        if(InventoryManager.sharders.containsKey(p)) {
            e.setCancelled(true);

            if (clicked.equals(InventoryManager.sharders.get(p).getUpgrade())) {
                InventoryManager.sharders.get(p).closeMenu(p);
                UpgradeMenu upgradeMenu = new UpgradeMenu();
                upgradeMenu.openMenu(p);
            } else if (clicked.equals(InventoryManager.sharders.get(p).getDisenchant())) {
                InventoryManager.sharders.get(p).closeMenu(p);
                DisenchantMenu disenchantMenu = new DisenchantMenu();
                disenchantMenu.openMenu(p);
            }
        }
        else if(InventoryManager.upgraders.containsKey(p)) {
            e.setCancelled(true);

            Inventory inv = InventoryManager.upgraders.get(p).getMenu();

            if(!clicked.hasItemMeta())
                return;

            String clickedName = ChatColor.translateAlternateColorCodes('&', clicked.getItemMeta().getDisplayName());

            if(!(clickedName.equalsIgnoreCase(instance.tier1Name) || clickedName.equalsIgnoreCase(instance.tier2Name) || clickedName.equalsIgnoreCase(instance.tier3Name)))
                return;

            if(slot > 9) {
                // player inv
                if((e.getClick().isLeftClick() || e.getClick().isRightClick()) && !e.getClick().isShiftClick()) {
                    if(InventoryManager.upgraders.get(p).isFull())
                        return;
                    ItemStack single = new ItemStack(clicked);
                    single.setAmount(1);
                    clicked.setAmount(clicked.getAmount() - 1);
                    for(int i = 1; i < inv.getSize(); i++) {
                        if(inv.getItem(i) == null) {
                            inv.setItem(i, single);
                            break;
                        }
                    }
                }
                else if(e.getClick().isShiftClick()) {
                    // add max possible (upper max: 9) to slots
                    int cond;
                    int emptySlots = InventoryManager.upgraders.get(p).getEmptySlots();
                    if(emptySlots >= clicked.getAmount())
                        cond = clicked.getAmount();
                    else
                        cond = emptySlots;
                    ItemStack single = new ItemStack(clicked);
                    single.setAmount(1);
                    int i = 1;
                    while(i < cond+1 || !InventoryManager.upgraders.get(p).isFull() && clicked.getAmount() > 0) {
                        if(inv.getItem(i) == null) {
                            clicked.setAmount(clicked.getAmount() - 1);
                            inv.setItem(i, single);
                        }
                        i++;
                    }
                }
            }
            else {
                // upgrade inv
                if(slot == 0) {
                    // result slot
                    int tier = InventoryManager.upgraders.get(p).isCorrectlyFull();
                    if(tier == -1)
                        return;
                    for(int i = 0; i < inv.getSize(); i++) {
                        inv.clear(i);
                    }
                    String command = "re lostshard " + p.getName() + " " + (tier+1) + " 1";
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
                }
                else {
                    // shards slot
                    inv.clear(e.getSlot());
                    if (p.getInventory().firstEmpty() == -1)
                        p.getWorld().dropItemNaturally(p.getLocation(), clicked);
                    else
                        p.getInventory().addItem(clicked);
                }
            }
            // check if correctly full, make next tier appear
            switch(InventoryManager.upgraders.get(p).isCorrectlyFull()) {
                case 1:
                    // appear 2
                    ItemStack t2 = new ItemStack(Material.PRISMARINE_SHARD, 1);
                    ItemMeta t2Meta = t2.getItemMeta();
                    t2Meta.setDisplayName(Chat.color(instance.getConfig().getString("Shards.T2")));
                    t2Meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    t2Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    t2.setItemMeta(t2Meta);
                    inv.setItem(0, t2);
                    break;
                case 2:
                    // appear 3
                    ItemStack t3 = new ItemStack(Material.PRISMARINE_SHARD, 1);
                    ItemMeta t3Meta = t3.getItemMeta();
                    t3Meta.setDisplayName(Chat.color(instance.getConfig().getString("Shards.T3")));
                    t3Meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    t3Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    t3.setItemMeta(t3Meta);
                    inv.setItem(0, t3);
                    break;
                default:
                    inv.setItem(0, null);
            }
        }
        else if(InventoryManager.disenchanters.containsKey(p)) {
            e.setCancelled(true);

            Inventory inv = InventoryManager.disenchanters.get(p).getMenu();

            if(!clicked.hasItemMeta())
                return;

            if(slot > 9) {
                // player inv
                String clickedLore = ChatColor.stripColor(clicked.getItemMeta().getLore().toString());

                if(!(clickedLore.contains("Tier I") || clickedLore.contains("Tier II") || clickedLore.contains("Tier III")))
                    return;

                if((e.getClick().isLeftClick() || e.getClick().isRightClick()) && !e.getClick().isShiftClick()) {
                    if(InventoryManager.disenchanters.get(p).isFull())
                        return;
                    ItemStack single = new ItemStack(clicked);
                    single.setAmount(1);
                    clicked.setAmount(clicked.getAmount() - 1);
                    for(int i = 1; i < inv.getSize(); i++) {
                        if(inv.getItem(i) == null) {
                            inv.setItem(i, single);
                            break;
                        }
                    }
                }
                else if(e.getClick().isShiftClick()) {
                    // add max possible (upper max: 9) to slots
                    int cond;
                    int emptySlots = InventoryManager.disenchanters.get(p).getEmptySlots();
                    if(emptySlots >= clicked.getAmount())
                        cond = clicked.getAmount();
                    else
                        cond = emptySlots;
                    ItemStack single = new ItemStack(clicked);
                    single.setAmount(1);
                    int i = 1;
                    while(i < cond+1 || !InventoryManager.disenchanters.get(p).isFull() && clicked.getAmount() > 0) {
                        if(inv.getItem(i) == null) {
                            clicked.setAmount(clicked.getAmount() - 1);
                            inv.setItem(i, single);
                        }
                        i++;
                    }
                }
            }
            else {
                // disenchant inv
                if(!(clicked.getType() == Material.SUNFLOWER)) {
                    // shard clicked
                    inv.clear(e.getSlot());
                    if (p.getInventory().firstEmpty() == -1)
                        p.getWorld().dropItemNaturally(p.getLocation(), clicked);
                    else
                        p.getInventory().addItem(clicked);
                }

                if(clicked.getType() == Material.SUNFLOWER) {
                    // token clicked
                    for(int i = 0; i < inv.getSize(); i++) {
                        inv.clear(i);
                    }
                    long tokens = Long.parseLong(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).substring(1));
                    String command = "re currency add Tokens " + p.getName() + " " + tokens + " true";
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
                }
            }
            // calculate tokens to give
            if(!(InventoryManager.disenchanters.get(p).isEmpty())) {
                long give = InventoryManager.disenchanters.get(p).checkValue();
                ItemStack tokens = new ItemStack(Material.SUNFLOWER, 1);
                ItemMeta tokensMeta = tokens.getItemMeta();
                tokensMeta.setDisplayName(Chat.color("&e⛀" + give));
                tokensMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                tokensMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                tokens.setItemMeta(tokensMeta);
                inv.setItem(0, tokens);
            }
            else
                inv.clear(0);
        }
    }

}
