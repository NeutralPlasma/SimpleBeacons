package eu.virtusdevelops.simplebeacons.command;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.gui.Handler;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandInterface{

    private SimpleBeacons simpleBeacons;
    private MessagesHandler messagesHandler;
    private Handler handler;

    public MainCommand(SimpleBeacons simpleBeacons, Handler handler, MessagesHandler messagesHandler){
        this.simpleBeacons = simpleBeacons;
        this.handler = handler;
        this.messagesHandler = messagesHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (sender.hasPermission("simplebeacons.command.main")) {

        }else {
            sender.sendMessage(messagesHandler.getMessage("commands.nopermission").replace("{permission}", "simplebeacons.command.main"));
            return true;
        }

        return true;
    }


}
