package us.hyperiummc.hyperiumshards.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.hyperiummc.hyperiumshards.managers.inventories.DisenchantMenu;

public class DisenchantCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return true;

        DisenchantMenu disenchantMenu = new DisenchantMenu();
        disenchantMenu.openMenu((Player) sender);

        return true;
    }

}
