package eu.virtusdevelops.simplebeacons.events.beacons;

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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BeaconBreak implements Listener {
    private MessagesHandler messagesHandler;
    private BeaconHandler beaconHandler;
    private NBT nbt;

    public BeaconBreak(MessagesHandler messagesHandler, BeaconHandler beaconHandler, NBT nbt){
        this.messagesHandler = messagesHandler;
        this.beaconHandler = beaconHandler;
        this.nbt = nbt;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBeaconBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(block.getType() == Material.BEACON){
            BeaconLocation location = new BeaconLocation(block.getX(), block.getY(), block.getZ(), block.getWorld().getName());
            BeaconData beaconData = beaconHandler.getBeacon(location);
            if(beaconData != null){
                if(player.hasPermission("simplebeacons.break")){
                    beaconHandler.removeBeacon(location);

                    block.getLocation().getWorld().dropItemNaturally(block.getLocation(), nbt.createBeacon(new ItemStack(Material.BEACON), beaconData.level));
                }else{
                    player.sendMessage(messagesHandler.getMessage("beacons.cantbreak"));
                    event.setCancelled(true);
                }
            }
        }
    }
}
