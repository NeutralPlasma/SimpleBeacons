package eu.virtusdevelops.simplebeacons.managers.modules;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.managers.BlockUpdater;
import eu.virtusdevelops.simplebeacons.utils.BlockUtil;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SplittableRandom;

public class HarvestModul extends Modul{
    private final SplittableRandom random = new SplittableRandom();

    @Override
    public void run(BeaconData beaconData, int tickrate, SimpleBeacons simpleBeacons) {

        Location loc = beaconData.getBeaconLocation().getBukkitLocation();
        Block block = loc.getBlock();
        if (beaconData.isChunkLoaded()) {
            Beacon beacon = (Beacon) block.getState();
            if (beacon.getTier() > 0) {
                int range = 10 + beacon.getTier() * 10;
                final Collection<Chunk> chunkList = BlockUtil.aroundChunks(loc.getChunk(), 1);
                new Updater(simpleBeacons, new ArrayList<>(chunkList), beaconData, range, loc);

            }
        }

        super.run(beaconData, tickrate, simpleBeacons);
    }

}


class Updater extends BukkitRunnable {
    int counter = 0;
    List<Chunk> chunks;
    BeaconData data;
    int range, minY, maxY;
    public Updater(SimpleBeacons plugin, List<Chunk> chunks, BeaconData data, int range, Location location){
        this.chunks = chunks;
        this.data = data;
        this.range = range;

        minY = location.getBlockY();
        maxY = Math.min(minY + range, location.getWorld().getMaxHeight()-1);

        this.runTaskTimer(plugin, 0L, 15L);
    }

    @Override
    public void run() {
        if(counter >= chunks.size()){
            this.cancel();
            return;
        }


        Chunk chunk = chunks.get(counter);
        for(int x = 0; x < 16; x++){
            for(int y = minY; y < maxY; y++){
                for(int z = 0; z < 16; z++){
                    Block block = chunk.getBlock(x,y,z);
                    if(block.getBlockData() instanceof Ageable){
                        Ageable bData = (Ageable) block.getBlockData();

                        if(bData.getAge() == bData.getMaximumAge()){
                            bData.setAge(0);
                            block.setBlockData(bData);
                            //location.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 0, 10);
                        }


                    }
                }
            }
        }
        counter++;
    }
}
