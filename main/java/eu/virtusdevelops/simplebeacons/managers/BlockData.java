package eu.virtusdevelops.simplebeacons.managers;

import org.bukkit.block.Block;

public class BlockData {
    private org.bukkit.block.data.BlockData blockData;
    private int x;
    private int y;
    private int z;
    public BlockData(org.bukkit.block.data.BlockData blockData, int x, int y, int z){
        this.blockData = blockData;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public org.bukkit.block.data.BlockData getBlockData() {
        return blockData;
    }
    public void setBlockData(org.bukkit.block.data.BlockData data){
        this.blockData = data;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }
}
