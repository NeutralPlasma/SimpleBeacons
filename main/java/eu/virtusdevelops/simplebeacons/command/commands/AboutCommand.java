package eu.virtusdevelops.simplebeacons.command.commands;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.command.CommandInterface;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import eu.virtusdevelops.simplebeacons.utils.NBT.NBT;
import eu.virtusdevelops.simplebeacons.utils.TextFormater;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AboutCommand implements CommandInterface {
    private NBT nbt;
    private SimpleBeacons simpleBeacons;
    private MessagesHandler messagesHandler;

    public AboutCommand(NBT nbt, SimpleBeacons simpleBeacons, MessagesHandler messagesHandler){
        this.nbt = nbt;
        this.simpleBeacons = simpleBeacons;
        this.messagesHandler = messagesHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if(sender.hasPermission("simplebeacons.command.about")){
            String message = "&8====================== \n" +
                    "&7Plugin link: &ehttps://songoda.com/marketplace/product/%%__PLUGIN__%% \n" +
                    "&7Bought by: &ehttps://songoda.com/profile/%%__USERNAME__%% \n" +
                    "&8====================== ";
            sender.sendMessage(TextFormater.sFormatText(message));
        }else{
            sender.sendMessage(messagesHandler.getMessage("commands.nopermission").replace("{permission}", "simplebeacons.command.about"));
        }
        return false;
    }
}
