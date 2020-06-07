package eu.virtusdevelops.simplebeacons;

import eu.virtusdevelops.simplebeacons.command.CommandHandler;
import eu.virtusdevelops.simplebeacons.command.MainCommand;
import eu.virtusdevelops.simplebeacons.command.TabComplete;
import eu.virtusdevelops.simplebeacons.command.commands.AboutCommand;
import eu.virtusdevelops.simplebeacons.command.commands.GiveCommand;
import eu.virtusdevelops.simplebeacons.command.commands.ReloadCommand;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.events.CloseInventoryEvent;
import eu.virtusdevelops.simplebeacons.events.InventoryOpenEvent;
import eu.virtusdevelops.simplebeacons.events.OnClickEvent;
import eu.virtusdevelops.simplebeacons.events.beacons.BeaconBreak;
import eu.virtusdevelops.simplebeacons.events.beacons.BeaconInteract;
import eu.virtusdevelops.simplebeacons.events.beacons.BeaconPlace;
import eu.virtusdevelops.simplebeacons.gui.Handler;
import eu.virtusdevelops.simplebeacons.managers.BeaconManager;
import eu.virtusdevelops.simplebeacons.managers.BeaconManagerHeavy;
import eu.virtusdevelops.simplebeacons.storage.BeaconHandler;
import eu.virtusdevelops.simplebeacons.storage.DataStorage;
import eu.virtusdevelops.simplebeacons.storage.MessagesData;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import eu.virtusdevelops.simplebeacons.utils.NBT.Current;
import eu.virtusdevelops.simplebeacons.utils.NBT.Legacy;
import eu.virtusdevelops.simplebeacons.utils.NBT.NBT;
import eu.virtusdevelops.simplebeacons.utils.TextFormater;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class SimpleBeacons extends JavaPlugin {

    private DataStorage dataStorage;
    private MessagesHandler messagesHandler;
    private MessagesData messagesData;
    private Current current;
    private Legacy legacy;
    private NBT nbt;
    private Handler handler;
    private BeaconHandler beaconHandler;
    private BeaconManager beaconManager;
    private BeaconManagerHeavy beaconManagerHeavy;
    private BeaconInteract beaconInteract;
    private List<String> modules = new ArrayList<>();


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
        console.sendMessage(TextFormater.sFormatText(line));
        console.sendMessage(TextFormater.sFormatText(messageline1));
        console.sendMessage(TextFormater.sFormatText(messageline2));
        console.sendMessage(TextFormater.sFormatText(messageline3));
        console.sendMessage(TextFormater.sFormatText(messageline4));
        // Registering all the classes
        this.dataStorage = new DataStorage(this);
        this.messagesData = new MessagesData(this);
        this.messagesHandler = new MessagesHandler(messagesData, this);

        setupEconomy();

        startup();

        this.current = new Current(this, messagesHandler);
        this.legacy = new Legacy(this,messagesHandler);
        this.nbt = new NBT(this, legacy, current);
        this.beaconHandler = new BeaconHandler(dataStorage, this);


        this.handler = new Handler(this, messagesHandler, beaconHandler);

        this.beaconManager = new BeaconManager(beaconHandler, this, messagesHandler);
        this.beaconManager = beaconManager.startTask(this, beaconHandler);

        this.beaconManagerHeavy = new BeaconManagerHeavy(beaconHandler, this, messagesHandler);
        this.beaconManagerHeavy = beaconManagerHeavy.startTask(this, beaconHandler);



        // Registering events
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new CloseInventoryEvent(this, handler), this);
        pm.registerEvents(new InventoryOpenEvent(handler), this);
        pm.registerEvents(new OnClickEvent(), this);
        pm.registerEvents(new BeaconBreak(messagesHandler, beaconHandler, nbt), this);
        pm.registerEvents(new BeaconPlace(nbt, beaconHandler, messagesHandler, this), this);
        beaconInteract = new BeaconInteract(messagesHandler, beaconHandler, nbt, handler, this);
        pm.registerEvents(beaconInteract, this);

        modules.add("farm:disabled");
        modules.add("effects:disabled");
        modules.add("protect:disabled");
        modules.add("breed:disabled");
        modules.add("ore:disabled");
        modules.add("item:disabled");

        registerCommands();
        time = System.currentTimeMillis() - time;
        String lineend = "&a===============[&b" + time + "ms &a]=================";
        console.sendMessage(TextFormater.sFormatText(lineend));
    }

    public void updateLinking(Player player, BeaconData beaconData){
        beaconInteract.updateLinking(player, beaconData);
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
        dataStorage.saveData();

    }

    private void startup(){
        dataStorage.setup();
        messagesData.setup();
        saveDefaultConfig();
    }

    public void reload(){
        dataStorage.reloadData();
        dataStorage.saveData();
        messagesData.reloadMessages();
        reloadConfig();
        messagesHandler.reload();

        beaconManagerHeavy.cancel();
        beaconManager.cancel();

        this.beaconManagerHeavy = beaconManagerHeavy.startTask(this, beaconHandler);
        this.beaconManager = beaconManager.startTask(this, beaconHandler);
    }

    public List<String> getModules(){
        return this.modules;
    }

    private void registerCommands() {

        CommandHandler chandler = new CommandHandler(messagesHandler);

        //Registers the command /example which has no arguments.
        chandler.register("simplebeacons", new MainCommand(this, handler, messagesHandler));

        //Registers the command /example args based on args[0] (args)
        chandler.register("give", new GiveCommand(nbt, this, messagesHandler));
        chandler.register("reload", new ReloadCommand(nbt, this, messagesHandler));
        chandler.register("about", new AboutCommand(nbt,this,messagesHandler));

        //Tab complete
        getCommand("simplebeacons").setExecutor(chandler);
        getCommand("simplebeacons").setTabCompleter(new TabComplete(this));
    }
}
