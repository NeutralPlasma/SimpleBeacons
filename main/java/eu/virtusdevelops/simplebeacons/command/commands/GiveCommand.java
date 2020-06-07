package eu.virtusdevelops.simplebeacons.command.commands;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.command.CommandInterface;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import eu.virtusdevelops.simplebeacons.utils.NBT.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand implements CommandInterface {
    private NBT nbt;
    private SimpleBeacons simpleBeacons;
    private MessagesHandler messagesHandler;

    public GiveCommand(NBT nbt, SimpleBeacons simpleBeacons, MessagesHandler messagesHandler){
        this.nbt = nbt;
        this.simpleBeacons = simpleBeacons;
        this.messagesHandler = messagesHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if(sender.hasPermission("simplebeacons.command.give")){
            if(args.length > 1){
                Player player = Bukkit.getPlayer(args[1]);
                if(player == null){
                    sender.sendMessage(messagesHandler.getMessage("commands.unknownplayer").replace("{player}", args[1]));
                    return false;
                }
                int level = 1;
                int amount = 1;

                try {
                    if (args.length > 2) { // level of the beacon
                        level = Integer.parseInt(args[2]);
                    }
                }catch (NumberFormatException error){
                    sender.sendMessage(messagesHandler.getMessage("commands.notnumber").replace("{input}", args[2]));
                }
                try {
                    if (args.length > 3) {
                        amount = Integer.parseInt(args[3]);
                    }
                }catch (NumberFormatException error){
                    sender.sendMessage(messagesHandler.getMessage("commands.notnumber").replace("{input}", args[3]));
                }

                ItemStack beacon = new ItemStack(Material.BEACON);
                beacon = nbt.createBeacon(beacon, level);
                beacon.setAmount(amount);
                player.getInventory().addItem(beacon);
            }else{
                sender.sendMessage(messagesHandler.getMessage("commands.missingplayer"));
            }
            int level = 1;
        }else{
            sender.sendMessage(messagesHandler.getMessage("commands.nopermission").replace("{permission}", "simplebeacons.command.give"));
        }
        return false;
    }
}
