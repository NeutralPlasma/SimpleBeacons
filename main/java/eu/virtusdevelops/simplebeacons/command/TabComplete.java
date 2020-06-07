package eu.virtusdevelops.simplebeacons.command;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabComplete implements TabCompleter {
    private SimpleBeacons simpleBeacons;

    public TabComplete(SimpleBeacons simpleBeacons){
        this.simpleBeacons = simpleBeacons;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("simplebeacons")){
            List<String> players = new ArrayList<>();
            //get online players
            for(Player player : Bukkit.getOnlinePlayers()){
                players.add(player.getName());
            }
            List<String> listedPlayers = new ArrayList<>();
            // player stuff.
            if(args.length > 1){
                if(args[0].equalsIgnoreCase("give") && commandSender.hasPermission("simplebeacons.command.give")){
                    if(args.length == 2) {
                        for (String player : players) {
                            String lplayer;
                            lplayer = player.toLowerCase();
                            if (player.contains(args[1]) || lplayer.contains(args[1])) {
                                listedPlayers.add(player);
                            }
                        }
                        return listedPlayers;
                    }
                    if(args.length == 3){
                        List<String> seeds = new ArrayList<>();
                        ConfigurationSection configurationSection = simpleBeacons.getConfig().getConfigurationSection("beacons");
                        for(String entered : configurationSection.getKeys(true)){
                            String lEntered;
                            lEntered = entered.toLowerCase();
                            if(entered.contains(args[2]) || lEntered.contains(args[2])){
                                if(!entered.contains(".")){
                                    seeds.add(entered);
                                }
                            }
                        }
                        return seeds;
                    }
                    if(args.length == 4){
                        List<String> numbers = new ArrayList<>();
                        for(int i = 1; i < 65; i++){
                            numbers.add(String.valueOf(i));
                        }
                        return numbers;
                    }
                }

            }



            else{ // subcommand stuff.
                List<String> possible = new ArrayList<>();
                List<String> has = new ArrayList<>();
                if(commandSender.hasPermission("simplebeacons.command.reload")){
                    possible.add("reload");
                }
                if(commandSender.hasPermission("simplebeacons.command.editor")){
                    possible.add("editor");
                }
                if(commandSender.hasPermission("simplebeacons.command.give")){
                    possible.add("give");
                }
                for(String entered : possible){
                    String lentered = entered.toLowerCase();
                    if(entered.contains(args[0]) || lentered.contains(args[0])){
                        has.add(entered);
                    }
                }
                return has;
            }
        }
        return null;
    }
}
