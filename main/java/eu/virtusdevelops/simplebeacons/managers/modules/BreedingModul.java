package eu.virtusdevelops.simplebeacons.managers.modules;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.utils.BlockUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;

import java.util.Arrays;
import java.util.Collection;

public class BreedingModul extends Modul {
    @Override
    public void run(BeaconData beaconData, int tickRate, SimpleBeacons simpleBeacons) {
        Location loc = beaconData.getBeaconLocation().getBukkitLocation();
        Block block = loc.getBlock();
        if (beaconData.isChunkLoaded()) {
            Beacon beacon = (Beacon) block.getState();
            if (beacon.getTier() > 0) {
                final Collection<Chunk> chunkList = BlockUtil.aroundChunks(loc.getChunk(), 1);
                chunkList.stream().filter(Chunk::isLoaded).map(Chunk::getEntities).filter(entities -> entities.length > 0 && entities.length < 25)
                        .forEach(entities -> Arrays.stream(entities).filter(entity -> entity instanceof Animals).forEach(entity -> {
                            Animals animal = (Animals) entity;
                            if(animal.isAdult() && animal.canBreed()) {
                                animal.setLoveModeTicks(200);
                            }
                        }));

            }
        }
        super.run(beaconData, tickRate, simpleBeacons);
    }
}
