package eu.virtusdevelops.simplebeacons.command.commands;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.command.CommandInterface;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import eu.virtusdevelops.simplebeacons.utils.NBT.NBT;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandInterface {
    private NBT nbt;
    private SimpleBeacons simpleBeacons;
    private MessagesHandler messagesHandler;

    public ReloadCommand(NBT nbt, SimpleBeacons simpleBeacons, MessagesHandler messagesHandler){
        this.nbt = nbt;
        this.simpleBeacons = simpleBeacons;
        this.messagesHandler = messagesHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if(sender.hasPermission("simplebeacons.command.reload")){
            long time = System.currentTimeMillis();
            simpleBeacons.reload();
            time = System.currentTimeMillis() - time;
            sender.sendMessage(messagesHandler.getMessage("commands.reloadsuccess").replace("{time}" , String.valueOf(time)));
        }else{
            sender.sendMessage(messagesHandler.getMessage("commands.nopermission").replace("{permission}", "simplebeacons.command.reload"));
        }
        return false;
    }
}
