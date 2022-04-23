package eu.virtusdevelops.simplebeacons.managers.modules;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public abstract class Modul {


    public void run(BeaconData beaconData, int tickRate, SimpleBeacons simpleBeacons){ }

    boolean isFull(DoubleChest chest, ItemStack item) {
        for(ItemStack slot : chest.getInventory()){
            if(slot == null) return false;
            if(slot.isSimilar(item)){
                if(slot.getAmount() + item.getAmount() < slot.getMaxStackSize()){
                    return false;
                }
            }
        }
        return true;
    }

    boolean isFull(PlayerInventory inventory, ItemStack item) {
        for(ItemStack slot : inventory){
            if(slot == null) return false;
            if(slot.isSimilar(item)){
                if(slot.getAmount() + item.getAmount() < slot.getMaxStackSize()){
                    return false;
                }
            }
        }
        return true;
    }

    boolean isFull(Chest chest, ItemStack item) {
        for(ItemStack slot : chest.getInventory()){
            if(slot == null) return false;
            if(slot.isSimilar(item)){
                if(slot.getAmount() + item.getAmount() < slot.getMaxStackSize()){
                    return false;
                }
            }
        }
        return true;
    }

}
