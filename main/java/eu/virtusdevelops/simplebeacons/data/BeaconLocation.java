package eu.virtusdevelops.simplebeacons.data;


import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.Collection;

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
        return Math.pow(x-beaconLocation.x, 2) + Math.pow(y-beaconLocation.y, 2) + Math.pow(z-beaconLocation.z, 2);
    }

    public double getDistance(Location loc){
        return Math.pow(x-loc.getBlockX(), 2) + Math.pow(y-loc.getBlockY(), 2) + Math.pow(z-loc.getBlockZ(), 2);
    }

    public Location getBukkitLocation(){
        return new Location(getWorld() , x, y, z);
    }


    public Collection<Entity> getNearbyEntities(double x1, double y1, double z1) {
        World world = this.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location has no world");
        }
        return world.getNearbyEntities(getBukkitLocation(), x1, y1, z1);
    }


    public World getWorld() {
        if (this.world == null) {
            return null;
        }
        World world = Bukkit.getWorld(this.world);
        if(world == null) return null;
        return world;
    }


    @Override
    public String toString(){
        return world + ":" + x + ":" + y + ":" + z;
    }

    public static BeaconLocation fromString(String string){
        String[] split = string.split(":");
        return new BeaconLocation(
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2]),
                Integer.parseInt(split[3]),
                split[0]
        );

    }
}
