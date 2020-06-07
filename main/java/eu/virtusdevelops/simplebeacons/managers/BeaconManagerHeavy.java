package eu.virtusdevelops.simplebeacons.managers;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.managers.modules.FarmModul;
import eu.virtusdevelops.simplebeacons.managers.modules.Modul;
import eu.virtusdevelops.simplebeacons.managers.modules.OreModul;
import eu.virtusdevelops.simplebeacons.storage.BeaconHandler;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class BeaconManagerHeavy extends BukkitRunnable {

    private static BeaconManagerHeavy instance;

    private SimpleBeacons plugin;
    private BeaconHandler beaconHandler;
    private MessagesHandler messagesHandler;
    private Modul farmModule = new FarmModul();
    private OreModul oreModul = new OreModul();
    private int current = -1;


    public BeaconManagerHeavy(BeaconHandler beaconHandler, SimpleBeacons simpleBeacons, MessagesHandler messagesHandler){
        this.beaconHandler = beaconHandler;
        this.plugin = simpleBeacons;
        this.messagesHandler = messagesHandler;
    }
    public BeaconManagerHeavy startTask(SimpleBeacons plug, BeaconHandler beaconHandler) {
        this.plugin = plug;
        this.beaconHandler = beaconHandler;
        if (instance == null) {
            instance = new BeaconManagerHeavy(beaconHandler, plug, messagesHandler);
            int tickrate = plugin.getConfig().getInt("BEACON_TICK_RATE_HEAVY");
            instance.runTaskTimer(plug, 50L, tickrate);
        }
        return instance;
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        current++;
        new HashMap<>(beaconHandler.getBeacons()).forEach((location, beaconData) ->{
            int tickrate = plugin.getConfig().getInt("BEACON_TICK_RATE");

            for(String modul : beaconData.enabledModules){
                if(modul.equalsIgnoreCase("farm:enabled")){
                    farmModule.run(beaconData, tickrate, plugin);
                }else if(modul.equalsIgnoreCase("ore:enabled")){
                    oreModul.run(beaconData, tickrate, plugin, current);
                }
            }
            if(current > 255){
                current = 0;
            }
            //beaconData.giveEffects(tickrate);
            //beaconData.blockEffects(plugin);
            //Bukkit.getConsoleSender().sendMessage("Beacon: " + location);
        });
        messagesHandler.debug("&8[&6Debug&8] &7Heavy Took: {time}&ems &7to update beacons", "{time}:" + (System.currentTimeMillis() - time));
        //Bukkit.getConsoleSender().sendMessage("Took: " + (System.currentTimeMillis() - time) + "ms to process all beacons" );
    }
}
