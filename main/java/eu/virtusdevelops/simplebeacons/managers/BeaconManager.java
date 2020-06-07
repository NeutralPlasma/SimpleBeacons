package eu.virtusdevelops.simplebeacons.managers;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.managers.modules.*;
import eu.virtusdevelops.simplebeacons.storage.BeaconHandler;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class BeaconManager extends BukkitRunnable {

    private static BeaconManager instance;
    private BeaconHandler beaconHandler;
    private MessagesHandler messagesHandler;
    private SimpleBeacons plugin;
    private Modul effectsModule = new EffectsModul();
    private Modul protectModule = new ProtectModul();
    private Modul breedModul = new BreedingModul();
    private Modul itemModul = new ItemModul();

    public BeaconManager(BeaconHandler beaconHandler, SimpleBeacons simpleBeacons, MessagesHandler messagesHandler){
        this.beaconHandler = beaconHandler;
        this.plugin = simpleBeacons;
        this.messagesHandler = messagesHandler;
    }
    public BeaconManager startTask(SimpleBeacons plug, BeaconHandler beaconHandler) {
        this.plugin = plug;
        this.beaconHandler = beaconHandler;
        if (instance == null) {
            instance = new BeaconManager(beaconHandler, plug, messagesHandler);
            int tickrate = plugin.getConfig().getInt("BEACON_TICK_RATE");
            instance.runTaskTimer(plug, 50L, tickrate);
        }
        return instance;
    }


    @Override
    public void run() {
        long time = System.currentTimeMillis();
        new HashMap<>(beaconHandler.getBeacons()).forEach((location, beaconData) ->{
            int tickrate = plugin.getConfig().getInt("BEACON_TICK_RATE");

            for(String modul : beaconData.enabledModules){
                if(modul.equalsIgnoreCase("effects:enabled")){
                    effectsModule.run(beaconData, tickrate, plugin);
                }else if(modul.equalsIgnoreCase("protect:enabled")){
                    protectModule.run(beaconData, tickrate, plugin);
                }else if(modul.equalsIgnoreCase("breed:enabled")){
                    breedModul.run(beaconData, tickrate, plugin);
                }else if(modul.equalsIgnoreCase("item:enabled")){
                    itemModul.run(beaconData, tickrate,plugin);
                }
            }

            //beaconData.giveEffects(tickrate);
            //beaconData.blockEffects(plugin);
            //Bukkit.getConsoleSender().sendMessage("Beacon: " + location);
        });
        messagesHandler.debug("&8[&6Debug&8] &7Took: {time}&ems &7to update beacons", "{time}:" + (System.currentTimeMillis() - time));
        //Bukkit.getConsoleSender().sendMessage("Took: " + (System.currentTimeMillis() - time) + "ms to process all beacons" );
    }
}
