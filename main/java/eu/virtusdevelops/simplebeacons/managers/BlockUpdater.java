package eu.virtusdevelops.simplebeacons.managers;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Directional;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.SplittableRandom;

public class BlockUpdater extends BukkitRunnable {

    private List<Chunk> data;
    private int counter = 0;
    private Location location;
    private final SplittableRandom random = new SplittableRandom();


    int minY;
    int maxY;

    public BlockUpdater(SimpleBeacons simpleBeacons, List<Chunk> data, Location location, int range){
        this.data = data;
        this.location = location;

        minY = location.getBlockY();
        maxY = Math.min(minY + range, location.getWorld().getMaxHeight()-1);

        this.runTaskTimer(simpleBeacons, 0L, 15L);

    }

    @Override
    public void run() {
        if(counter >= data.size()){
            this.cancel();
            return;
        }
        Chunk chunk = data.get(counter);
        for(int x = 0; x < 16; x++){
            for(int y = minY; y < maxY; y++){
                for(int z = 0; z < 16; z++){
                    Block block = chunk.getBlock(x,y,z);
                    if(block.getBlockData() instanceof Ageable && random.nextInt(100) > 70 ){
                        Ageable bData = (Ageable) block.getBlockData();
                        if(bData.getAge() != bData.getMaximumAge()){
                            bData.setAge(bData.getAge() + 1);
                            block.setBlockData(bData);
                            location.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 0, 10);
                        }else{
                            grow(block);
                        }
                    }
                }
            }
        }
        counter++;
    }

    public void grow(Block block){
        if(block.getType() == Material.PUMPKIN_STEM){
            switch (random.nextInt(4)) {
                case 1 -> {
                    Block west = block.getRelative(BlockFace.WEST);
                    if (west.getType() == Material.AIR && west.getRelative(BlockFace.DOWN).getType() == Material.DIRT) {
                        west.setType(Material.PUMPKIN);
                        block.setType(Material.ATTACHED_PUMPKIN_STEM);
                        Directional data = (Directional) block.getBlockData();
                        data.setFacing(BlockFace.WEST);
                        block.setBlockData(data);
                        location.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 0, 10);
                    }
                }
                case 2 -> {
                    Block east = block.getRelative(BlockFace.EAST);
                    if (east.getType() == Material.AIR && east.getRelative(BlockFace.DOWN).getType() == Material.DIRT) {
                        east.setType(Material.PUMPKIN);
                        block.setType(Material.ATTACHED_PUMPKIN_STEM);
                        Directional data = (Directional) block.getBlockData();
                        data.setFacing(BlockFace.EAST);
                        block.setBlockData(data);
                        location.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 0, 10);
                    }
                }
                case 3 -> {
                    Block north = block.getRelative(BlockFace.NORTH);
                    if (north.getType() == Material.AIR && north.getRelative(BlockFace.DOWN).getType() == Material.DIRT) {
                        north.setType(Material.PUMPKIN);
                        block.setType(Material.ATTACHED_PUMPKIN_STEM);
                        Directional data = (Directional) block.getBlockData();
                        data.setFacing(BlockFace.NORTH);
                        block.setBlockData(data);
                        location.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 0, 10);
                    }
                }
                default -> {
                    Block south = block.getRelative(BlockFace.SOUTH);
                    if (south.getType() == Material.AIR && south.getRelative(BlockFace.DOWN).getType() == Material.DIRT) {
                        south.setType(Material.PUMPKIN);
                        block.setType(Material.ATTACHED_PUMPKIN_STEM);
                        Directional data = (Directional) block.getBlockData();
                        data.setFacing(BlockFace.SOUTH);
                        block.setBlockData(data);
                        location.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 0, 10);
                    }
                }
            }
        }else if(block.getType() == Material.MELON_STEM){
            switch (random.nextInt(4)) {
                case 1 -> {
                    Block west = block.getRelative(BlockFace.WEST);
                    if (west.getType() == Material.AIR && west.getRelative(BlockFace.DOWN).getType() == Material.DIRT) {
                        west.setType(Material.MELON);
                        block.setType(Material.ATTACHED_MELON_STEM);
                        Directional data = (Directional) block.getBlockData();
                        data.setFacing(BlockFace.WEST);
                        block.setBlockData(data);
                        location.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 0, 10);
                    }
                }
                case 2 -> {
                    Block east = block.getRelative(BlockFace.EAST);
                    if (east.getType() == Material.AIR && east.getRelative(BlockFace.DOWN).getType() == Material.DIRT) {
                        east.setType(Material.MELON);
                        block.setType(Material.ATTACHED_MELON_STEM);
                        Directional data = (Directional) block.getBlockData();
                        data.setFacing(BlockFace.EAST);
                        block.setBlockData(data);
                        location.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 0, 10);
                    }
                }
                case 3 -> {
                    Block north = block.getRelative(BlockFace.NORTH);
                    if (north.getType() == Material.AIR && north.getRelative(BlockFace.DOWN).getType() == Material.DIRT) {
                        north.setType(Material.MELON);
                        block.setType(Material.ATTACHED_MELON_STEM);
                        Directional data = (Directional) block.getBlockData();
                        data.setFacing(BlockFace.NORTH);
                        block.setBlockData(data);
                        location.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 0, 10);
                    }
                }
                default -> {
                    Block south = block.getRelative(BlockFace.SOUTH);
                    if (south.getType() == Material.AIR && south.getRelative(BlockFace.DOWN).getType() == Material.DIRT) {
                        south.setType(Material.MELON);
                        block.setType(Material.ATTACHED_MELON_STEM);
                        Directional data = (Directional) block.getBlockData();
                        data.setFacing(BlockFace.SOUTH);
                        block.setBlockData(data);
                        location.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 0, 10);
                    }
                }
            }
        }


        // to dalje naredi da bo bol kul
    }
}
