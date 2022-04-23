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
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OreModul extends Modul{
    private static final int CHUNK_SIZE  = 16;
    private static final int CHUNK_SIZE_MIN  = 0;
    private static final Collection<Material> materials = new ArrayList<>(Arrays.asList(Material.NETHER_QUARTZ_ORE,
            Material.REDSTONE_ORE, Material.COAL_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE,
            Material.GOLD_ORE, Material.IRON_ORE, Material.LAPIS_ORE, Material.DEEPSLATE_IRON_ORE, Material.DEEPSLATE_COAL_ORE,
            Material.DEEPSLATE_GOLD_ORE, Material.DEEPSLATE_COPPER_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_EMERALD_ORE,
            Material.COPPER_ORE, Material.DEEPSLATE_REDSTONE_ORE));


    private ItemStack tool = new ItemStack(Material.DIAMOND_PICKAXE);

    public void run(BeaconData beaconData, int tickrate, SimpleBeacons simpleBeacons) {

        Location loc = beaconData.getBeaconLocation().getBukkitLocation();
        Block block = loc.getBlock();
        if (beaconData.isChunkLoaded()) {


            Beacon beacon = (Beacon) block.getState();

            if (beacon.getTier() > 0) {


                final int minY = beaconData.getHeight();
                final int maxY = minY+1;
                beaconData.setHeight(maxY);

                final Collection<Chunk> chunkList = BlockUtil.aroundChunks(loc.getChunk(), 1);
                List<Block> blocks = IntStream.range(CHUNK_SIZE_MIN, CHUNK_SIZE).boxed()
                        .flatMap(x -> IntStream.range(minY, maxY).boxed()
                                .flatMap(y -> IntStream.range(CHUNK_SIZE_MIN, CHUNK_SIZE).boxed()
                                        .flatMap(z -> chunkList.stream().map(chunk -> chunk.getBlock(x,y,z)))))
                        .filter(blockData -> materials.contains(blockData.getType()))
                        .collect(Collectors.toList());
                //Bukkit.getConsoleSender().sendMessage("Scanning area: " + height + " found: " + blocks.size());

                blocks.forEach(ore -> {

                    Location location = ore.getLocation();
                    for (BeaconLocation data : beaconData.getLinkedLocations()) {
                        Block c = data.getBukkitLocation().getBlock();
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
