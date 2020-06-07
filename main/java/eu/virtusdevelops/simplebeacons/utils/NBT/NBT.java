package eu.virtusdevelops.simplebeacons.utils.NBT;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NBT {
    private SimpleBeacons simpleBeacons;
    private Legacy legacy;
    private Current current;
    private boolean useLegacy = false;


    public NBT(SimpleBeacons simpleBeacons, Legacy legacy, Current current) {
        this.simpleBeacons = simpleBeacons;
        this.legacy = legacy;
        this.current = current;


        final String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        if (version.contains("v1_13")) {
            this.useLegacy = true;
        }
    }

    public Integer getInt(ItemStack item, String dataContainer){
        if(useLegacy){
            return legacy.getInt(item, dataContainer);
        }else{
            return current.getInt(item, dataContainer);
        }
    }

    public String getString(ItemStack item, String dataContainer){
        if(useLegacy){
            return legacy.getString(item, dataContainer);
        }else{
            return current.getString(item, dataContainer);
        }
    }

    public ItemMeta setInt(ItemStack item, String dataContainer, int value){
        if(useLegacy){
            return legacy.setInt(item.getItemMeta(),value, dataContainer);
        }else{
            return current.setInt(item.getItemMeta(),value, dataContainer);
        }
    }


    public ItemStack createBeacon(ItemStack item, int level){
        if (useLegacy){
            return legacy.createBeacon(item, level);
        }else{
            return current.createBeacon(item, level);
        }
    }
}
