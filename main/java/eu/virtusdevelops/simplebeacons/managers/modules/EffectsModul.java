package eu.virtusdevelops.simplebeacons.managers.modules;


import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectsModul extends Modul {

    @Override
    public void run(BeaconData beaconData, int tickrate, SimpleBeacons simpleBeacons) {
        Location loc = beaconData.getBeaconLocation().getBukkitLocation();
        Block block = loc.getBlock();
        if(beaconData.isChunkLoaded()) {
            Beacon beacon = (Beacon) block.getState();
            //Bukkit.getConsoleSender().sendMessage("Tier: " + beacon.getTier());
            if (beacon.getTier() > 0) {
                for (Entity entity : beacon.getEntitiesInRange()) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        //Bukkit.getConsoleSender().sendMessage(player.getName());
                        String[] effect = beaconData.getSelectedEffect().split(":");
                        if (effect.length > 1) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect[0]), tickrate + 20, Integer.parseInt(effect[1])));
                        } else {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(effect[0]), tickrate + 20, 0));
                        }
                    }
                }
            }
        }
        super.run(beaconData, tickrate, simpleBeacons);
    }
}
