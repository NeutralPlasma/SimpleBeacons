package eu.virtusdevelops.simplebeacons.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface CommandInterface {

    // Method for main command.
    boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args);

}
