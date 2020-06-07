package eu.virtusdevelops.simplebeacons.events.beacons;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.data.BeaconLocation;
import eu.virtusdevelops.simplebeacons.storage.BeaconHandler;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import eu.virtusdevelops.simplebeacons.utils.NBT.NBT;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BeaconPlace implements Listener {
    private SimpleBeacons simpleBeacons;
    private NBT nbt;
    private BeaconHandler beaconHandler;
    private MessagesHandler messagesHandler;

    public BeaconPlace(NBT nbt, BeaconHandler beaconHandler, MessagesHandler messagesHandler, SimpleBeacons simpleBeacons){
        this.nbt = nbt;
        this.beaconHandler = beaconHandler;
        this.messagesHandler = messagesHandler;
        this.simpleBeacons = simpleBeacons;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBeaconPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        ItemStack itemStack = event.getItemInHand();
        if(itemStack.getType() == Material.BEACON){
            if(itemStack.hasItemMeta()){
                int level = nbt.getInt(itemStack, "level");
                if(level != 0){
                    BeaconLocation location = new BeaconLocation(block.getX(), block.getY(), block.getZ(), block.getWorld().getName());
                    if(player.hasPermission("simplebeacons.place")) {
                        beaconHandler.addBeacon(new BeaconData(level, "REGENERATION", player.getUniqueId(), location, new ArrayList<>(),new ArrayList<>()));
                    }else {
                        player.sendMessage(messagesHandler.getMessage("beacons.cantplace"));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
