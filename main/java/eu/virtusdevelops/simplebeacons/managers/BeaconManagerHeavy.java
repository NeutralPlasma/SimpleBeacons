package eu.virtusdevelops.simplebeacons.managers;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.managers.modules.FarmModul;
import eu.virtusdevelops.simplebeacons.managers.modules.HarvestModul;
import eu.virtusdevelops.simplebeacons.managers.modules.Modul;
import eu.virtusdevelops.simplebeacons.managers.modules.OreModul;
import eu.virtusdevelops.simplebeacons.storage.BeaconHandler;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class BeaconManagerHeavy{

    private static BeaconManagerHeavy instance;

    private SimpleBeacons plugin;
    private BeaconHandler beaconHandler;
    private MessagesHandler messagesHandler;
    private Modul farmModule = new FarmModul();
    private OreModul oreModul = new OreModul();
    private HarvestModul harvestModul = new HarvestModul();

    private BukkitTask task;


    public BeaconManagerHeavy(BeaconHandler beaconHandler, SimpleBeacons simpleBeacons, MessagesHandler messagesHandler){
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
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::task, 0L, plugin.getConfig().getInt("BEACON_TICK_RATE_HEAVY"));
    }

    public void task() {
        long time = System.currentTimeMillis();
        new HashMap<>(beaconHandler.getBeacons()).forEach((location, beaconData) ->{
            int tickrate = plugin.getConfig().getInt("BEACON_TICK_RATE");

            for(Module modul : beaconData.getEnabledModules()){
                if(modul == Module.FARM){
                    farmModule.run(beaconData, tickrate, plugin);
                }else if(modul == Module.ORE){
                    oreModul.run(beaconData, tickrate, plugin);
                }else if(modul == Module.HARVESTER){
                    harvestModul.run(beaconData, tickrate, plugin);
                }
            }

        });
        messagesHandler.debug("&8[&6Debug&8] &7Heavy Took: {time}&ems &7to update beacons", "{time}:" + (System.currentTimeMillis() - time));
    }
}
