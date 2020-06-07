package eu.virtusdevelops.simplebeacons.managers.modules;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.managers.BlockData;
import eu.virtusdevelops.simplebeacons.managers.BlockUpdater;
import eu.virtusdevelops.simplebeacons.utils.BlockUtil;
import org.bukkit.*;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;

import java.util.*;

public class FarmModul extends Modul{
    private final SplittableRandom random = new SplittableRandom();

    @Override
    public void run(BeaconData beaconData, int tickrate, SimpleBeacons simpleBeacons) {

        Location loc = new Location(Bukkit.getWorld(beaconData.beaconLocation.world), beaconData.beaconLocation.x, beaconData.beaconLocation.y, beaconData.beaconLocation.z);
        Block block = loc.getBlock();
        if (beaconData.isChunkLoaded()) {
            Beacon beacon = (Beacon) block.getState();
            if (beacon.getTier() > 0) {
                int range = 10 + beacon.getTier() * 10;

                final Collection<Chunk> chunkList = BlockUtil.aroundChunks(loc.getChunk(), 1);
                new BlockUpdater(simpleBeacons, new ArrayList<>(chunkList), loc, range);


            }
        }

        super.run(beaconData, tickrate, simpleBeacons);
    }

}
