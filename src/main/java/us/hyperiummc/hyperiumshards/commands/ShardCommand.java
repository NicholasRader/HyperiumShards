package us.hyperiummc.hyperiumshards.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.hyperiummc.hyperiumshards.managers.inventories.ShardMenu;

public class ShardCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player p))
            return true;

        ShardMenu shardMenu = new ShardMenu();
        shardMenu.openMenu(p);

        return true;
    }

}
