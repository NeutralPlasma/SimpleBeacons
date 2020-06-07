package eu.virtusdevelops.simplebeacons.managers.modules;

import com.sun.scenario.effect.impl.state.AccessHelper;
import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.managers.BlockData;
import eu.virtusdevelops.simplebeacons.utils.BlockUtil;
import org.bukkit.*;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.Ageable;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OreModul extends Modul{
    private final SplittableRandom random = new SplittableRandom();
    private static final int CHUNK_SIZE  = 16;
    private static final int CHUNK_SIZE_MIN  = 0;
    private static final Collection<Material> materials = new ArrayList<>(Arrays.asList(Material.NETHER_QUARTZ_ORE,
            Material.REDSTONE_ORE, Material.COAL_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE,
            Material.GOLD_ORE, Material.IRON_ORE, Material.LAPIS_ORE));


    public void run(BeaconData beaconData, int tickrate, SimpleBeacons simpleBeacons, int height) {
        Location loc = new Location(Bukkit.getWorld(beaconData.beaconLocation.world), beaconData.beaconLocation.x, beaconData.beaconLocation.y, beaconData.beaconLocation.z);
        Block block = loc.getBlock();
        if (beaconData.isChunkLoaded()) {
            Beacon beacon = (Beacon) block.getState();
            if (beacon.getTier() > 0) {
                ItemStack tool = new ItemStack(Material.DIAMOND_PICKAXE);

                final int minY = height;
                final int maxY = height+1;

                final Collection<Chunk> chunkList = BlockUtil.aroundChunks(loc.getChunk(), 1);
                List<Block> blocks = IntStream.range(CHUNK_SIZE_MIN, CHUNK_SIZE).boxed()
                        .flatMap(x -> IntStream.range(minY, maxY).boxed()
                                .flatMap(y -> IntStream.range(CHUNK_SIZE_MIN, CHUNK_SIZE).boxed()
                                        .flatMap(z -> chunkList.stream().map(chunk -> chunk.getBlock(x,y,z)))))
                        .filter(blockData -> materials.contains(blockData.getType()))
                        .collect(Collectors.toList());
                Bukkit.getConsoleSender().sendMessage("Scanning area: " + height + " found: " + blocks.size());

                blocks.forEach(ore -> {
                    Location location = ore.getLocation();
                    for (String data : beaconData.linkedLocations) {
                        String[] info = data.split(":");
                        Block c = location.getWorld().getBlockAt(Integer.valueOf(info[1]), Integer.valueOf(info[2]), Integer.valueOf(info[3]));
                        if(c.getType() == Material.CHEST) {
                            Chest chest = (Chest) c.getState();
                            boolean added = false;
                            if (chest instanceof DoubleChest) {
                                DoubleChest dchest = (DoubleChest) chest;
                                boolean addedl = false;
                                for (ItemStack item : ore.getDrops(tool)) {
                                    if (!isFull(dchest, item)) {
                                        dchest.getInventory().addItem(item);
                                        ore.setType(Material.STONE);
                                        addedl = true;
                                        added = true;
                                    } else {
                                        added = false;
                                    }

                                }
                                if (addedl) {
                                    ore.setType(Material.STONE);
                                }
                            } else {
                                boolean addedl = false;
                                for (ItemStack item : ore.getDrops(tool)) {
                                    if (!isFull(chest, item)) {
                                        chest.getInventory().addItem(item);
                                        addedl = true;
                                        added = true;
                                    } else {
                                        added = false;
                                    }
                                }
                                if (addedl) {
                                    ore.setType(Material.STONE);
                                }
                            }
                            if (added) {
                                break;
                            }
                        }
                    }

                });
            }
        }
        super.run(beaconData, tickrate, simpleBeacons);
    }
}
