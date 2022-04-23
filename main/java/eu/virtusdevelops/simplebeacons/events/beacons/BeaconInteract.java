package eu.virtusdevelops.simplebeacons.events.beacons;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.data.BeaconLocation;
import eu.virtusdevelops.simplebeacons.gui.MainGUI;
import eu.virtusdevelops.simplebeacons.storage.BeaconHandler;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import eu.virtusdevelops.simplebeacons.utils.NBT.NBT;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BeaconInteract implements Listener {
    private SimpleBeacons simpleBeacons;
    private MessagesHandler messagesHandler;
    private BeaconHandler beaconHandler;
    //private NBT nbt;
    private HashMap<UUID, BeaconData> linking = new HashMap<>();

    public BeaconInteract(MessagesHandler messagesHandler, BeaconHandler beaconHandler, NBT nbt,
                          SimpleBeacons simpleBeacons){
        this.messagesHandler = messagesHandler;
        this.beaconHandler = beaconHandler;
        //this.nbt = nbt;
        this.simpleBeacons = simpleBeacons;
    }

    public void startLinking(Player player, BeaconData data){
        beaconHandler.clearLocations(data);
        //data.setLinkedLocations(new ArrayList<>());

        linking.put(player.getUniqueId(), data);
        player.sendMessage(messagesHandler.getMessage("linking.start"));
    }

    public void stopLinking(Player player){
        linking.remove(player.getUniqueId());
    }


    public void addLocation(Player player, BeaconLocation loc){
        beaconHandler.addLocation(linking.get(player.getUniqueId()), loc);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBeaconInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block.getType() == Material.BEACON) {
                Player player = event.getPlayer();
                BeaconLocation location = new BeaconLocation(block.getX(), block.getY(),
                        block.getZ(), block.getWorld().getName());
                BeaconData beaconData = beaconHandler.getBeacon(location);

                if(beaconData != null) {
                    if (player.hasPermission("simplebeacons.use")) {
                        event.setCancelled(true);
                        new MainGUI(player, beaconData, simpleBeacons, messagesHandler, beaconHandler);

                    } else {
                        player.sendMessage(messagesHandler.getMessage("beacons.cantuse"));
                        event.setCancelled(true);
                    }
                }else{
                    if(simpleBeacons.getConfig().getBoolean("convert_default")){
                        beaconHandler.addBeacon(new BeaconData(1, "REGENERATION", player.getUniqueId(), location, new ArrayList<>(),new ArrayList<>(), 0));
                    }
                }

            }
        }else if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            if(linking.get(event.getPlayer().getUniqueId()) != null) {
                Block block = event.getClickedBlock();
                BeaconData beaconData = linking.get(event.getPlayer().getUniqueId());
                event.setCancelled(true);
                if(event.getPlayer().isSneaking()){
                    stopLinking(event.getPlayer());
                    //updateLinking(event.getPlayer(), beaconData);
                    return;
                }

                int links = simpleBeacons.getFileManager().getConfiguration("beacons").getInt("beacons." + beaconData.getLevel() + ".linkamount");
                int maxDistance = simpleBeacons.getFileManager().getConfiguration("beacons").getInt("beacons." + beaconData.getLevel() + ".linkDistance");

                if (links > beaconData.getLinkedLocations().size()) {
                    if (block.getType() == Material.CHEST) {
                        if (beaconData.getBeaconLocation().world.equals(block.getWorld().getName())) {
                            Location data = block.getLocation();
                            Player player = event.getPlayer();
                            BeaconLocation location = new BeaconLocation(data.getBlockX(), data.getBlockY(),
                                    data.getBlockZ(), data.getWorld().getName());
                            if(location.getDistance(beaconData.getBeaconLocation()) > maxDistance * maxDistance){
                                player.sendMessage(messagesHandler.getMessage("linking.tofar"));
                                return;
                            }

                            if(location.getDistance(beaconData.getBeaconLocation()) == 0){
                                player.sendMessage(messagesHandler.getMessage("linking.cancel"));
                                stopLinking(player);
                                return;
                            }

                            if(beaconData.getLinkedLocations().contains(location.toString())){
                                player.sendMessage(messagesHandler.getMessage("linking.alreadylinked"));
                                return;
                            }
                            //player.sendMessage("Distance: " + location.getDistance(beaconData.beaconLocation));

                            addLocation(player, location);
                            player.sendMessage(messagesHandler.getMessage("linking.linked"));
                            //beaconData.getLinkedLocations().add(location.toString());

                            if(beaconData.getLinkedLocations().size() == links){
                                stopLinking(player);
                                //updateLinking(event.getPlayer(), beaconData);
                            }

                        }
                    }else{
                        Player player = event.getPlayer();
                        player.sendMessage(messagesHandler.getMessage("linking.linking_in_progress"));
                    }
                }else{
                    stopLinking(event.getPlayer());
                }
            }
        }
    }
}
