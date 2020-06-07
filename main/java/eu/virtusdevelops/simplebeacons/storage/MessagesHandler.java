package eu.virtusdevelops.simplebeacons.storage;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.utils.TextFormater;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class MessagesHandler {
    private MessagesData messagesData;
    private SimpleBeacons simpleBeacons;
    private boolean debug = false;

    public MessagesHandler(MessagesData messagesData, SimpleBeacons simpleBeacons){
        this.simpleBeacons = simpleBeacons;
        this.messagesData = messagesData;
        debug = simpleBeacons.getConfig().getBoolean("debug");
    }

    public void reload(){
        debug = simpleBeacons.getConfig().getBoolean("debug");
    }

    public String getMessage(String path){
        String message = messagesData.getMessages().getString(path);
        if(message == null){
            return path;
        }
        return TextFormater.sFormatText(message);
    }

    public String getRawMessage(String path){
        return messagesData.getMessages().getString(path);
    }

    public List<String> getList(String path){
        return TextFormater.colorFormatList(getRawList(path));
    }

    public List<String> formatList(List<String> list, String replace, String value){
        List<String> newList = new ArrayList<>();
        for(String st : list){
            st = st.replace(replace, value);
            newList.add(TextFormater.sFormatText(st));
        }
        return newList;
    }

    public List<String> formatList(List<String> list, String[] replaces, String[] values){
        List<String> newList = new ArrayList<>();
        for(String st : list){
            for(int i = 0; i < replaces.length; i++){
                st = st.replace(replaces[i], values[i]);
            }
            newList.add(TextFormater.sFormatText(st));
        }
        return newList;
    }

    public List<String> formatList(List<String> list, String... replacements){
        List<String> newList = new ArrayList<>();
        for(String st : list){
            for(String replace : replacements ) {
                String[] data = replace.split(":");
                st = st.replace(data[0], data[1]);
            }
            newList.add(TextFormater.sFormatText(st));
        }
        return newList;
    }

    public String formatString(String name, String... replacements){
        for(String replace : replacements ) {
            String[] data = replace.split(":");
            name = name.replace(data[0], data[1]);
        }
        return name;
    }

    public ItemStack getItem(ItemStack item, String itemData, String... replacements){
        ItemMeta meta = item.getItemMeta();
        List<String> lore = getRawList(itemData + ".lore");
        lore = formatList(lore, replacements);
        String name = getMessage(itemData + ".name");
        if(replacements.length > 0) {
            name = formatString(name, replacements);
        }
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public String formatString(String text, String[] replaces, String[] values){
        for(int i = 0; i < replaces.length; i++){
            try {
                text = text.replace(replaces[i], values[i]);
            }catch (Exception ignored){}
        }
        return TextFormater.sFormatText(text);
    }

    public List<String> getRawList(String path){
        List<String> list = messagesData.getMessages().getStringList(path);
        if(list.isEmpty()){
            list.add(path);
        }
        return list;
    }

    public void debug(String message, String... replacements){
        if(debug) {
            for (String string : replacements) {
                String[] data = string.split(":");
                if (data.length > 1) {
                    message = message.replace(data[0], data[1]);
                }
            }
            Bukkit.getConsoleSender().sendMessage(TextFormater.sFormatText(message));
        }
    }

}
