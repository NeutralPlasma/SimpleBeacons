package eu.virtusdevelops.simplebeacons.storage;

import com.google.gson.Gson;
import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.data.BeaconLocation;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class BeaconHandler {
    private Gson gson;
    private DataStorage dataStorage;
    private SimpleBeacons simpleBeacons;
    private HashMap<String, BeaconData> beacons = new HashMap<>();

    public BeaconHandler(DataStorage dataStorage, SimpleBeacons simpleBeacons){
        this.gson = new Gson();
        this.dataStorage = dataStorage;
        this.simpleBeacons = simpleBeacons;
        start();
        setup();
    }

    public void addBeacon(BeaconData beaconData){
        String data = gson.toJson(beaconData);
        String path = beaconData.beaconLocation.toString();
        dataStorage.getData().set("beacons." + path, data);

        beacons.put(beaconData.beaconLocation.toString(), beaconData);
    }

    public void removeBeacon(BeaconLocation location){
        beacons.remove(location.toString());
        dataStorage.getData().set("beacons." + location.toString(), null);
    }

    public void updateBeacon(BeaconData beaconData){
        removeBeacon(beaconData.beaconLocation);
        addBeacon(beaconData);
    }

    public BeaconData getBeacon(BeaconLocation location){
        return beacons.get(location.toString());
    }

    public List<String> getModules(BeaconData data){
        List<String> modules = simpleBeacons.getConfig().getStringList("beacons." + data.level + ".modules").stream().map(module -> module = module + ":disabled").collect(Collectors.toList());
        List<String> modules2 = simpleBeacons.getConfig().getStringList("global.modules").stream().map(module -> module = module + ":disabled").collect(Collectors.toList());
        TreeSet<String> set = new TreeSet<>(modules);
        set.addAll(modules2);
        return new ArrayList<>(set);
    }

    public List<String> getModulesRaw(BeaconData data){
        List<String> modules = simpleBeacons.getConfig().getStringList("beacons." + data.level + ".modules");
        List<String> modules2 = simpleBeacons.getConfig().getStringList("global.modules");
        TreeSet<String> set = new TreeSet<>(modules);
        set.addAll(modules2);
        return new ArrayList<>(set);
    }

    public void setup(){
        try {
            ConfigurationSection configurationSection = dataStorage.getData().getConfigurationSection("beacons");
            for (String entered : configurationSection.getKeys(true)) {

                String data = dataStorage.getData().getString("beacons." + entered);

                BeaconData beaconData = gson.fromJson(data, BeaconData.class);
                if (beaconData != null) {
                    beacons.put(beaconData.beaconLocation.toString(), beaconData);
                }

            }
        }catch (Exception ignored) {}
    }
    public HashMap<String, BeaconData> getBeacons(){
        return beacons;
    }

    public void start(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(simpleBeacons, () -> {
            dataStorage.saveData();
        }, 20L, 600L);
    }
}
