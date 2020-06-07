package eu.virtusdevelops.simplebeacons.managers.modules;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.utils.BlockUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.entity.Monster;

import java.util.Arrays;
import java.util.Collection;

public class ProtectModul extends Modul{

    @Override
    public void run(BeaconData beaconData, int tickRate, SimpleBeacons simpleBeacons) {
        Location loc = new Location(Bukkit.getWorld(beaconData.beaconLocation.world), beaconData.beaconLocation.x, beaconData.beaconLocation.y, beaconData.beaconLocation.z);
        Block block = loc.getBlock();
        if (beaconData.isChunkLoaded()) {
            Beacon beacon = (Beacon) block.getState();
            if (beacon.getTier() > 0) {
                final Collection<Chunk> chunkList = BlockUtil.aroundChunks(loc.getChunk(), 1);
                chunkList.stream().filter(Chunk::isLoaded).map(Chunk::getEntities).filter(entities -> entities.length > 0)
                        .forEach(entities -> Arrays.stream(entities).filter(entity -> entity instanceof Monster).forEach(entity -> {
                            Monster monster = (Monster) entity;
                            monster.damage(beacon.getTier()*beaconData.level);

                        }));
            }
        }


        super.run(beaconData, tickRate, simpleBeacons);
    }
}
