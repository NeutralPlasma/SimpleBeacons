package eu.virtusdevelops.simplebeacons.newCommands;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import eu.virtusdevelops.simplebeacons.utils.ListUtil;
import eu.virtusdevelops.simplebeacons.utils.NBT.NBT;
import eu.virtusdevelops.virtuscore.command.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GiveCommand extends AbstractCommand {

    private final SimpleBeacons plugin;
    private MessagesHandler message;
    private NBT nbt;

    public GiveCommand(SimpleBeacons plugin, MessagesHandler messagesHandler, NBT nbt) {
        super(CommandType.BOTH,  true, "give");
        this.plugin = plugin;
        this.message = messagesHandler;
        this.nbt = nbt;
    }


    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {

        Player player = Bukkit.getPlayer(args[0]);
        if(player == null){
            sender.sendMessage(message.getMessage("commands.unknownplayer").replace("{player}", args[0]));
            return ReturnType.SYNTAX_ERROR;
        }

        if (args.length < 1) {
            sender.sendMessage(message.getMessage("wrongSyntax").replace("{syntax}", getSyntax()));
            return ReturnType.SYNTAX_ERROR;
        }
        int level = 1;
        int amount = 1;

        try {
            if (args.length > 1) { // level of the beacon
                level = Integer.parseInt(args[1]);
            }
        }catch (NumberFormatException error){
            sender.sendMessage(message.getMessage("commands.notnumber").replace("{input}", args[1]));
            return ReturnType.SYNTAX_ERROR;
        }
        try {
            if (args.length > 2) {
                amount = Integer.parseInt(args[2]);
            }
        }catch (NumberFormatException error){
            sender.sendMessage(message.getMessage("commands.notnumber").replace("{input}", args[2]));
            return ReturnType.SYNTAX_ERROR;
        }

        ItemStack beacon = new ItemStack(Material.BEACON);
        beacon = nbt.createBeacon(beacon, level);
        beacon.setAmount(amount);
        player.getInventory().addItem(beacon);


        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if (args.length == 1) {
            return ListUtil.convertToList(Bukkit.getOnlinePlayers(), Player::getName);
        } else if (args.length == 2) {
            return getBeacons();
        } else if (args.length == 3) {
            return Arrays.asList("1","2","4","8","16","32","64");
        }
        return Collections.emptyList();

    }

    private ArrayList<String> getBeacons(){
        ArrayList<String> seeds = new ArrayList<>();
        ConfigurationSection configurationSection = plugin.getFileManager().getConfiguration("beacons").getConfigurationSection("beacons");
        for(String entered : configurationSection.getKeys(true)){
            if(!entered.contains(".")){
                seeds.add(entered);
            }
        }
        return seeds;
    }

    @Override
    public String getPermissionNode() {
        return "simplebeacons.command.give";
    }

    @Override
    public String getSyntax() {
        return "give <player> <beacon> [amount]";
    }

    @Override
    public String getDescription() {
        return "Gives specific player a specific beacon.";
    }
}
