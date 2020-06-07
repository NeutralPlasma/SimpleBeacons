package eu.virtusdevelops.simplebeacons.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.*;

public class BlockUtil {



    public static List<Block> getSquare(Block start, int radius){
        if (radius < 0) {
            return new ArrayList<>(0);
        }
        int iterations = (radius * 2) + 1;
        List<Block> blocks = new ArrayList<>(iterations * iterations * iterations);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks.add(start.getRelative(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static Collection<Chunk> aroundChunks(Chunk origin, int radius) {
        World world = origin.getWorld();

        int length = (radius * 2) + 1;
        Set<Chunk> chunks = new HashSet<>(length * length);

        int cX = origin.getX();
        int cZ = origin.getZ();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                chunks.add(world.getChunkAt(cX + x, cZ + z));
            }
        }
        return chunks;
    }

    public static List<Block> getFlatSquare(Block start, int radius){
        if (radius < 0) {
            return new ArrayList<>(0);
        }
        int iterations = (radius * 2) + 1;
        List<Block> blocks = new ArrayList<>(iterations * iterations);
        int y = start.getY();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                blocks.add(start.getRelative(x, y, z));
            }

        }
        return blocks;
    }

    private static List<Block> getBlocks(final Location base, final int changeX, final int changeY, final int changeZ) {
        final List<Block> blocks = new ArrayList<>();
        for (int x = base.getBlockX() - changeX; x <= base.getBlockX() + changeX; ++x) {
            for (int y = base.getBlockY() - changeY; y <= base.getBlockY() + changeY; ++y) {
                for (int z = base.getBlockZ() - changeZ; z <= base.getBlockZ() + changeZ; ++z) {
                    final Location loc = new Location(base.getWorld(), (double)x, (double)y, (double)z);
                    final Block b = loc.getBlock();
                    if (!b.getType().equals(Material.AIR) && b != null) {
                        blocks.add(b);
                    }
                }
            }
        }
        return blocks;
    }

    public static Block getLocation(BlockFace face, Block block){

        Location nextLocation = block.getLocation();

        switch (face) {
            case SOUTH :
                nextLocation.setZ(nextLocation.getZ() + 1);
                break;
            case UP:
                nextLocation.setY(nextLocation.getY() + 1);
                break;
            case DOWN:
                nextLocation.setY(nextLocation.getY() - 1);
                break;
            case EAST:
                nextLocation.setX(nextLocation.getX() + 1);
                break;
            case WEST:
                nextLocation.setX(nextLocation.getX() - 1);
                break;
            case NORTH:
                nextLocation.setZ(nextLocation.getZ() - 1);
                break;
        }

        return block.getWorld().getBlockAt(nextLocation);
    }
}
