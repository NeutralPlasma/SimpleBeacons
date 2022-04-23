package eu.virtusdevelops.simplebeacons.gui;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.managers.Module;
import eu.virtusdevelops.simplebeacons.storage.BeaconHandler;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import eu.virtusdevelops.virtuscore.gui.Icon;
import eu.virtusdevelops.virtuscore.gui.Paginator;
import eu.virtusdevelops.virtuscore.utils.ItemUtils;
import eu.virtusdevelops.virtuscore.utils.TextUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleGUI {
    private Paginator paginator;
    private Player player;
    private BeaconData beaconData;

    private SimpleBeacons plugin;
    private BeaconHandler beaconHandler;
    private MessagesHandler message;


    public ModuleGUI(Player player, BeaconData beaconData, SimpleBeacons plugin, BeaconHandler beaconHandler, MessagesHandler message){
        this.player = player;
        this.beaconData = beaconData;
        this.plugin = plugin;
        this.beaconHandler = beaconHandler;
        this.message = message;

        paginator = new Paginator(player, Collections.emptyList(), List.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34), TextUtils.colorFormat("&8[&bModules menu&8]"), 45);

        load();
    }

    private void load(){
        refresh();
        paginator.page();
    }

    private void refresh(){
        List<Icon> icons = new ArrayList<>();


        for(Module module : beaconHandler.getModules(beaconData)){

            List<Module> enabled = beaconData.getEnabledModules();

            if(!player.hasPermission("simplebeacons.module." + module)){
                continue;
            }
            Material mat = switch (module){
                case ORE -> Material.IRON_ORE;
                case FARM -> Material.DIAMOND_HOE;
                case ITEM -> Material.HOPPER;
                case BREED -> Material.WHEAT;
                case EFFECTS -> Material.POTION;
                case PROTECT -> Material.DIAMOND_SWORD;
                case HARVESTER -> Material.IRON_HOE;
            };
            ItemStack item = new ItemStack(mat);

            String enabledText = enabled.contains(module) ? "enabled" : "disabled";
            String name = message.getRawMessage("modules.format")
                    .replace("{module}", message.getRawMessage("modules." + module))
                    .replace("{status}", enabledText);
            name = TextUtils.colorFormat(name);

            List<String> lore = new ArrayList<>();
            lore.add("&7Click to enable/disable.");

            item = ItemUtils.setName(item, TextUtils.colorFormat(name));
            item = ItemUtils.setLore(item, TextUtils.colorFormatList(lore));


            Icon icon = new Icon(item);
            icon.addClickAction((player1) -> {
                if(beaconData.getEnabledModules().contains(module)){
                    beaconHandler.removeModule(beaconData, module);
                }else{
                    beaconHandler.addModule(beaconData, module);
                }
                //beaconHandler.removeBeacon(beaconData.getBeaconLocation());
                //beaconHandler.addBeacon(beaconData);
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 10, 1);

                load();

            });
            icons.add(icon);
        }
        paginator.setIcons(icons);
    }
}
