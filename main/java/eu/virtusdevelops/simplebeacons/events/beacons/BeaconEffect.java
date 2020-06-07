package eu.virtusdevelops.simplebeacons.events.beacons;

import com.destroystokyo.paper.event.block.BeaconEffectEvent;
import org.bukkit.block.Beacon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BeaconEffect implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true) // To be done.
    public void onEffect(BeaconEffectEvent event){
        Beacon beacon = (Beacon) event.getBlock();
        //event.getBlock().getLocation().getWorld().isChunkLoaded(event.getBlock().getX() >> 4, event.getBlock().getZ() >> 4); // Usefull for non papermc stuff
    }
}
