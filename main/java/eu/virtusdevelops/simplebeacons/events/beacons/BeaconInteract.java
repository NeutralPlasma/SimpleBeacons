package eu.virtusdevelops.simplebeacons.events.beacons;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.data.BeaconLocation;
import eu.virtusdevelops.simplebeacons.gui.Handler;
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
    private Handler handler;
    private NBT nbt;
    private HashMap<UUID, BeaconData> linking = new HashMap<>();

    public BeaconInteract(MessagesHandler messagesHandler, BeaconHandler beaconHandler, NBT nbt,
                          Handler handler, SimpleBeacons simpleBeacons){
        this.messagesHandler = messagesHandler;
        this.beaconHandler = beaconHandler;
        this.nbt = nbt;
        this.simpleBeacons = simpleBeacons;
        this.handler = handler;
    }

    public void updateLinking(Player player, BeaconData beaconData){
        if(linking.get(player.getUniqueId()) == null){
            player.sendMessage(messagesHandler.getMessage("linking.start"));
            beaconData.linkedLocations.clear();
            linking.put(player.getUniqueId(), beaconData);
        }else{
            player.sendMessage(messagesHandler.getMessage("linking.stop"));
            beaconHandler.updateBeacon(beaconData);
            linking.remove(player.getUniqueId());
        }
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
                        handler.openMainGUI(player, beaconData);
                        //player.sendMessage("Data: level=" + beaconData.level + " effect=" + beaconData.selectedEffect);
                    } else {
                        player.sendMessage(messagesHandler.getMessage("beacons.cantuse"));
                        event.setCancelled(true);
                    }
                }
            }
        }else if(event.getAction() == Action.LEFT_CLICK_BLOCK){
            if(linking.get(event.getPlayer().getUniqueId()) != null) {
                Block block = event.getClickedBlock();
                BeaconData beaconData = linking.get(event.getPlayer().getUniqueId());
                event.setCancelled(true);
                if(event.getPlayer().isSneaking()){
                    updateLinking(event.getPlayer(), beaconData);
                    return;
                }

                int links = simpleBeacons.getConfig().getInt("beacons." + beaconData.level + ".linkamount");
                int maxDistance = simpleBeacons.getConfig().getInt("beacons." + beaconData.level + ".linkDistance");

                if (links > beaconData.linkedLocations.size()) {
                    if (block.getType() == Material.CHEST) {
                        if (beaconData.beaconLocation.world.equals(block.getWorld().getName())) {
                            Location data = block.getLocation();
                            Player player = event.getPlayer();
                            BeaconLocation location = new BeaconLocation(data.getBlockX(), data.getBlockY(),
                                    data.getBlockZ(), data.getWorld().getName());
                            if(location.getDistance(beaconData.beaconLocation) > maxDistance){
                                player.sendMessage(messagesHandler.getMessage("linking.tofar"));
                                return;
                            }

                            if(location.getDistance(beaconData.beaconLocation) == 0){
                                player.sendMessage(messagesHandler.getMessage("linking.cancel"));
                                updateLinking(player, beaconData);
                                return;
                            }

                            if(beaconData.linkedLocations.contains(location.toString())){
                                player.sendMessage(messagesHandler.getMessage("linking.alreadylinked"));
                                return;
                            }
                            player.sendMessage("Distance: " + location.getDistance(beaconData.beaconLocation));
                            player.sendMessage(messagesHandler.getMessage("linking.linked"));
                            beaconData.linkedLocations.add(location.toString());

                            if(beaconData.linkedLocations.size() == links){
                                updateLinking(event.getPlayer(), beaconData);
                            }
                        }
                    }
                }else{
                    updateLinking(event.getPlayer(), beaconData);
                }
            }
        }
    }
}
