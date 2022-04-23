package eu.virtusdevelops.simplebeacons.managers.modules;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.data.BeaconLocation;
import eu.virtusdevelops.simplebeacons.utils.BlockUtil;
import org.bukkit.*;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;

import java.util.Arrays;
import java.util.Collection;

public class ItemModul extends Modul{

    @Override
    public void run(BeaconData beaconData, int tickRate, SimpleBeacons simpleBeacons) {
        Location loc = beaconData.getBeaconLocation().getBukkitLocation();
        Block block = loc.getBlock();
        if (beaconData.isChunkLoaded()) {
            Beacon beacon = (Beacon) block.getState();
            if (beacon.getTier() > 0) {
                final Collection<Chunk> chunkList = BlockUtil.aroundChunks(loc.getChunk(), 1);
                chunkList.stream().filter(Chunk::isLoaded).map(Chunk::getEntities).filter(entities -> entities.length > 0)
                        .forEach(entities -> Arrays.stream(entities).filter(entity -> entity instanceof Item).forEach(entity -> {
                            Item item = (Item) entity;
                            for (BeaconLocation data : beaconData.getLinkedLocations()) {
                                Block c = data.getBukkitLocation().getBlock();
                                if(c.getType() == Material.CHEST) {
                                    Chest chest = (Chest) c.getState();
                                    boolean added = false;
                                    if (chest instanceof DoubleChest) {
                                        DoubleChest doubleChest = (DoubleChest) chest;
                                        if (!isFull(doubleChest, item.getItemStack())) {
                                            item.remove();
                                            item.getLocation().getWorld().playEffect(item.getLocation(), Effect.MOBSPAWNER_FLAMES, 0, 1);
                                            doubleChest.getInventory().addItem(item.getItemStack());
                                            added = true;
                                        }
                                    } else {
                                        if (!isFull(chest, item.getItemStack())) {
                                            item.remove();
                                            item.getLocation().getWorld().playEffect(item.getLocation(), Effect.MOBSPAWNER_FLAMES, 0, 10);
                                            chest.getInventory().addItem(item.getItemStack());
                                            added = true;
                                        }
                                    }
                                    if (added) {
                                        break;
                                    }
                                }
                            }
                        }));
            }
        }
        super.run(beaconData, tickRate, simpleBeacons);
    }
}
