package eu.virtusdevelops.simplebeacons;

import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.events.beacons.BeaconBreak;
import eu.virtusdevelops.simplebeacons.events.beacons.BeaconInteract;
import eu.virtusdevelops.simplebeacons.events.beacons.BeaconPlace;
import eu.virtusdevelops.simplebeacons.managers.BeaconManager;
import eu.virtusdevelops.simplebeacons.managers.BeaconManagerHeavy;
import eu.virtusdevelops.simplebeacons.newCommands.AboutCommand;
import eu.virtusdevelops.simplebeacons.newCommands.GiveCommand;
import eu.virtusdevelops.simplebeacons.newCommands.ReloadCommand;
import eu.virtusdevelops.simplebeacons.storage.*;
import eu.virtusdevelops.simplebeacons.utils.NBT.Current;
import eu.virtusdevelops.simplebeacons.utils.NBT.Legacy;
import eu.virtusdevelops.simplebeacons.utils.NBT.NBT;
import eu.virtusdevelops.virtuscore.command.CommandManager;
import eu.virtusdevelops.virtuscore.gui.Handler;
import eu.virtusdevelops.virtuscore.managers.FileManager;
import eu.virtusdevelops.virtuscore.utils.FileLocation;
import eu.virtusdevelops.virtuscore.utils.HexUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class SimpleBeacons extends JavaPlugin {

    private SQLStorage storage;
    private MessagesHandler messagesHandler;
    private MessagesData messagesData;
    private Current current;
    private Legacy legacy;
    private NBT nbt;
    private BeaconHandler beaconHandler;
    private BeaconManager beaconManager;
    private BeaconManagerHeavy beaconManagerHeavy;
    private BeaconInteract beaconInteract;
    private FileManager fileManager;
    private Handler handler;

    private CommandManager commandManager;


    private static Economy econ = null;

    @Override
    public void onEnable() {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        long time = System.currentTimeMillis();
        String line = "&a=================================";
        String messageline1 = "&6Made by Virtus Develops";
        String messageline2 = "&SimpleBeacons: &6" + this.getDescription().getVersion();
        String messageline3 = "&7Running on: " + getServer().getVersion();
        String messageline4 = "&7Action: &aEnabling plugin&7...";

        console.sendMessage(HexUtil.colorify(line));
        console.sendMessage(HexUtil.colorify(messageline1));
        console.sendMessage(HexUtil.colorify(messageline2));
        console.sendMessage(HexUtil.colorify(messageline3));
        console.sendMessage(HexUtil.colorify(messageline4));
        // Registering all the classes
        handler = new Handler(this);


        this.fileManager = new FileManager(this, new LinkedHashSet<>(Arrays.asList(
                FileLocation.of("beacons.yml", true, false)
        )));


        this.storage = new SQLStorage(this);
        this.messagesData = new MessagesData(this);
        this.messagesHandler = new MessagesHandler(messagesData, this);
        this.commandManager = new CommandManager(this);

        setupEconomy();

        this.fileManager.loadFiles();

        this.current = new Current(this, messagesHandler);
        this.legacy = new Legacy(this,messagesHandler);
        this.nbt = new NBT(this, legacy, current);
        this.beaconHandler = new BeaconHandler(storage, this);

        startup();



        this.beaconManager = new BeaconManager(beaconHandler, this, messagesHandler);
        beaconManager.startTask(this, beaconHandler);

        this.beaconManagerHeavy = new BeaconManagerHeavy(beaconHandler, this, messagesHandler);
        beaconManagerHeavy.startTask(this, beaconHandler);



        // Registering events
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new BeaconBreak(messagesHandler, beaconHandler, nbt), this);
        pm.registerEvents(new BeaconPlace(nbt, beaconHandler, messagesHandler, this), this);
        beaconInteract = new BeaconInteract(messagesHandler, beaconHandler, nbt, this);
        pm.registerEvents(beaconInteract, this);





        registerCommands();
        time = System.currentTimeMillis() - time;
        String lineend = "&a===============[&b" + time + "ms &a]=================";
        console.sendMessage(HexUtil.colorify(lineend));
    }

    public void updateLinking(Player player, BeaconData beaconData){
        beaconInteract.startLinking(player, beaconData);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }

    @Override
    public void onDisable() {
        storage.closeConnections();

    }

    private void startup(){
        storage.createTable();

        //dataStorage.setup();
        messagesData.setup();
        saveDefaultConfig();

        beaconHandler.setup();
    }

    public void reload(){
        beaconHandler.setup();

        messagesData.reloadMessages();

        reloadConfig();

        messagesHandler.reload();

        fileManager.clear();
        fileManager.loadFiles();



        beaconManagerHeavy.startTask(this, beaconHandler);
        beaconManager.startTask(this, beaconHandler);
    }


    public FileManager getFileManager(){
        return this.fileManager;
    }

    /*
        Needs to be re-done. (Already checked how Songoda does it looks noice)-
     */
    private void registerCommands() {

        commandManager.addMainCommand("simplebeacons").addSubCommands(
                new GiveCommand(this, messagesHandler, nbt),
                new ReloadCommand(this, messagesHandler),
                new AboutCommand()
        );


    }
}
