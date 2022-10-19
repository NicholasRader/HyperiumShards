package us.hyperiummc.hyperiumshards;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import us.hyperiummc.hyperiumshards.commands.DisenchantCommand;
import us.hyperiummc.hyperiumshards.commands.ShardCommand;
import us.hyperiummc.hyperiumshards.commands.UpgradeCommand;
import us.hyperiummc.hyperiumshards.events.CloseSubInv;
import us.hyperiummc.hyperiumshards.events.ShardMenuClick;

public final class HyperiumShards extends JavaPlugin {

    public static HyperiumShards getInstance() {
        return JavaPlugin.getPlugin(HyperiumShards.class);
    }

    public String tier1Name = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Shards.T1"));
    public String tier2Name = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Shards.T2"));
    public String tier3Name = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Shards.T3"));

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("shard").setExecutor(new ShardCommand());
        getCommand("upgrade").setExecutor(new UpgradeCommand());
        getCommand("disenchant").setExecutor(new DisenchantCommand());
        getServer().getPluginManager().registerEvents(new ShardMenuClick(), this);
        getServer().getPluginManager().registerEvents(new CloseSubInv(), this);
    }

}
