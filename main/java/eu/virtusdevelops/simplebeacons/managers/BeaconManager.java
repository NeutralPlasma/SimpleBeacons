package eu.virtusdevelops.simplebeacons.managers;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.managers.modules.*;
import eu.virtusdevelops.simplebeacons.storage.BeaconHandler;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class BeaconManager {

    private static BeaconManager instance;
    private BeaconHandler beaconHandler;
    private MessagesHandler messagesHandler;
    private SimpleBeacons plugin;
    private Modul effectsModule = new EffectsModul();
    private Modul protectModule = new ProtectModul();
    private Modul breedModul = new BreedingModul();
    private Modul itemModul = new ItemModul();

    private BukkitTask task;

    public BeaconManager(BeaconHandler beaconHandler, SimpleBeacons simpleBeacons, MessagesHandler messagesHandler){
        this.beaconHandler = beaconHandler;
        this.plugin = simpleBeacons;
        this.messagesHandler = messagesHandler;


    }
    public void startTask(SimpleBeacons plug, BeaconHandler beaconHandler) {
        this.plugin = plug;
        this.beaconHandler = beaconHandler;

        if(task != null) {
            task.cancel();
        }

        task = Bukkit.getScheduler().runTaskTimer(plugin, this::task, 0L, plugin.getConfig().getInt("BEACON_TICK_RATE"));


    }

    public void task() {
        long time = System.currentTimeMillis();
        new HashMap<>(beaconHandler.getBeacons()).forEach((location, beaconData) ->{
            int tickrate = plugin.getConfig().getInt("BEACON_TICK_RATE");

            for(Module modul : beaconData.getEnabledModules()){
                switch (modul) {
                    case EFFECTS -> {
                        effectsModule.run(beaconData, tickrate, plugin);
                    }
                    case PROTECT -> {
                        protectModule.run(beaconData, tickrate, plugin);
                    }
                    case BREED -> {
                        breedModul.run(beaconData, tickrate, plugin);
                    }
                    case ITEM -> {
                        itemModul.run(beaconData, tickrate, plugin);
                    }
                    default -> {
                    }
                }
            }

        });
        messagesHandler.debug("&8[&6Debug&8] &7Took: {time}&ems &7to update beacons", "{time}:" + (System.currentTimeMillis() - time));
    }
}
