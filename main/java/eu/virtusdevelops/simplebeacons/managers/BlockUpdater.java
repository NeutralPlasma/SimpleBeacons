package eu.virtusdevelops.simplebeacons.managers;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.utils.ListUtil;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.List;
import java.util.SplittableRandom;
import java.util.stream.IntStream;

public class BlockUpdater extends BukkitRunnable {

    private List<Chunk> data;
    private int counter = 0;
    private Location location;
    private final SplittableRandom random = new SplittableRandom();

    private static final int CHUNK_SIZE  = 16;
    private static final int CHUNK_SIZE_MIN  = 0;

    int minY;
    int maxY;

    public BlockUpdater(SimpleBeacons simpleBeacons, List<Chunk> data, Location location, int range){
        this.data = data;
        this.location = location;

        minY = location.getBlockY();
        maxY = Math.min(minY + range, 255);

        this.runTaskTimer(simpleBeacons, 0L, 15L);

    }

    @Override
    public void run() {
        if(counter >= data.size()){
            this.cancel();
            return;
        }
        Chunk chunk = data.get(counter);
        for(int x = CHUNK_SIZE_MIN; x < CHUNK_SIZE; x++){
            for(int y = minY; y < maxY; y++){
                for(int z = CHUNK_SIZE_MIN; z < CHUNK_SIZE; z++){
                    Block block = chunk.getBlock(x,y,z);

                    if(block.getBlockData() instanceof Ageable && random.nextInt(100) > 70 ){
                        Ageable bData = (Ageable) block.getBlockData();
                        bData.setAge(Math.min(bData.getAge() + 1, bData.getMaximumAge()));
                        block.setBlockData(bData);
                        location.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 0, 15);
                    }
                }
            }
        }
        counter++;
    }
}
