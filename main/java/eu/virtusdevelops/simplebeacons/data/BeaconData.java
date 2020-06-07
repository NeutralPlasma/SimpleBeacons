package eu.virtusdevelops.simplebeacons.data;

import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.List;
import java.util.UUID;

public class BeaconData {
    public int level;
    public String selectedEffect;
    public UUID placedBy;
    public BeaconLocation beaconLocation;
    public List<String> enabledModules;
    public List<String> linkedLocations;

    public BeaconData(int level, String selectedEffect, UUID placedBy, BeaconLocation location, List<String> enabledModules, List<String> linkedLocations){
        this.level = level;
        this.selectedEffect = selectedEffect;
        this.placedBy = placedBy;
        this.beaconLocation = location;
        this.enabledModules = enabledModules;
        this.linkedLocations = linkedLocations;
    }

    public boolean isChunkLoaded(){
        Location loc = new Location(Bukkit.getWorld(beaconLocation.world), beaconLocation.x, beaconLocation.y, beaconLocation.z);
        Block block = loc.getBlock();
        if (block.getWorld().isChunkLoaded(beaconLocation.x >> 4, beaconLocation.z >> 4)) {
            return block.getType() == Material.BEACON;
        }
        return false;
    }


}
