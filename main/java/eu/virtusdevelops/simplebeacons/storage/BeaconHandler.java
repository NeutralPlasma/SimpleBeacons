package eu.virtusdevelops.simplebeacons.storage;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.data.BeaconLocation;
import eu.virtusdevelops.simplebeacons.managers.Module;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class BeaconHandler {
   // private final Gson gson;
    private final SQLStorage sqlStorage;
    private final SimpleBeacons simpleBeacons;
    private final HashMap<String, BeaconData> beacons = new HashMap<>();
    private BukkitTask task;

    public BeaconHandler(SQLStorage storage, SimpleBeacons simpleBeacons){
        //this.gson = new Gson();
        this.sqlStorage = storage;
        this.simpleBeacons = simpleBeacons;
        //setup();
        //start();
    }

    public void addBeacon(BeaconData beaconData){
        Bukkit.getScheduler().runTaskAsynchronously(simpleBeacons, (Runnable) -> {
                BeaconData newData  = sqlStorage.addBeacon(beaconData);
                beacons.remove(beaconData.getBeaconLocation().toString(), beaconData);
                beacons.put(newData.getBeaconLocation().toString(), newData);
        });
        beacons.put(beaconData.getBeaconLocation().toString(), beaconData);
    }

    public void addLocation(BeaconData data, BeaconLocation location){
        Bukkit.getScheduler().runTaskAsynchronously(simpleBeacons, (Runnable) -> {
            sqlStorage.addLinkedLocation(data, location);

            //beacons.put(newData.getBeaconLocation().toString(), newData);
        });
        data.addLinkedLocation(location);
    }


    public void addModule(BeaconData data, Module module){
        Bukkit.getScheduler().runTaskAsynchronously(simpleBeacons, (Runnable) -> {
            sqlStorage.addBeaconModule(module, data);

            //beacons.put(newData.getBeaconLocation().toString(), newData);
        });
        data.addEnabledModule(module);
    }

    public void removeModule(BeaconData data, Module module){
        Bukkit.getScheduler().runTaskAsynchronously(simpleBeacons, (Runnable) -> {
            sqlStorage.removeBeaconModule(module, data);

            //beacons.put(newData.getBeaconLocation().toString(), newData);
        });
        data.removeEnabledModule(module);
    }


    public void clearLocations(BeaconData data){
        Bukkit.getScheduler().runTaskAsynchronously(simpleBeacons, (Runnable) -> {
            sqlStorage.removeAllLocations(data);
        });
        data.setLinkedLocations(new ArrayList<>());
    }

    public void removeBeacon(BeaconLocation location){
        Bukkit.getScheduler().runTaskAsynchronously(simpleBeacons, (Runnable) -> {
            BeaconData data = beacons.get(location.toString());
            for(Module module : data.getEnabledModules()){
                sqlStorage.removeBeaconModule(module, data);
            }
            for(BeaconLocation loc : data.getLinkedLocations()){
                sqlStorage.removeLinkedLocation(loc, data);
            }

            sqlStorage.removeBeacon(beacons.get(location.toString()));
            beacons.remove(location.toString());
        });
    }

    public void updateBeacon(BeaconData beaconData){
        Bukkit.getScheduler().runTaskAsynchronously(simpleBeacons, (Runnable) -> {
            sqlStorage.updateBeacon(beaconData);
        });
    }

    public BeaconData getBeacon(BeaconLocation location){
        return beacons.get(location.toString());
    }

    public List<Module> getModules(BeaconData data){
        List<Module> modules1 = simpleBeacons.getFileManager().getConfiguration("beacons").getStringList("beacons." + data.getLevel() + ".modules")
                .stream()
                .map(Module::valueOf).toList();
        List<Module> modules2 = simpleBeacons.getConfig().getStringList("global.modules")
                .stream()
                .map(Module::valueOf).toList();
        TreeSet<Module> set = new TreeSet<>(modules1);
        set.addAll(modules2);
        return new ArrayList<>(set);
    }

    public List<String> getEffects(BeaconData data){
        List<String> modules = simpleBeacons.getFileManager().getConfiguration("beacons").getStringList("beacons." + data.getLevel() + ".effects");
        List<String> modules2 = simpleBeacons.getConfig().getStringList("global.effects");
        TreeSet<String> set = new TreeSet<>(modules);
        set.addAll(modules2);
        return new ArrayList<>(set);
    }

    public void registerBeacons(List<BeaconData> beaconDataList){
        List<Integer> loaded = new ArrayList<>();
        task = Bukkit.getScheduler().runTaskTimer(simpleBeacons, new Runnable() {
            @Override
            public void run() {
                List<BeaconData> data = beaconDataList.stream().filter(beacon -> !loaded.contains(beacon.getId())).toList();

                for(BeaconData beacon : data){
                    if(beacon.getBeaconLocation().getWorld() != null){
                        beacon.init();
                        beacons.put(beacon.getBeaconLocation().toString(), beacon);
                        loaded.add(beacon.getId());
                    }
                }
                if((beaconDataList.size() - loaded.size()) < 1){
                    task.cancel();
                }
                simpleBeacons.getLogger().severe("Couldnt load: " + (beaconDataList.size() - loaded.size()) + " beacons! Retrying in 5 seconds." );

            }
        }, 0L, 100L);


    }

    public void setup(){
        try {
            // wait for worlds to load somehow?
            beacons.clear();
            Bukkit.getScheduler().runTaskAsynchronously(simpleBeacons, (runnable) -> {
                List<BeaconData> beaconsData = new ArrayList<>();
                int count = 0;
                for(BeaconData data : sqlStorage.getAllBeacons()){
                    beaconsData.add(data);
                    count++;
                }
                simpleBeacons.getLogger().info("Loaded " + count + " beacons from database.");
                registerBeacons(beaconsData);
            });






            /*ConfigurationSection configurationSection = dataStorage.getData().getConfigurationSection("beacons");
            for (String entered : configurationSection.getKeys(true)) {

                BeaconData data = (BeaconData) configurationSection.get( entered, BeaconData.class);
                if(data != null){
                    beacons.put(data.getBeaconLocation().toString(), data);
                }

            }*/
        }catch (Exception ignored) {}
    }
    public HashMap<String, BeaconData> getBeacons(){
        return beacons;
    }

    /*public void start(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(simpleBeacons, dataStorage::saveData, 20L, 600L);
    }*/
}
