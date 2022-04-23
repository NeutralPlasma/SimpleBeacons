package eu.virtusdevelops.simplebeacons.storage;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.virtuscore.utils.HexUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessagesData {


    private FileConfiguration messagesconfiguration;
    private File messagesFile;
    private SimpleBeacons plugin;

    public MessagesData(SimpleBeacons plugin){
        this.plugin = plugin;
    }


    public void setup(){

        //creates plugin folder
        if(!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdir();
        }
        //---------------------

        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if(!messagesFile.exists()){
            try{
                messagesFile.createNewFile();
                messagesconfiguration = YamlConfiguration.loadConfiguration(messagesFile);
                plugin.saveResource("messages.yml", true);
                Bukkit.getConsoleSender().sendMessage(HexUtil.colorify("&aSuccessfully created messages.yml file!"));


            }catch (IOException e){
                Bukkit.getConsoleSender().sendMessage(HexUtil.colorify("&cFailed to create messages.yml file, Error: &f" + e.getMessage()));

            }

        }
        messagesconfiguration = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public FileConfiguration getMessages() {
        return messagesconfiguration;
    }


    public void saveMessages(){
        try{
            messagesconfiguration.save(messagesFile);
            Bukkit.getConsoleSender().sendMessage(HexUtil.colorify("&aSuccessfully saved messages.yml file."));
        }catch(IOException e){
            Bukkit.getConsoleSender().sendMessage(HexUtil.colorify("&cFailed to save messages.yml file, Error: &f" + e.getMessage()));
        }
    }

    public void reloadMessages() {
        messagesconfiguration = YamlConfiguration.loadConfiguration(messagesFile);
        Bukkit.getConsoleSender().sendMessage(HexUtil.colorify("&aReloaded messages.yml file."));
    }
}
