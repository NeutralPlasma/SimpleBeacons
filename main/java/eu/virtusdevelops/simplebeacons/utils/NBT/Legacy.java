package eu.virtusdevelops.simplebeacons.utils.NBT;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import eu.virtusdevelops.simplebeacons.utils.TextFormater;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

public class Legacy {
    private SimpleBeacons simpleBeacons;
    private MessagesHandler messagesHandler;

    public Legacy(SimpleBeacons simpleBeacons, MessagesHandler messagesHandler){
        this.simpleBeacons = simpleBeacons;
        this.messagesHandler = messagesHandler;
    }
    public Integer getInt(ItemStack item, String dataContainer){

        NamespacedKey key = new NamespacedKey(simpleBeacons, dataContainer);
        ItemMeta itemMeta = item.getItemMeta();
        CustomItemTagContainer tagContainer = itemMeta.getCustomTagContainer();
        if(tagContainer.hasCustomTag(key, ItemTagType.INTEGER)) {
            int foundValue = tagContainer.getCustomTag(key, ItemTagType.INTEGER);
            return foundValue;
        }
        return 0;
    }

    public String getString(ItemStack item, String dataContainer){
        NamespacedKey key = new NamespacedKey(simpleBeacons, dataContainer);
        ItemMeta itemMeta = item.getItemMeta();
        CustomItemTagContainer tagContainer = itemMeta.getCustomTagContainer();
        if(tagContainer.hasCustomTag(key, ItemTagType.STRING)) {
            String foundValue = tagContainer.getCustomTag(key, ItemTagType.STRING);
            return foundValue;
        }
        return "none";
    }

    public ItemMeta setInt(ItemMeta meta, int number, String dataContainer){
        NamespacedKey key = new NamespacedKey(simpleBeacons, dataContainer);
        try {
            CustomItemTagContainer tagContainer = meta.getCustomTagContainer();
            tagContainer.setCustomTag(key, ItemTagType.INTEGER, number);
        }catch (NullPointerException error){
            error.getCause();
            error.fillInStackTrace();
        }
        return meta;
    }

    public ItemMeta setString(ItemMeta meta, String string, String dataContainer){
        NamespacedKey key = new NamespacedKey(simpleBeacons, dataContainer);
        try {
            CustomItemTagContainer tagContainer = meta.getCustomTagContainer();
            tagContainer.setCustomTag(key, ItemTagType.STRING, string);
        }catch (NullPointerException error){
            error.getCause();
            error.fillInStackTrace();
        }
        return meta;
    }


    public ItemStack createBeacon(ItemStack item, int level){
        try {
            ItemMeta meta = item.getItemMeta();
            meta = setInt(meta, level, "level");
            meta.setDisplayName(TextFormater.sFormatText(simpleBeacons.getConfig().getString("beacons." + level + ".name")));

            meta.setLore(messagesHandler.formatList(
                    simpleBeacons.getConfig().getStringList("beacons." + level + ".lore"),
                    new String[]{"{level}"}, new String[]{String.valueOf(level)}
            ));
            item.setItemMeta(meta);
            return item;
        }catch (NullPointerException error){
            error.getCause();
            return null;
        }
    }
}
