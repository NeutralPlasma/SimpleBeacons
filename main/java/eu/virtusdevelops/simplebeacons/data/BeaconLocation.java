package eu.virtusdevelops.simplebeacons.data;


import org.bukkit.Location;

public class BeaconLocation {
    public int x;
    public int y;
    public int z;
    public String world;

    public BeaconLocation(int x, int y, int z, String world){
        this.x = x; this.y = y; this.z = z;
        this.world = world;
    }

    public double getDistance(BeaconLocation beaconLocation){
        return Math.sqrt(Math.pow(x-beaconLocation.x, 2) + Math.pow(y-beaconLocation.y, 2) + Math.pow(z-beaconLocation.z, 2));
    }

    public double getDistance(Location loc){
        return Math.sqrt(Math.pow(x-loc.getBlockX(), 2) + Math.pow(y-loc.getBlockY(), 2) + Math.pow(z-loc.getBlockZ(), 2));
    }

    @Override
    public String toString(){
        return world + ":" + x + ":" + y + ":" + z;
    }
}
