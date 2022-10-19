package us.hyperiummc.hyperiumshards.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.hyperiummc.hyperiumshards.managers.inventories.UpgradeMenu;

public class UpgradeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))
            return true;

        UpgradeMenu upgradeMenu = new UpgradeMenu();
        upgradeMenu.openMenu((Player) sender);

        return true;
    }

}
