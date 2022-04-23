package eu.virtusdevelops.simplebeacons.data;

import eu.virtusdevelops.simplebeacons.managers.Module;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.List;
import java.util.UUID;

public class BeaconData {
    private int id;
    private int level;
    private String selectedEffect;
    private UUID placedBy;
    private BeaconLocation beaconLocation;
    private List<Module> enabledModules;
    private List<BeaconLocation> linkedLocations;
    private int height = 0;

    public BeaconData(int level, String selectedEffect, UUID placedBy, BeaconLocation location, List<Module> enabledModules, List<BeaconLocation> linkedLocations, int id){
        this.level = level;
        this.selectedEffect = selectedEffect;
        this.placedBy = placedBy;
        this.beaconLocation = location;
        this.enabledModules = enabledModules;
        this.id = id;
        this.linkedLocations = linkedLocations;


        this.height = 0;
    }

    public void init(){
        // late init to wait for worlds to load :)
        this.height = beaconLocation.getBukkitLocation().getWorld().getMinHeight()+1;
    }

    public boolean isChunkLoaded(){
        Location loc = beaconLocation.getBukkitLocation();
        Block block = loc.getBlock();
        if (block.getWorld().isChunkLoaded(beaconLocation.x >> 4, beaconLocation.z >> 4)) {
            return block.getType() == Material.BEACON;
        }
        return false;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if(height >= beaconLocation.y){
            this.height = 0;
        }else {
            this.height = height;
        }
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public BeaconLocation getBeaconLocation() {
        return beaconLocation;
    }

    public List<Module> getEnabledModules() {
        return enabledModules;
    }

    public List<BeaconLocation> getLinkedLocations() {
        return linkedLocations;
    }

    public String getSelectedEffect() {
        return selectedEffect;
    }

    public UUID getPlacedBy() {
        return placedBy;
    }

    public int getId() {
        return id;
    }


    public void setBeaconLocation(BeaconLocation beaconLocation) {
        this.beaconLocation = beaconLocation;
    }

    public void setEnabledModules(List<Module> enabledModules) {
        this.enabledModules = enabledModules;
    }

    public void setLinkedLocations(List<BeaconLocation> linkedLocations) {
        this.linkedLocations = linkedLocations;
    }

    public void setPlacedBy(UUID placedBy) {
        this.placedBy = placedBy;
    }

    public void setSelectedEffect(String selectedEffect) {
        this.selectedEffect = selectedEffect;
    }

    public void addLinkedLocation(BeaconLocation loc){
        linkedLocations.add(loc);
    }

    public void addEnabledModule(Module module){
        enabledModules.add(module);
    }
    public void removeEnabledModule(Module module){
        enabledModules.remove(module);
    }

    public void setId(int id) {
        this.id = id;
    }
}
